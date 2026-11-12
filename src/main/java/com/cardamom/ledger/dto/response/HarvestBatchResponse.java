package com.cardamom.ledger.dto.response;

import com.cardamom.ledger.domain.entity.HarvestBatchEntity;
import com.cardamom.ledger.domain.enums.HarvestStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class HarvestBatchResponse {

    private Long id;
    private Long blockId;
    private String blockName;
    private LocalDate harvestDate;
    private BigDecimal greenWeightKg;
    private BigDecimal dryWeightKg;
    private BigDecimal bhattiCost;
    private String gradeBreakdownJson;
    private HarvestStatus status;
    private BigDecimal outturnPercentage;
    private BigDecimal remainingUnsoldQuantityKg;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public HarvestBatchResponse() {
    }

    public HarvestBatchResponse(Long id, Long blockId, String blockName, LocalDate harvestDate,
                                BigDecimal greenWeightKg, BigDecimal dryWeightKg, BigDecimal bhattiCost,
                                String gradeBreakdownJson, HarvestStatus status,
                                BigDecimal outturnPercentage, BigDecimal remainingUnsoldQuantityKg,
                                LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.blockId = blockId;
        this.blockName = blockName;
        this.harvestDate = harvestDate;
        this.greenWeightKg = greenWeightKg;
        this.dryWeightKg = dryWeightKg;
        this.bhattiCost = bhattiCost;
        this.gradeBreakdownJson = gradeBreakdownJson;
        this.status = status;
        this.outturnPercentage = outturnPercentage;
        this.remainingUnsoldQuantityKg = remainingUnsoldQuantityKg;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static HarvestBatchResponse fromEntity(HarvestBatchEntity entity, String blockName, BigDecimal remainingUnsoldQuantity) {
        if (entity == null) return null;
        return new HarvestBatchResponse(
                entity.getId(),
                entity.getBlockId(),
                blockName,
                entity.getHarvestDate(),
                entity.getGreenWeightKg(),
                entity.getDryWeightKg(),
                entity.getBhattiCost(),
                entity.getGradeBreakdownJson(),
                entity.getStatus(),
                entity.calculateOutturnPercentage(),
                remainingUnsoldQuantity,
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

    public BigDecimal getOutturnPercentage() {
        return outturnPercentage;
    }

    public void setOutturnPercentage(BigDecimal outturnPercentage) {
        this.outturnPercentage = outturnPercentage;
    }

    public BigDecimal getRemainingUnsoldQuantityKg() {
        return remainingUnsoldQuantityKg;
    }

    public void setRemainingUnsoldQuantityKg(BigDecimal remainingUnsoldQuantityKg) {
        this.remainingUnsoldQuantityKg = remainingUnsoldQuantityKg;
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
