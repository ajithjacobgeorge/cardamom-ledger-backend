package com.cardamom.ledger.dto.response;

import com.cardamom.ledger.domain.entity.BlockEntity;
import com.cardamom.ledger.domain.enums.CardamomVariety;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BlockResponse {

    private Long id;
    private String name;
    private CardamomVariety variety;
    private BigDecimal totalAreaAcres;
    private Integer plantingYear;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public BlockResponse() {
    }

    public BlockResponse(Long id, String name, CardamomVariety variety, BigDecimal totalAreaAcres,
                         Integer plantingYear, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.variety = variety;
        this.totalAreaAcres = totalAreaAcres;
        this.plantingYear = plantingYear;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static BlockResponse fromEntity(BlockEntity entity) {
        if (entity == null) return null;
        return new BlockResponse(
                entity.getId(),
                entity.getName(),
                entity.getVariety(),
                entity.getTotalAreaAcres(),
                entity.getPlantingYear(),
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CardamomVariety getVariety() {
        return variety;
    }

    public void setVariety(CardamomVariety variety) {
        this.variety = variety;
    }

    public BigDecimal getTotalAreaAcres() {
        return totalAreaAcres;
    }

    public void setTotalAreaAcres(BigDecimal totalAreaAcres) {
        this.totalAreaAcres = totalAreaAcres;
    }

    public Integer getPlantingYear() {
        return plantingYear;
    }

    public void setPlantingYear(Integer plantingYear) {
        this.plantingYear = plantingYear;
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
