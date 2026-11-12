package com.cardamom.ledger.controller;

import com.cardamom.ledger.dto.response.EstateSummaryResponse;
import com.cardamom.ledger.dto.response.ProductionCostMetrics;
import com.cardamom.ledger.service.EstateAnalyticsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AnalyticsController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("dev")
class AnalyticsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EstateAnalyticsService analyticsService;

    @Test
    @DisplayName("GET /api/analytics/summary returns HTTP 200 with complete production cost and block yield metrics")
    void testGetEstateSummary() throws Exception {
        EstateSummaryResponse response = new EstateSummaryResponse();
        response.setTotalPlantedAreaAcres(new BigDecimal("35.75"));
        response.setTotalBlocksCount(3);
        response.setTotalGreenWeightHarvestedKg(new BigDecimal("4470.00"));
        response.setTotalDryWeightCuredKg(new BigDecimal("644.50"));
        response.setOverallOutturnPercentage(new BigDecimal("21.20"));
        response.setTotalExpensesAmount(new BigDecimal("157000.00"));
        response.setTotalBhattiCosts(new BigDecimal("16800.00"));
        response.setTotalGrossRealization(new BigDecimal("917825.00"));
        response.setTotalNetRealization(new BigDecimal("908646.75"));
        response.setNetOperationalProfit(new BigDecimal("734846.75"));

        ProductionCostMetrics costMetrics = new ProductionCostMetrics();
        costMetrics.setTotalOperatingExpenses(new BigDecimal("157000.00"));
        costMetrics.setTotalBhattiDryingCosts(new BigDecimal("16800.00"));
        costMetrics.setTotalCombinedExpenses(new BigDecimal("173800.00"));
        costMetrics.setTotalDryYieldKg(new BigDecimal("644.50"));
        costMetrics.setCostOfProductionPerDryKg(new BigDecimal("269.67"));
        costMetrics.setAverageRealizationPricePerKg(new BigDecimal("2490.71"));
        costMetrics.setNetProfitMarginPerDryKg(new BigDecimal("2221.04"));
        response.setProductionCostMetrics(costMetrics);

        response.setBlockYieldEfficiencies(Collections.emptyList());
        response.setMonthlyProfitMarginTrends(Collections.emptyList());

        when(analyticsService.getEstateSummary()).thenReturn(response);

        mockMvc.perform(get("/api/analytics/summary")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.totalPlantedAreaAcres").value(35.75))
                .andExpect(jsonPath("$.productionCostMetrics.costOfProductionPerDryKg").value(269.67))
                .andExpect(jsonPath("$.productionCostMetrics.netProfitMarginPerDryKg").value(2221.04));

        // Also test alias /api/v1/analytics/summary
        mockMvc.perform(get("/api/v1/analytics/summary")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productionCostMetrics.costOfProductionPerDryKg").value(269.67));
    }
}
