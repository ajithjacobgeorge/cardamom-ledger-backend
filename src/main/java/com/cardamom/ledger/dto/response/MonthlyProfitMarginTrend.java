package com.cardamom.ledger.dto.response;

import java.math.BigDecimal;
import java.util.Map;

public class MonthlyProfitMarginTrend {

    private String month; // Format: YYYY-MM
    private BigDecimal grossRevenue;
    private BigDecimal commissionDeduction;
    private BigDecimal netRevenue;
    private BigDecimal operatingExpenses;
    private BigDecimal netProfit;
    private BigDecimal profitMarginPercentage;
    private BigDecimal quantitySoldKg;
    private Map<String, BigDecimal> gradeBreakdownKg; // Grade -> Kg sold/processed

    public MonthlyProfitMarginTrend() {
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public BigDecimal getGrossRevenue() {
        return grossRevenue;
    }

    public void setGrossRevenue(BigDecimal grossRevenue) {
        this.grossRevenue = grossRevenue;
    }

    public BigDecimal getCommissionDeduction() {
        return commissionDeduction;
    }

    public void setCommissionDeduction(BigDecimal commissionDeduction) {
        this.commissionDeduction = commissionDeduction;
    }

    public BigDecimal getNetRevenue() {
        return netRevenue;
    }

    public void setNetRevenue(BigDecimal netRevenue) {
        this.netRevenue = netRevenue;
    }

    public BigDecimal getOperatingExpenses() {
        return operatingExpenses;
    }

    public void setOperatingExpenses(BigDecimal operatingExpenses) {
        this.operatingExpenses = operatingExpenses;
    }

    public BigDecimal getNetProfit() {
        return netProfit;
    }

    public void setNetProfit(BigDecimal netProfit) {
        this.netProfit = netProfit;
    }

    public BigDecimal getProfitMarginPercentage() {
        return profitMarginPercentage;
    }

    public void setProfitMarginPercentage(BigDecimal profitMarginPercentage) {
        this.profitMarginPercentage = profitMarginPercentage;
    }

    public BigDecimal getQuantitySoldKg() {
        return quantitySoldKg;
    }

    public void setQuantitySoldKg(BigDecimal quantitySoldKg) {
        this.quantitySoldKg = quantitySoldKg;
    }

    public Map<String, BigDecimal> getGradeBreakdownKg() {
        return gradeBreakdownKg;
    }

    public void setGradeBreakdownKg(Map<String, BigDecimal> gradeBreakdownKg) {
        this.gradeBreakdownKg = gradeBreakdownKg;
    }
}
