package com.cardamom.ledger.domain.entity;

import com.cardamom.ledger.domain.enums.HarvestStatus;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Entity
@Table(name = "harvest_batches", indexes = {
        @Index(name = "idx_batches_block_id", columnList = "block_id"),
        @Index(name = "idx_batches_harvest_date", columnList = "harvest_date"),
        @Index(name = "idx_batches_status", columnList = "status")
})
public class HarvestBatchEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "block_id", nullable = false)
    private Long blockId;

    @Column(name = "harvest_date", nullable = false)
    private LocalDate harvestDate;

    @Column(name = "green_weight_kg", precision = 10, scale = 2, nullable = false)
    private BigDecimal greenWeightKg;

    @Column(name = "dry_weight_kg", precision = 10, scale = 2)
    private BigDecimal dryWeightKg;

    @Column(name = "bhatti_cost", precision = 10, scale = 2)
    private BigDecimal bhattiCost;

    @Column(name = "grade_breakdown_json", columnDefinition = "TEXT")
    private String gradeBreakdownJson;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private HarvestStatus status = HarvestStatus.RAW;

    public HarvestBatchEntity() {
    }

    public HarvestBatchEntity(Long id, Long blockId, LocalDate harvestDate, BigDecimal greenWeightKg,
                              BigDecimal dryWeightKg, BigDecimal bhattiCost, String gradeBreakdownJson,
                              HarvestStatus status) {
        this.id = id;
        this.blockId = blockId;
        this.harvestDate = harvestDate;
        this.greenWeightKg = greenWeightKg;
        this.dryWeightKg = dryWeightKg;
        this.bhattiCost = bhattiCost;
        this.gradeBreakdownJson = gradeBreakdownJson;
        this.status = status != null ? status : HarvestStatus.RAW;
    }

    /**
     * Calculates outturn ratio: (dry weight / green weight) * 100.
     * In cardamom plantations, standard cured recovery is typically 18% - 24%.
     */
    public BigDecimal calculateOutturnPercentage() {
        if (greenWeightKg == null || dryWeightKg == null || greenWeightKg.compareTo(BigDecimal.ZERO) <= 0) {
            return null;
        }
        return dryWeightKg.multiply(BigDecimal.valueOf(100))
                .divide(greenWeightKg, 2, RoundingMode.HALF_UP);
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

    @Override
    public String toString() {
        return "HarvestBatchEntity{" +
                "id=" + id +
                ", blockId=" + blockId +
                ", harvestDate=" + harvestDate +
                ", greenWeightKg=" + greenWeightKg +
                ", dryWeightKg=" + dryWeightKg +
                ", bhattiCost=" + bhattiCost +
                ", status=" + status +
                '}';
    }
}
