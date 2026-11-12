package com.cardamom.ledger;

import com.cardamom.ledger.domain.enums.CardamomVariety;
import com.cardamom.ledger.domain.enums.ExpenseCategory;
import com.cardamom.ledger.domain.enums.HarvestStatus;
import com.cardamom.ledger.dto.request.CreateBlockRequest;
import com.cardamom.ledger.dto.request.CreateExpenseRequest;
import com.cardamom.ledger.dto.request.CreateHarvestBatchRequest;
import com.cardamom.ledger.dto.request.CreateSaleTransactionRequest;
import com.cardamom.ledger.dto.response.BlockResponse;
import com.cardamom.ledger.dto.response.EstateSummaryResponse;
import com.cardamom.ledger.dto.response.ExpenseResponse;
import com.cardamom.ledger.dto.response.HarvestBatchResponse;
import com.cardamom.ledger.dto.response.SaleTransactionResponse;
import com.cardamom.ledger.exception.BusinessValidationException;
import com.cardamom.ledger.service.BlockService;
import com.cardamom.ledger.service.EstateAnalyticsService;
import com.cardamom.ledger.service.ExpenseService;
import com.cardamom.ledger.service.HarvestBatchService;
import com.cardamom.ledger.service.SaleTransactionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class EstateOperationsIntegrationTest {

    @Autowired
    private BlockService blockService;

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private HarvestBatchService harvestBatchService;

    @Autowired
    private SaleTransactionService saleTransactionService;

    @Autowired
    private EstateAnalyticsService analyticsService;

    @Test
    @DisplayName("Should successfully execute end-to-end plantation operations lifecycle")
    void testEndToEndPlantationWorkflow() {
        // 1. Create a new plantation block
        CreateBlockRequest blockReq = new CreateBlockRequest(
                "High Mountain Block D",
                CardamomVariety.MYSORE,
                new BigDecimal("6.50"),
                2022
        );
        BlockResponse block = blockService.createBlock(blockReq);
        assertThat(block.getId()).isNotNull();
        assertThat(block.getName()).isEqualTo("High Mountain Block D");

        // 2. Add an expense to this block
        CreateExpenseRequest expReq = new CreateExpenseRequest(
                block.getId(),
                ExpenseCategory.FERTILIZER,
                new BigDecimal("15000.00"),
                LocalDate.now(),
                "Organic compost application"
        );
        ExpenseResponse expense = expenseService.createExpense(expReq);
        assertThat(expense.getId()).isNotNull();
        assertThat(expense.getAmount()).isEqualByComparingTo("15000.00");
        assertThat(expense.getBlockName()).isEqualTo("High Mountain Block D");

        // 3. Record a harvest batch with green weight and dry weight
        CreateHarvestBatchRequest batchReq = new CreateHarvestBatchRequest(
                block.getId(),
                LocalDate.now(),
                new BigDecimal("1000.00"),
                new BigDecimal("215.00"), // ~21.5% outturn
                new BigDecimal("5500.00"),
                "{\"AGEB\": 80.0, \"AGB\": 95.0, \"Bulk\": 40.0}",
                HarvestStatus.STORED
        );
        HarvestBatchResponse batch = harvestBatchService.createBatch(batchReq);
        assertThat(batch.getId()).isNotNull();
        assertThat(batch.getOutturnPercentage()).isEqualByComparingTo("21.50");
        assertThat(batch.getRemainingUnsoldQuantityKg()).isEqualByComparingTo("215.00");

        // 4. Execute a sale transaction
        CreateSaleTransactionRequest saleReq = new CreateSaleTransactionRequest(
                batch.getId(),
                "Vandanmedu Green Gold Spices",
                new BigDecimal("100.00"),
                new BigDecimal("2600.00"), // gross = 260,000
                new BigDecimal("2600.00"), // commission = 1% = 2,600
                LocalDate.now()
        );
        SaleTransactionResponse sale = saleTransactionService.createSale(saleReq);
        assertThat(sale.getId()).isNotNull();
        assertThat(sale.getGrossAmount()).isEqualByComparingTo("260000.00");
        assertThat(sale.getNetRealizationAmount()).isEqualByComparingTo("257400.00");

        // 5. Verify batch unsold balance is updated
        HarvestBatchResponse updatedBatch = harvestBatchService.getBatchById(batch.getId());
        assertThat(updatedBatch.getRemainingUnsoldQuantityKg()).isEqualByComparingTo("115.00");

        // 6. Test inventory over-sale guard
        CreateSaleTransactionRequest excessiveSaleReq = new CreateSaleTransactionRequest(
                batch.getId(),
                "Unauthorized Buyer",
                new BigDecimal("200.00"), // only 115 kg left!
                new BigDecimal("2500.00"),
                new BigDecimal("500.00"),
                LocalDate.now()
        );
        assertThatThrownBy(() -> saleTransactionService.createSale(excessiveSaleReq))
                .isInstanceOf(BusinessValidationException.class)
                .hasMessageContaining("Insufficient cured stock");

        // 7. Verify analytics summary
        EstateSummaryResponse summary = analyticsService.getEstateSummary();
        assertThat(summary.getTotalBlocksCount()).isGreaterThanOrEqualTo(4);
        assertThat(summary.getTotalNetRealization()).isGreaterThan(BigDecimal.ZERO);
    }
}
