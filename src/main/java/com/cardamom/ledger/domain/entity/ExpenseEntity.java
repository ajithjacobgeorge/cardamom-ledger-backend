package com.cardamom.ledger.domain.entity;

import com.cardamom.ledger.domain.enums.ExpenseCategory;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "plantation_expenses", indexes = {
        @Index(name = "idx_expenses_block_id", columnList = "block_id"),
        @Index(name = "idx_expenses_date", columnList = "date"),
        @Index(name = "idx_expenses_category", columnList = "category")
})
public class ExpenseEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "block_id")
    private Long blockId;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false, length = 30)
    private ExpenseCategory category;

    @Column(name = "amount", precision = 12, scale = 2, nullable = false)
    private BigDecimal amount;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "notes", length = 500)
    private String notes;

    public ExpenseEntity() {
    }

    public ExpenseEntity(Long id, Long blockId, ExpenseCategory category, BigDecimal amount, LocalDate date, String notes) {
        this.id = id;
        this.blockId = blockId;
        this.category = category;
        this.amount = amount;
        this.date = date;
        this.notes = notes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBlockId() {
        return blockId;
    }

    public void setBlockId(Long blockId) {
        this.blockId = blockId;
    }

    public ExpenseCategory getCategory() {
        return category;
    }

    public void setCategory(ExpenseCategory category) {
        this.category = category;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "ExpenseEntity{" +
                "id=" + id +
                ", blockId=" + blockId +
                ", category=" + category +
                ", amount=" + amount +
                ", date=" + date +
                ", notes='" + notes + '\'' +
                '}';
    }
}
