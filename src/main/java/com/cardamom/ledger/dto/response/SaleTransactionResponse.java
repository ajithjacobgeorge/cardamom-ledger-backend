package com.cardamom.ledger.dto.response;

import com.cardamom.ledger.domain.entity.SaleTransactionEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class SaleTransactionResponse {

    private Long id;
    private Long batchId;
    private String buyerOrAuctionHouse;
    private BigDecimal totalQuantitySoldKg;
    private BigDecimal pricePerKg;
    private BigDecimal grossAmount;
    private BigDecimal commissionDeduction;
    private BigDecimal netRealizationAmount;
    private LocalDate saleDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public SaleTransactionResponse() {
    }

    public SaleTransactionResponse(Long id, Long batchId, String buyerOrAuctionHouse,
                                   BigDecimal totalQuantitySoldKg, BigDecimal pricePerKg,
                                   BigDecimal grossAmount, BigDecimal commissionDeduction,
                                   BigDecimal netRealizationAmount, LocalDate saleDate,
                                   LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.batchId = batchId;
        this.buyerOrAuctionHouse = buyerOrAuctionHouse;
        this.totalQuantitySoldKg = totalQuantitySoldKg;
        this.pricePerKg = pricePerKg;
        this.grossAmount = grossAmount;
        this.commissionDeduction = commissionDeduction;
        this.netRealizationAmount = netRealizationAmount;
        this.saleDate = saleDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static SaleTransactionResponse fromEntity(SaleTransactionEntity entity) {
        if (entity == null) return null;
        BigDecimal gross = (entity.getTotalQuantitySoldKg() != null && entity.getPricePerKg() != null)
                ? entity.getTotalQuantitySoldKg().multiply(entity.getPricePerKg())
                : BigDecimal.ZERO;

        return new SaleTransactionResponse(
                entity.getId(),
                entity.getBatchId(),
                entity.getBuyerOrAuctionHouse(),
                entity.getTotalQuantitySoldKg(),
                entity.getPricePerKg(),
                gross,
                entity.getCommissionDeduction(),
                entity.getNetRealizationAmount(),
                entity.getSaleDate(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public BigDecimal getGrossAmount() {
        return grossAmount;
    }

    public void setGrossAmount(BigDecimal grossAmount) {
        this.grossAmount = grossAmount;
    }

    public BigDecimal getCommissionDeduction() {
        return commissionDeduction;
    }

    public void setCommissionDeduction(BigDecimal commissionDeduction) {
        this.commissionDeduction = commissionDeduction;
    }

    public BigDecimal getNetRealizationAmount() {
        return netRealizationAmount;
    }

    public void setNetRealizationAmount(BigDecimal netRealizationAmount) {
        this.netRealizationAmount = netRealizationAmount;
    }

    public LocalDate getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(LocalDate saleDate) {
        this.saleDate = saleDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
