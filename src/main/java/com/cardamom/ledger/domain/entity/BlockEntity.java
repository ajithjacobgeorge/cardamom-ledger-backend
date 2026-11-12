package com.cardamom.ledger.domain.entity;

import com.cardamom.ledger.domain.enums.CardamomVariety;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "plantation_blocks")
public class BlockEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 100, unique = true)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "variety", nullable = false, length = 30)
    private CardamomVariety variety;

    @Column(name = "total_area_acres", precision = 8, scale = 2, nullable = false)
    private BigDecimal totalAreaAcres;

    @Column(name = "planting_year", nullable = false)
    private Integer plantingYear;

    public BlockEntity() {
    }

    public BlockEntity(Long id, String name, CardamomVariety variety, BigDecimal totalAreaAcres, Integer plantingYear) {
        this.id = id;
        this.name = name;
        this.variety = variety;
        this.totalAreaAcres = totalAreaAcres;
        this.plantingYear = plantingYear;
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

    @Override
    public String toString() {
        return "BlockEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", variety=" + variety +
                ", totalAreaAcres=" + totalAreaAcres +
                ", plantingYear=" + plantingYear +
                '}';
    }
}
