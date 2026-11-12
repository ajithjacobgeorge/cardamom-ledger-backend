package com.cardamom.ledger.dto.response;

import java.math.BigDecimal;

public class ProductionCostMetrics {

    private BigDecimal totalOperatingExpenses;
    private BigDecimal totalBhattiDryingCosts;
    private BigDecimal totalCombinedExpenses;
    private BigDecimal totalDryYieldKg;
    private BigDecimal costOfProductionPerDryKg;
    private BigDecimal averageRealizationPricePerKg;
    private BigDecimal netProfitMarginPerDryKg;

    public ProductionCostMetrics() {
    }

    public BigDecimal getTotalOperatingExpenses() {
        return totalOperatingExpenses;
    }

    public void setTotalOperatingExpenses(BigDecimal totalOperatingExpenses) {
        this.totalOperatingExpenses = totalOperatingExpenses;
    }

    public BigDecimal getTotalBhattiDryingCosts() {
        return totalBhattiDryingCosts;
    }

    public void setTotalBhattiDryingCosts(BigDecimal totalBhattiDryingCosts) {
        this.totalBhattiDryingCosts = totalBhattiDryingCosts;
    }

    public BigDecimal getTotalCombinedExpenses() {
        return totalCombinedExpenses;
    }

    public void setTotalCombinedExpenses(BigDecimal totalCombinedExpenses) {
        this.totalCombinedExpenses = totalCombinedExpenses;
    }

    public BigDecimal getTotalDryYieldKg() {
        return totalDryYieldKg;
    }

    public void setTotalDryYieldKg(BigDecimal totalDryYieldKg) {
        this.totalDryYieldKg = totalDryYieldKg;
    }

    public BigDecimal getCostOfProductionPerDryKg() {
        return costOfProductionPerDryKg;
    }

    public void setCostOfProductionPerDryKg(BigDecimal costOfProductionPerDryKg) {
        this.costOfProductionPerDryKg = costOfProductionPerDryKg;
    }

    public BigDecimal getAverageRealizationPricePerKg() {
        return averageRealizationPricePerKg;
    }

    public void setAverageRealizationPricePerKg(BigDecimal averageRealizationPricePerKg) {
        this.averageRealizationPricePerKg = averageRealizationPricePerKg;
    }

    public BigDecimal getNetProfitMarginPerDryKg() {
        return netProfitMarginPerDryKg;
    }

    public void setNetProfitMarginPerDryKg(BigDecimal netProfitMarginPerDryKg) {
        this.netProfitMarginPerDryKg = netProfitMarginPerDryKg;
    }
}
