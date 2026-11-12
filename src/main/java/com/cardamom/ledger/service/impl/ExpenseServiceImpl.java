package com.cardamom.ledger.service.impl;

import com.cardamom.ledger.domain.entity.BlockEntity;
import com.cardamom.ledger.domain.entity.ExpenseEntity;
import com.cardamom.ledger.domain.enums.ExpenseCategory;
import com.cardamom.ledger.dto.request.CreateExpenseRequest;
import com.cardamom.ledger.dto.request.UpdateExpenseRequest;
import com.cardamom.ledger.dto.response.ExpenseResponse;
import com.cardamom.ledger.exception.ResourceNotFoundException;
import com.cardamom.ledger.repository.BlockRepository;
import com.cardamom.ledger.repository.ExpenseRepository;
import com.cardamom.ledger.service.ExpenseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class ExpenseServiceImpl implements ExpenseService {

    private static final Logger log = LoggerFactory.getLogger(ExpenseServiceImpl.class);

    private final ExpenseRepository expenseRepository;
    private final BlockRepository blockRepository;

    public ExpenseServiceImpl(ExpenseRepository expenseRepository, BlockRepository blockRepository) {
        this.expenseRepository = expenseRepository;
        this.blockRepository = blockRepository;
    }

    @Override
    public ExpenseResponse createExpense(CreateExpenseRequest request) {
        log.info("Recording new expense category {} amount {}", request.getCategory(), request.getAmount());
        String blockName = null;
        if (request.getBlockId() != null) {
            BlockEntity block = blockRepository.findById(request.getBlockId())
                    .orElseThrow(() -> new ResourceNotFoundException("Block", "id", request.getBlockId()));
            blockName = block.getName();
        }

        ExpenseEntity expense = new ExpenseEntity();
        expense.setBlockId(request.getBlockId());
        expense.setCategory(request.getCategory());
        expense.setAmount(request.getAmount());
        expense.setDate(request.getDate());
        expense.setNotes(request.getNotes());

        ExpenseEntity saved = expenseRepository.save(expense);
        log.info("Saved expense id {} for block {}", saved.getId(), blockName != null ? blockName : "General Estate");
        return ExpenseResponse.fromEntity(saved, blockName);
    }

    @Override
    @Transactional(readOnly = true)
    public ExpenseResponse getExpenseById(Long id) {
        ExpenseEntity expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense", "id", id));

        String blockName = resolveBlockName(expense.getBlockId());
        return ExpenseResponse.fromEntity(expense, blockName);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExpenseResponse> getAllExpenses(Long blockId, ExpenseCategory category, LocalDate startDate, LocalDate endDate) {
        Map<Long, String> blockNameMap = blockRepository.findAll().stream()
                .collect(Collectors.toMap(BlockEntity::getId, BlockEntity::getName));

        List<ExpenseEntity> list = expenseRepository.findAll();

        return list.stream()
                .filter(e -> blockId == null || blockId.equals(e.getBlockId()))
                .filter(e -> category == null || category.equals(e.getCategory()))
                .filter(e -> startDate == null || !e.getDate().isBefore(startDate))
                .filter(e -> endDate == null || !e.getDate().isAfter(endDate))
                .map(e -> ExpenseResponse.fromEntity(e, e.getBlockId() != null ? blockNameMap.get(e.getBlockId()) : null))
                .collect(Collectors.toList());
    }

    @Override
    public ExpenseResponse updateExpense(Long id, UpdateExpenseRequest request) {
        log.info("Updating expense id: {}", id);
        ExpenseEntity expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense", "id", id));

        if (request.getBlockId() != null) {
            if (!blockRepository.existsById(request.getBlockId())) {
                throw new ResourceNotFoundException("Block", "id", request.getBlockId());
            }
            expense.setBlockId(request.getBlockId());
        }

        if (request.getCategory() != null) {
            expense.setCategory(request.getCategory());
        }
        if (request.getAmount() != null) {
            expense.setAmount(request.getAmount());
        }
        if (request.getDate() != null) {
            expense.setDate(request.getDate());
        }
        if (request.getNotes() != null) {
            expense.setNotes(request.getNotes());
        }

        ExpenseEntity updated = expenseRepository.save(expense);
        String blockName = resolveBlockName(updated.getBlockId());
        return ExpenseResponse.fromEntity(updated, blockName);
    }

    @Override
    public void deleteExpense(Long id) {
        log.info("Deleting expense id: {}", id);
        if (!expenseRepository.existsById(id)) {
            throw new ResourceNotFoundException("Expense", "id", id);
        }
        expenseRepository.deleteById(id);
    }

    private String resolveBlockName(Long blockId) {
        if (blockId == null) return null;
        return blockRepository.findById(blockId)
                .map(BlockEntity::getName)
                .orElse(null);
    }
}
