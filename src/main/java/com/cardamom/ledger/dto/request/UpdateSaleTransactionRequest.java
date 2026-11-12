package com.cardamom.ledger.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public class UpdateSaleTransactionRequest {

    private Long batchId;

    @Size(max = 150, message = "Buyer/Auction name cannot exceed 150 characters")
    private String buyerOrAuctionHouse;

    @DecimalMin(value = "0.01", message = "Sold quantity must be greater than zero")
    @Digits(integer = 8, fraction = 2, message = "Sold quantity format is invalid (max 2 decimal places)")
    private BigDecimal totalQuantitySoldKg;

    @DecimalMin(value = "0.01", message = "Price per kg must be greater than zero")
    @Digits(integer = 8, fraction = 2, message = "Price per kg format is invalid (max 2 decimal places)")
    private BigDecimal pricePerKg;

    @DecimalMin(value = "0.00", message = "Commission deduction cannot be negative")
    @Digits(integer = 8, fraction = 2, message = "Commission format is invalid (max 2 decimal places)")
    private BigDecimal commissionDeduction;

    @PastOrPresent(message = "Sale date cannot be in the future")
    private LocalDate saleDate;

    public UpdateSaleTransactionRequest() {
    }

    public Long getBatchId() {
        return batchId;
    }

    public void setBatchId(Long batchId) {
        this.batchId = batchId;
    }

    public String getBuyerOrAuctionHouse() {
        return buyerOrAuctionHouse;
    }

    public void setBuyerOrAuctionHouse(String buyerOrAuctionHouse) {
        this.buyerOrAuctionHouse = buyerOrAuctionHouse;
    }

    public BigDecimal getTotalQuantitySoldKg() {
        return totalQuantitySoldKg;
    }

    public void setTotalQuantitySoldKg(BigDecimal totalQuantitySoldKg) {
        this.totalQuantitySoldKg = totalQuantitySoldKg;
    }

    public BigDecimal getPricePerKg() {
        return pricePerKg;
    }

    public void setPricePerKg(BigDecimal pricePerKg) {
        this.pricePerKg = pricePerKg;
    }

    public BigDecimal getCommissionDeduction() {
        return commissionDeduction;
    }

    public void setCommissionDeduction(BigDecimal commissionDeduction) {
        this.commissionDeduction = commissionDeduction;
    }

    public LocalDate getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(LocalDate saleDate) {
        this.saleDate = saleDate;
    }
}
