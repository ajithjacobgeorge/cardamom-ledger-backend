package com.cardamom.ledger.repository;

import com.cardamom.ledger.domain.entity.SaleTransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface SaleTransactionRepository extends JpaRepository<SaleTransactionEntity, Long> {

    List<SaleTransactionEntity> findByBatchId(Long batchId);

    List<SaleTransactionEntity> findBySaleDateBetween(LocalDate startDate, LocalDate endDate);

    @Query("SELECT s FROM SaleTransactionEntity s WHERE LOWER(s.buyerOrAuctionHouse) LIKE LOWER(CONCAT('%', :buyer, '%'))")
    List<SaleTransactionEntity> searchByBuyerOrAuctionHouse(@Param("buyer") String buyer);

    @Query("SELECT COALESCE(SUM(s.totalQuantitySoldKg), 0) FROM SaleTransactionEntity s WHERE s.batchId = :batchId")
    BigDecimal sumQuantitySoldByBatchId(@Param("batchId") Long batchId);

    @Query("SELECT COALESCE(SUM(s.netRealizationAmount), 0) FROM SaleTransactionEntity s")
    BigDecimal sumTotalNetRealization();

    @Query("SELECT COALESCE(SUM(s.totalQuantitySoldKg), 0) FROM SaleTransactionEntity s")
    BigDecimal sumTotalQuantitySold();

    @Query("SELECT COALESCE(SUM(s.commissionDeduction), 0) FROM SaleTransactionEntity s")
    BigDecimal sumTotalCommissionDeduction();
}
