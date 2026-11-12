package com.cardamom.ledger.dto.response;

import java.math.BigDecimal;
import java.util.Map;

public class EstateSummaryResponse {

    private BigDecimal totalPlantedAreaAcres;
    private long totalBlocksCount;
    private BigDecimal totalGreenWeightHarvestedKg;
    private BigDecimal totalDryWeightCuredKg;
    private BigDecimal overallOutturnPercentage;
    private BigDecimal totalExpensesAmount;
    private BigDecimal totalBhattiCosts;
    private BigDecimal totalQuantitySoldKg;
    private BigDecimal totalGrossRealization;
    private BigDecimal totalCommissionPaid;
    private BigDecimal totalNetRealization;
    private BigDecimal netOperationalProfit;
    private Map<String, BigDecimal> expensesByCategory;
    private ProductionCostMetrics productionCostMetrics;
    private java.util.List<BlockYieldEfficiency> blockYieldEfficiencies;
    private java.util.List<MonthlyProfitMarginTrend> monthlyProfitMarginTrends;

    public EstateSummaryResponse() {
    }

    public ProductionCostMetrics getProductionCostMetrics() {
        return productionCostMetrics;
    }

    public void setProductionCostMetrics(ProductionCostMetrics productionCostMetrics) {
        this.productionCostMetrics = productionCostMetrics;
    }

    public java.util.List<BlockYieldEfficiency> getBlockYieldEfficiencies() {
        return blockYieldEfficiencies;
    }

    public void setBlockYieldEfficiencies(java.util.List<BlockYieldEfficiency> blockYieldEfficiencies) {
        this.blockYieldEfficiencies = blockYieldEfficiencies;
    }

    public java.util.List<MonthlyProfitMarginTrend> getMonthlyProfitMarginTrends() {
        return monthlyProfitMarginTrends;
    }

    public void setMonthlyProfitMarginTrends(java.util.List<MonthlyProfitMarginTrend> monthlyProfitMarginTrends) {
        this.monthlyProfitMarginTrends = monthlyProfitMarginTrends;
    }

    public BigDecimal getTotalPlantedAreaAcres() {
        return totalPlantedAreaAcres;
    }

    public void setTotalPlantedAreaAcres(BigDecimal totalPlantedAreaAcres) {
        this.totalPlantedAreaAcres = totalPlantedAreaAcres;
    }

    public long getTotalBlocksCount() {
        return totalBlocksCount;
    }

    public void setTotalBlocksCount(long totalBlocksCount) {
        this.totalBlocksCount = totalBlocksCount;
    }

    public BigDecimal getTotalGreenWeightHarvestedKg() {
        return totalGreenWeightHarvestedKg;
    }

    public void setTotalGreenWeightHarvestedKg(BigDecimal totalGreenWeightHarvestedKg) {
        this.totalGreenWeightHarvestedKg = totalGreenWeightHarvestedKg;
    }

    public BigDecimal getTotalDryWeightCuredKg() {
        return totalDryWeightCuredKg;
    }

    public void setTotalDryWeightCuredKg(BigDecimal totalDryWeightCuredKg) {
        this.totalDryWeightCuredKg = totalDryWeightCuredKg;
    }

    public BigDecimal getOverallOutturnPercentage() {
        return overallOutturnPercentage;
    }

    public void setOverallOutturnPercentage(BigDecimal overallOutturnPercentage) {
        this.overallOutturnPercentage = overallOutturnPercentage;
    }

    public BigDecimal getTotalExpensesAmount() {
        return totalExpensesAmount;
    }

    public void setTotalExpensesAmount(BigDecimal totalExpensesAmount) {
        this.totalExpensesAmount = totalExpensesAmount;
    }

    public BigDecimal getTotalBhattiCosts() {
        return totalBhattiCosts;
    }

    public void setTotalBhattiCosts(BigDecimal totalBhattiCosts) {
        this.totalBhattiCosts = totalBhattiCosts;
    }

    public BigDecimal getTotalQuantitySoldKg() {
        return totalQuantitySoldKg;
    }

    public void setTotalQuantitySoldKg(BigDecimal totalQuantitySoldKg) {
        this.totalQuantitySoldKg = totalQuantitySoldKg;
    }

    public BigDecimal getTotalGrossRealization() {
        return totalGrossRealization;
    }

    public void setTotalGrossRealization(BigDecimal totalGrossRealization) {
        this.totalGrossRealization = totalGrossRealization;
    }

    public BigDecimal getTotalCommissionPaid() {
        return totalCommissionPaid;
    }

    public void setTotalCommissionPaid(BigDecimal totalCommissionPaid) {
        this.totalCommissionPaid = totalCommissionPaid;
    }

    public BigDecimal getTotalNetRealization() {
        return totalNetRealization;
    }

    public void setTotalNetRealization(BigDecimal totalNetRealization) {
        this.totalNetRealization = totalNetRealization;
    }

    public BigDecimal getNetOperationalProfit() {
        return netOperationalProfit;
    }

    public void setNetOperationalProfit(BigDecimal netOperationalProfit) {
        this.netOperationalProfit = netOperationalProfit;
    }

    public Map<String, BigDecimal> getExpensesByCategory() {
        return expensesByCategory;
    }

    public void setExpensesByCategory(Map<String, BigDecimal> expensesByCategory) {
        this.expensesByCategory = expensesByCategory;
    }
}
