package com.cardamom.ledger.dto.request;

import com.cardamom.ledger.domain.enums.ExpenseCategory;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public class UpdateExpenseRequest {

    private Long blockId;

    private ExpenseCategory category;

    @DecimalMin(value = "0.01", message = "Expense amount must be greater than zero")
    @Digits(integer = 10, fraction = 2, message = "Amount format is invalid (max 2 decimal places)")
    private BigDecimal amount;

    @PastOrPresent(message = "Expense date cannot be in the future")
    private LocalDate date;

    @Size(max = 500, message = "Notes cannot exceed 500 characters")
    private String notes;

    public UpdateExpenseRequest() {
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
}
