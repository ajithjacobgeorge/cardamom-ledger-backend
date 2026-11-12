package com.cardamom.ledger.domain.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "sale_transactions", indexes = {
        @Index(name = "idx_sales_batch_id", columnList = "batch_id"),
        @Index(name = "idx_sales_sale_date", columnList = "sale_date"),
        @Index(name = "idx_sales_buyer", columnList = "buyer_or_auction_house")
})
public class SaleTransactionEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "batch_id", nullable = false)
    private Long batchId;

    @Column(name = "buyer_or_auction_house", length = 150, nullable = false)
    private String buyerOrAuctionHouse;

    @Column(name = "total_quantity_sold_kg", precision = 10, scale = 2, nullable = false)
    private BigDecimal totalQuantitySoldKg;

    @Column(name = "price_per_kg", precision = 10, scale = 2, nullable = false)
    private BigDecimal pricePerKg;

    @Column(name = "commission_deduction", precision = 10, scale = 2, nullable = false)
    private BigDecimal commissionDeduction;

    @Column(name = "net_realization_amount", precision = 12, scale = 2, nullable = false)
    private BigDecimal netRealizationAmount;

    @Column(name = "sale_date", nullable = false)
    private LocalDate saleDate;

    public SaleTransactionEntity() {
    }

    public SaleTransactionEntity(Long id, Long batchId, String buyerOrAuctionHouse, BigDecimal totalQuantitySoldKg,
                                 BigDecimal pricePerKg, BigDecimal commissionDeduction,
                                 BigDecimal netRealizationAmount, LocalDate saleDate) {
        this.id = id;
        this.batchId = batchId;
        this.buyerOrAuctionHouse = buyerOrAuctionHouse;
        this.totalQuantitySoldKg = totalQuantitySoldKg;
        this.pricePerKg = pricePerKg;
        this.commissionDeduction = commissionDeduction;
        this.netRealizationAmount = netRealizationAmount;
        this.saleDate = saleDate;
    }

    /**
     * Calculates net realization: (totalQuantitySoldKg * pricePerKg) - commissionDeduction
     */
    public static BigDecimal calculateNetRealization(BigDecimal quantity, BigDecimal price, BigDecimal commission) {
        if (quantity == null || price == null) {
            return BigDecimal.ZERO;
        }
        BigDecimal gross = quantity.multiply(price);
        BigDecimal comm = commission != null ? commission : BigDecimal.ZERO;
        return gross.subtract(comm);
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

    @Override
    public String toString() {
        return "SaleTransactionEntity{" +
                "id=" + id +
                ", batchId=" + batchId +
                ", buyerOrAuctionHouse='" + buyerOrAuctionHouse + '\'' +
                ", totalQuantitySoldKg=" + totalQuantitySoldKg +
                ", pricePerKg=" + pricePerKg +
                ", commissionDeduction=" + commissionDeduction +
                ", netRealizationAmount=" + netRealizationAmount +
                ", saleDate=" + saleDate +
                '}';
    }
}
