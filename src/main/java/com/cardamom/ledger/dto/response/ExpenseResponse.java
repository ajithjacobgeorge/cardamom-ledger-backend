package com.cardamom.ledger.dto.response;

import com.cardamom.ledger.domain.entity.ExpenseEntity;
import com.cardamom.ledger.domain.enums.ExpenseCategory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class ExpenseResponse {

    private Long id;
    private Long blockId;
    private String blockName;
    private ExpenseCategory category;
    private BigDecimal amount;
    private LocalDate date;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ExpenseResponse() {
    }

    public ExpenseResponse(Long id, Long blockId, String blockName, ExpenseCategory category,
                           BigDecimal amount, LocalDate date, String notes,
                           LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.blockId = blockId;
        this.blockName = blockName;
        this.category = category;
        this.amount = amount;
        this.date = date;
        this.notes = notes;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static ExpenseResponse fromEntity(ExpenseEntity entity, String blockName) {
        if (entity == null) return null;
        return new ExpenseResponse(
                entity.getId(),
                entity.getBlockId(),
                blockName,
                entity.getCategory(),
                entity.getAmount(),
                entity.getDate(),
                entity.getNotes(),
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

    public Long getBlockId() {
        return blockId;
    }

    public void setBlockId(Long blockId) {
        this.blockId = blockId;
    }

    public String getBlockName() {
        return blockName;
    }

    public void setBlockName(String blockName) {
        this.blockName = blockName;
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
