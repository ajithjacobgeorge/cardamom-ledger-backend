package com.cardamom.ledger.dto.response;

import com.cardamom.ledger.domain.enums.CardamomVariety;
import java.math.BigDecimal;

public class BlockYieldEfficiency {

    private Long blockId;
    private String blockName;
    private CardamomVariety variety;
    private BigDecimal totalAreaAcres;
    private BigDecimal greenHarvestKg;
    private BigDecimal dryYieldKg;
    private BigDecimal yieldPerAcreKg;
    private BigDecimal curingOutturnPercentage;
    private BigDecimal directExpenses;
    private BigDecimal costPerAcre;

    public BlockYieldEfficiency() {
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

    public BigDecimal getGreenHarvestKg() {
        return greenHarvestKg;
    }

    public void setGreenHarvestKg(BigDecimal greenHarvestKg) {
        this.greenHarvestKg = greenHarvestKg;
    }

    public BigDecimal getDryYieldKg() {
        return dryYieldKg;
    }

    public void setDryYieldKg(BigDecimal dryYieldKg) {
        this.dryYieldKg = dryYieldKg;
    }

    public BigDecimal getYieldPerAcreKg() {
        return yieldPerAcreKg;
    }

    public void setYieldPerAcreKg(BigDecimal yieldPerAcreKg) {
        this.yieldPerAcreKg = yieldPerAcreKg;
    }

    public BigDecimal getCuringOutturnPercentage() {
        return curingOutturnPercentage;
    }

    public void setCuringOutturnPercentage(BigDecimal curingOutturnPercentage) {
        this.curingOutturnPercentage = curingOutturnPercentage;
    }

    public BigDecimal getDirectExpenses() {
        return directExpenses;
    }

    public void setDirectExpenses(BigDecimal directExpenses) {
        this.directExpenses = directExpenses;
    }

    public BigDecimal getCostPerAcre() {
        return costPerAcre;
    }

    public void setCostPerAcre(BigDecimal costPerAcre) {
        this.costPerAcre = costPerAcre;
    }
}
