package com.cardamom.ledger.service;

import com.cardamom.ledger.domain.enums.ExpenseCategory;
import com.cardamom.ledger.dto.request.CreateExpenseRequest;
import com.cardamom.ledger.dto.request.UpdateExpenseRequest;
import com.cardamom.ledger.dto.response.ExpenseResponse;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseService {

    ExpenseResponse createExpense(CreateExpenseRequest request);

    ExpenseResponse getExpenseById(Long id);

    List<ExpenseResponse> getAllExpenses(Long blockId, ExpenseCategory category, LocalDate startDate, LocalDate endDate);

    ExpenseResponse updateExpense(Long id, UpdateExpenseRequest request);

    void deleteExpense(Long id);
}
