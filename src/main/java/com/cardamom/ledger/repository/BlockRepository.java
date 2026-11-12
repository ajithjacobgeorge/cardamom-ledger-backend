package com.cardamom.ledger.repository;

import com.cardamom.ledger.domain.entity.BlockEntity;
import com.cardamom.ledger.domain.enums.CardamomVariety;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface BlockRepository extends JpaRepository<BlockEntity, Long> {

    Optional<BlockEntity> findByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCase(String name);

    List<BlockEntity> findByVariety(CardamomVariety variety);

    @Query("SELECT COALESCE(SUM(b.totalAreaAcres), 0) FROM BlockEntity b")
    BigDecimal getTotalPlantedAreaAcres();
}
