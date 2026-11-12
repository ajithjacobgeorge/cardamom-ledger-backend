package com.cardamom.ledger.dto.request;

import com.cardamom.ledger.domain.enums.CardamomVariety;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public class UpdateBlockRequest {

    @Size(max = 100, message = "Block name cannot exceed 100 characters")
    private String name;

    private CardamomVariety variety;

    @DecimalMin(value = "0.01", message = "Total area must be greater than zero")
    @Digits(integer = 6, fraction = 2, message = "Total area format is invalid (max 2 decimal places)")
    private BigDecimal totalAreaAcres;

    @Min(value = 1970, message = "Planting year must be 1970 or later")
    @Max(value = 2100, message = "Planting year is invalid")
    private Integer plantingYear;

    public UpdateBlockRequest() {
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
}
