package com.cardamom.ledger.repository;

import com.cardamom.ledger.domain.entity.HarvestBatchEntity;
import com.cardamom.ledger.domain.enums.HarvestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface HarvestBatchRepository extends JpaRepository<HarvestBatchEntity, Long> {

    List<HarvestBatchEntity> findByBlockId(Long blockId);

    List<HarvestBatchEntity> findByStatus(HarvestStatus status);

    List<HarvestBatchEntity> findByHarvestDateBetween(LocalDate startDate, LocalDate endDate);

    List<HarvestBatchEntity> findByBlockIdAndStatus(Long blockId, HarvestStatus status);

    @Query("SELECT COALESCE(SUM(h.greenWeightKg), 0) FROM HarvestBatchEntity h WHERE h.blockId = :blockId")
    BigDecimal sumGreenWeightByBlockId(@Param("blockId") Long blockId);

    @Query("SELECT COALESCE(SUM(h.dryWeightKg), 0) FROM HarvestBatchEntity h WHERE h.blockId = :blockId")
    BigDecimal sumDryWeightByBlockId(@Param("blockId") Long blockId);

    @Query("SELECT COALESCE(SUM(h.greenWeightKg), 0) FROM HarvestBatchEntity h")
    BigDecimal sumTotalGreenWeight();

    @Query("SELECT COALESCE(SUM(h.dryWeightKg), 0) FROM HarvestBatchEntity h")
    BigDecimal sumTotalDryWeight();

    @Query("SELECT COALESCE(SUM(h.bhattiCost), 0) FROM HarvestBatchEntity h")
    BigDecimal sumTotalBhattiCost();
}
