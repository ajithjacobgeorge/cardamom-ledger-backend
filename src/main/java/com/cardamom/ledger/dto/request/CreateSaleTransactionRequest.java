package com.cardamom.ledger.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CreateSaleTransactionRequest {

    @NotNull(message = "Batch ID is required")
    private Long batchId;

    @NotBlank(message = "Buyer or auction house name is required")
    @Size(max = 150, message = "Buyer/Auction name cannot exceed 150 characters")
    private String buyerOrAuctionHouse;

    @NotNull(message = "Total quantity sold (kg) is required")
    @DecimalMin(value = "0.01", message = "Sold quantity must be greater than zero")
    @Digits(integer = 8, fraction = 2, message = "Sold quantity format is invalid (max 2 decimal places)")
    private BigDecimal totalQuantitySoldKg;

    @NotNull(message = "Price per kg is required")
    @DecimalMin(value = "0.01", message = "Price per kg must be greater than zero")
    @Digits(integer = 8, fraction = 2, message = "Price per kg format is invalid (max 2 decimal places)")
    private BigDecimal pricePerKg;

    @NotNull(message = "Commission deduction is required")
    @DecimalMin(value = "0.00", message = "Commission deduction cannot be negative")
    @Digits(integer = 8, fraction = 2, message = "Commission format is invalid (max 2 decimal places)")
    private BigDecimal commissionDeduction;

    @NotNull(message = "Sale date is required")
    @PastOrPresent(message = "Sale date cannot be in the future")
    private LocalDate saleDate;

    public CreateSaleTransactionRequest() {
    }

    public CreateSaleTransactionRequest(Long batchId, String buyerOrAuctionHouse,
                                        BigDecimal totalQuantitySoldKg, BigDecimal pricePerKg,
                                        BigDecimal commissionDeduction, LocalDate saleDate) {
        this.batchId = batchId;
        this.buyerOrAuctionHouse = buyerOrAuctionHouse;
        this.totalQuantitySoldKg = totalQuantitySoldKg;
        this.pricePerKg = pricePerKg;
        this.commissionDeduction = commissionDeduction;
        this.saleDate = saleDate;
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
