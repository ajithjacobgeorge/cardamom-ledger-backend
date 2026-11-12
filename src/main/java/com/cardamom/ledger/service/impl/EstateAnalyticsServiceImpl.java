package com.cardamom.ledger.service.impl;

import com.cardamom.ledger.domain.entity.BlockEntity;
import com.cardamom.ledger.domain.entity.ExpenseEntity;
import com.cardamom.ledger.domain.entity.HarvestBatchEntity;
import com.cardamom.ledger.domain.entity.SaleTransactionEntity;
import com.cardamom.ledger.dto.response.BlockYieldEfficiency;
import com.cardamom.ledger.dto.response.EstateSummaryResponse;
import com.cardamom.ledger.dto.response.MonthlyProfitMarginTrend;
import com.cardamom.ledger.dto.response.ProductionCostMetrics;
import com.cardamom.ledger.repository.BlockRepository;
import com.cardamom.ledger.repository.ExpenseRepository;
import com.cardamom.ledger.repository.HarvestBatchRepository;
import com.cardamom.ledger.repository.SaleTransactionRepository;
import com.cardamom.ledger.service.EstateAnalyticsService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Transactional(readOnly = true)
public class EstateAnalyticsServiceImpl implements EstateAnalyticsService {

    private static final Logger log = LoggerFactory.getLogger(EstateAnalyticsServiceImpl.class);
    private static final DateTimeFormatter MONTH_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM");

    private final BlockRepository blockRepository;
    private final ExpenseRepository expenseRepository;
    private final HarvestBatchRepository harvestBatchRepository;
    private final SaleTransactionRepository saleTransactionRepository;
    private final ObjectMapper objectMapper;

    public EstateAnalyticsServiceImpl(BlockRepository blockRepository,
                                      ExpenseRepository expenseRepository,
                                      HarvestBatchRepository harvestBatchRepository,
                                      SaleTransactionRepository saleTransactionRepository,
                                      ObjectMapper objectMapper) {
        this.blockRepository = blockRepository;
        this.expenseRepository = expenseRepository;
        this.harvestBatchRepository = harvestBatchRepository;
        this.saleTransactionRepository = saleTransactionRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public EstateSummaryResponse getEstateSummary() {
        EstateSummaryResponse summary = new EstateSummaryResponse();

        // 1. Basic Estate Area & Counts
        BigDecimal totalArea = blockRepository.getTotalPlantedAreaAcres();
        long blocksCount = blockRepository.count();
        summary.setTotalPlantedAreaAcres(totalArea != null ? totalArea : BigDecimal.ZERO);
        summary.setTotalBlocksCount(blocksCount);

        // 2. Harvest Totals & Curing Outturn
        BigDecimal greenWeight = harvestBatchRepository.sumTotalGreenWeight();
        BigDecimal dryWeight = harvestBatchRepository.sumTotalDryWeight();
        BigDecimal bhattiCost = harvestBatchRepository.sumTotalBhattiCost();

        greenWeight = greenWeight != null ? greenWeight : BigDecimal.ZERO;
        dryWeight = dryWeight != null ? dryWeight : BigDecimal.ZERO;
        bhattiCost = bhattiCost != null ? bhattiCost : BigDecimal.ZERO;

        summary.setTotalGreenWeightHarvestedKg(greenWeight);
        summary.setTotalDryWeightCuredKg(dryWeight);
        summary.setTotalBhattiCosts(bhattiCost);

        if (greenWeight.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal outturn = dryWeight.multiply(BigDecimal.valueOf(100))
                    .divide(greenWeight, 2, RoundingMode.HALF_UP);
            summary.setOverallOutturnPercentage(outturn);
        } else {
            summary.setOverallOutturnPercentage(BigDecimal.ZERO);
        }

        // 3. Operational Expenses
        BigDecimal totalExpenses = expenseRepository.sumTotalExpenses();
        summary.setTotalExpensesAmount(totalExpenses != null ? totalExpenses : BigDecimal.ZERO);

        Map<String, BigDecimal> expensesByCategory = new LinkedHashMap<>();
        List<Object[]> categorySums = expenseRepository.sumExpensesByCategory();
        for (Object[] row : categorySums) {
            expensesByCategory.put(row[0].toString(), (BigDecimal) row[1]);
        }
        summary.setExpensesByCategory(expensesByCategory);

        // 4. Sales Realizations
        BigDecimal totalQtySold = saleTransactionRepository.sumTotalQuantitySold();
        BigDecimal totalNet = saleTransactionRepository.sumTotalNetRealization();
        BigDecimal totalCommission = saleTransactionRepository.sumTotalCommissionDeduction();

        BigDecimal netRealization = totalNet != null ? totalNet : BigDecimal.ZERO;
        BigDecimal commission = totalCommission != null ? totalCommission : BigDecimal.ZERO;
        BigDecimal gross = netRealization.add(commission);

        summary.setTotalQuantitySoldKg(totalQtySold != null ? totalQtySold : BigDecimal.ZERO);
        summary.setTotalCommissionPaid(commission);
        summary.setTotalGrossRealization(gross);
        summary.setTotalNetRealization(netRealization);

        BigDecimal directExpenses = summary.getTotalExpensesAmount();
        BigDecimal totalAllExpenses = directExpenses.add(bhattiCost);
        summary.setNetOperationalProfit(netRealization.subtract(totalAllExpenses));

        // 5. Cost of Production per Dry Kilogram (Total Annual Expenses / Total Dry Yield Kg)
        ProductionCostMetrics costMetrics = new ProductionCostMetrics();
        costMetrics.setTotalOperatingExpenses(directExpenses);
        costMetrics.setTotalBhattiDryingCosts(bhattiCost);
        costMetrics.setTotalCombinedExpenses(totalAllExpenses);
        costMetrics.setTotalDryYieldKg(dryWeight);

        if (dryWeight.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal costPerDryKg = totalAllExpenses.divide(dryWeight, 2, RoundingMode.HALF_UP);
            costMetrics.setCostOfProductionPerDryKg(costPerDryKg);
        } else {
            costMetrics.setCostOfProductionPerDryKg(BigDecimal.ZERO);
        }

        if (summary.getTotalQuantitySoldKg().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal avgPrice = gross.divide(summary.getTotalQuantitySoldKg(), 2, RoundingMode.HALF_UP);
            costMetrics.setAverageRealizationPricePerKg(avgPrice);
            costMetrics.setNetProfitMarginPerDryKg(avgPrice.subtract(costMetrics.getCostOfProductionPerDryKg()));
        } else {
            costMetrics.setAverageRealizationPricePerKg(BigDecimal.ZERO);
            costMetrics.setNetProfitMarginPerDryKg(BigDecimal.ZERO);
        }
        summary.setProductionCostMetrics(costMetrics);

        // 6. Yield Efficiency per Acre (Kg/Acre comparison across blocks)
        List<BlockEntity> allBlocks = blockRepository.findAll();
        List<BlockYieldEfficiency> blockEfficiencies = new ArrayList<>();

        for (BlockEntity block : allBlocks) {
            BlockYieldEfficiency eff = new BlockYieldEfficiency();
            eff.setBlockId(block.getId());
            eff.setBlockName(block.getName());
            eff.setVariety(block.getVariety());
            eff.setTotalAreaAcres(block.getTotalAreaAcres());

            BigDecimal bGreen = harvestBatchRepository.sumGreenWeightByBlockId(block.getId());
            BigDecimal bDry = harvestBatchRepository.sumDryWeightByBlockId(block.getId());
            BigDecimal bExpenses = expenseRepository.sumExpensesByBlockId(block.getId());

            bGreen = bGreen != null ? bGreen : BigDecimal.ZERO;
            bDry = bDry != null ? bDry : BigDecimal.ZERO;
            bExpenses = bExpenses != null ? bExpenses : BigDecimal.ZERO;

            eff.setGreenHarvestKg(bGreen);
            eff.setDryYieldKg(bDry);
            eff.setDirectExpenses(bExpenses);

            if (block.getTotalAreaAcres() != null && block.getTotalAreaAcres().compareTo(BigDecimal.ZERO) > 0) {
                eff.setYieldPerAcreKg(bDry.divide(block.getTotalAreaAcres(), 2, RoundingMode.HALF_UP));
                eff.setCostPerAcre(bExpenses.divide(block.getTotalAreaAcres(), 2, RoundingMode.HALF_UP));
            } else {
                eff.setYieldPerAcreKg(BigDecimal.ZERO);
                eff.setCostPerAcre(BigDecimal.ZERO);
            }

            if (bGreen.compareTo(BigDecimal.ZERO) > 0) {
                eff.setCuringOutturnPercentage(bDry.multiply(BigDecimal.valueOf(100))
                        .divide(bGreen, 2, RoundingMode.HALF_UP));
            } else {
                eff.setCuringOutturnPercentage(BigDecimal.ZERO);
            }

            blockEfficiencies.add(eff);
        }
        summary.setBlockYieldEfficiencies(blockEfficiencies);

        // 7. Profit Margin trends grouped by month and auction grade
        summary.setMonthlyProfitMarginTrends(computeMonthlyTrends());

        return summary;
    }

    private List<MonthlyProfitMarginTrend> computeMonthlyTrends() {
        Map<String, MonthlyAggregator> monthMap = new TreeMap<>();

        // Group Sales Transactions by YYYY-MM
        List<SaleTransactionEntity> sales = saleTransactionRepository.findAll();
        for (SaleTransactionEntity sale : sales) {
            String month = sale.getSaleDate().format(MONTH_FORMATTER);
            MonthlyAggregator agg = monthMap.computeIfAbsent(month, k -> new MonthlyAggregator(month));

            BigDecimal gross = sale.getTotalQuantitySoldKg().multiply(sale.getPricePerKg());
            agg.grossRevenue = agg.grossRevenue.add(gross);
            agg.commissionDeduction = agg.commissionDeduction.add(sale.getCommissionDeduction());
            agg.netRevenue = agg.netRevenue.add(sale.getNetRealizationAmount());
            agg.quantitySoldKg = agg.quantitySoldKg.add(sale.getTotalQuantitySoldKg());
        }

        // Group Operational Expenses by YYYY-MM
        List<ExpenseEntity> expenses = expenseRepository.findAll();
        for (ExpenseEntity exp : expenses) {
            String month = exp.getDate().format(MONTH_FORMATTER);
            MonthlyAggregator agg = monthMap.computeIfAbsent(month, k -> new MonthlyAggregator(month));
            agg.operatingExpenses = agg.operatingExpenses.add(exp.getAmount());
        }

        // Group Bhatti / Drying costs & Auction Grade distribution from Harvest Batches
        List<HarvestBatchEntity> batches = harvestBatchRepository.findAll();
        for (HarvestBatchEntity batch : batches) {
            String month = batch.getHarvestDate().format(MONTH_FORMATTER);
            MonthlyAggregator agg = monthMap.computeIfAbsent(month, k -> new MonthlyAggregator(month));

            if (batch.getBhattiCost() != null) {
                agg.operatingExpenses = agg.operatingExpenses.add(batch.getBhattiCost());
            }

            // Parse Grade Breakdown JSON
            if (batch.getGradeBreakdownJson() != null && !batch.getGradeBreakdownJson().isBlank()) {
                try {
                    Map<String, Object> grades = objectMapper.readValue(
                            batch.getGradeBreakdownJson(),
                            new TypeReference<Map<String, Object>>() {}
                    );
                    for (Map.Entry<String, Object> entry : grades.entrySet()) {
                        BigDecimal kg = new BigDecimal(entry.getValue().toString());
                        agg.gradeBreakdownKg.merge(entry.getKey(), kg, BigDecimal::add);
                    }
                } catch (Exception e) {
                    log.debug("Could not parse gradeBreakdownJson for batch {}: {}", batch.getId(), e.getMessage());
                }
            }
        }

        // Transform aggregators into DTOs
        List<MonthlyProfitMarginTrend> trends = new ArrayList<>();
        for (MonthlyAggregator agg : monthMap.values()) {
            MonthlyProfitMarginTrend trend = new MonthlyProfitMarginTrend();
            trend.setMonth(agg.month);
            trend.setGrossRevenue(agg.grossRevenue);
            trend.setCommissionDeduction(agg.commissionDeduction);
            trend.setNetRevenue(agg.netRevenue);
            trend.setOperatingExpenses(agg.operatingExpenses);

            BigDecimal netProfit = agg.netRevenue.subtract(agg.operatingExpenses);
            trend.setNetProfit(netProfit);

            if (agg.netRevenue.compareTo(BigDecimal.ZERO) > 0) {
                trend.setProfitMarginPercentage(
                        netProfit.multiply(BigDecimal.valueOf(100))
                                .divide(agg.netRevenue, 2, RoundingMode.HALF_UP)
                );
            } else {
                trend.setProfitMarginPercentage(BigDecimal.ZERO);
            }

            trend.setQuantitySoldKg(agg.quantitySoldKg);
            trend.setGradeBreakdownKg(agg.gradeBreakdownKg);
            trends.add(trend);
        }

        return trends;
    }

    private static class MonthlyAggregator {
        final String month;
        BigDecimal grossRevenue = BigDecimal.ZERO;
        BigDecimal commissionDeduction = BigDecimal.ZERO;
        BigDecimal netRevenue = BigDecimal.ZERO;
        BigDecimal operatingExpenses = BigDecimal.ZERO;
        BigDecimal quantitySoldKg = BigDecimal.ZERO;
        Map<String, BigDecimal> gradeBreakdownKg = new LinkedHashMap<>();

        MonthlyAggregator(String month) {
            this.month = month;
        }
    }
}
