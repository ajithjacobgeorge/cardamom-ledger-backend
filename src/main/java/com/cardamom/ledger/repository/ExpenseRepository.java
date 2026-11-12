package com.cardamom.ledger.repository;

import com.cardamom.ledger.domain.entity.ExpenseEntity;
import com.cardamom.ledger.domain.enums.ExpenseCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<ExpenseEntity, Long> {

    List<ExpenseEntity> findByBlockId(Long blockId);

    List<ExpenseEntity> findByCategory(ExpenseCategory category);

    List<ExpenseEntity> findByDateBetween(LocalDate startDate, LocalDate endDate);

    List<ExpenseEntity> findByBlockIdAndDateBetween(Long blockId, LocalDate startDate, LocalDate endDate);

    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM ExpenseEntity e WHERE e.blockId = :blockId")
    BigDecimal sumExpensesByBlockId(@Param("blockId") Long blockId);

    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM ExpenseEntity e")
    BigDecimal sumTotalExpenses();

    @Query("SELECT e.category, COALESCE(SUM(e.amount), 0) FROM ExpenseEntity e GROUP BY e.category")
    List<Object[]> sumExpensesByCategory();
}
