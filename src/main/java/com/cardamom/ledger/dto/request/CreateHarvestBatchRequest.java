package com.cardamom.ledger.dto.request;

import com.cardamom.ledger.domain.enums.HarvestStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import java.math.BigDecimal;
import java.time.LocalDate;

public class CreateHarvestBatchRequest {

    @NotNull(message = "Block ID is required")
    private Long blockId;

    @NotNull(message = "Harvest date is required")
    @PastOrPresent(message = "Harvest date cannot be in the future")
    private LocalDate harvestDate;

    @NotNull(message = "Green weight (kg) is required")
    @DecimalMin(value = "0.01", message = "Green weight must be greater than zero")
    @Digits(integer = 8, fraction = 2, message = "Green weight format is invalid (max 2 decimal places)")
    private BigDecimal greenWeightKg;

    @DecimalMin(value = "0.00", message = "Dry weight must be zero or positive")
    @Digits(integer = 8, fraction = 2, message = "Dry weight format is invalid (max 2 decimal places)")
    private BigDecimal dryWeightKg;

    @DecimalMin(value = "0.00", message = "Bhatti cost must be zero or positive")
    @Digits(integer = 8, fraction = 2, message = "Bhatti cost format is invalid (max 2 decimal places)")
    private BigDecimal bhattiCost;

    private String gradeBreakdownJson;

    private HarvestStatus status = HarvestStatus.RAW;

    public CreateHarvestBatchRequest() {
    }

    public CreateHarvestBatchRequest(Long blockId, LocalDate harvestDate, BigDecimal greenWeightKg,
                                     BigDecimal dryWeightKg, BigDecimal bhattiCost,
                                     String gradeBreakdownJson, HarvestStatus status) {
        this.blockId = blockId;
        this.harvestDate = harvestDate;
        this.greenWeightKg = greenWeightKg;
        this.dryWeightKg = dryWeightKg;
        this.bhattiCost = bhattiCost;
        this.gradeBreakdownJson = gradeBreakdownJson;
        this.status = status != null ? status : HarvestStatus.RAW;
    }

    public Long getBlockId() {
        return blockId;
    }

    public void setBlockId(Long blockId) {
        this.blockId = blockId;
    }

    public LocalDate getHarvestDate() {
        return harvestDate;
    }

    public void setHarvestDate(LocalDate harvestDate) {
        this.harvestDate = harvestDate;
    }

    public BigDecimal getGreenWeightKg() {
        return greenWeightKg;
    }

    public void setGreenWeightKg(BigDecimal greenWeightKg) {
        this.greenWeightKg = greenWeightKg;
    }

    public BigDecimal getDryWeightKg() {
        return dryWeightKg;
    }

    public void setDryWeightKg(BigDecimal dryWeightKg) {
        this.dryWeightKg = dryWeightKg;
    }

    public BigDecimal getBhattiCost() {
        return bhattiCost;
    }

    public void setBhattiCost(BigDecimal bhattiCost) {
        this.bhattiCost = bhattiCost;
    }

    public String getGradeBreakdownJson() {
        return gradeBreakdownJson;
    }

    public void setGradeBreakdownJson(String gradeBreakdownJson) {
        this.gradeBreakdownJson = gradeBreakdownJson;
    }

    public HarvestStatus getStatus() {
        return status;
    }

    public void setStatus(HarvestStatus status) {
        this.status = status;
    }
}
