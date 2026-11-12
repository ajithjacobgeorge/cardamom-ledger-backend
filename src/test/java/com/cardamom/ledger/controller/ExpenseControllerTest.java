package com.cardamom.ledger.controller;

import com.cardamom.ledger.domain.enums.ExpenseCategory;
import com.cardamom.ledger.dto.request.CreateExpenseRequest;
import com.cardamom.ledger.dto.response.ExpenseResponse;
import com.cardamom.ledger.service.ExpenseService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ExpenseController.class)
@AutoConfigureMockMvc(addFilters = false)
class ExpenseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ExpenseService expenseService;

    @Test
    @DisplayName("POST /api/v1/expenses - Success")
    void createExpense_Success() throws Exception {
        CreateExpenseRequest request = new CreateExpenseRequest(
                1L,
                ExpenseCategory.LABOR,
                new BigDecimal("12000.00"),
                LocalDate.of(2026, 8, 10),
                "Plucking labor wages"
        );

        ExpenseResponse response = new ExpenseResponse(
                1L, 1L, "Lower Valley Block A", ExpenseCategory.LABOR,
                new BigDecimal("12000.00"), LocalDate.of(2026, 8, 10),
                "Plucking labor wages", LocalDateTime.now(), LocalDateTime.now()
        );

        when(expenseService.createExpense(any(CreateExpenseRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/expenses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.category").value("LABOR"))
                .andExpect(jsonPath("$.amount").value(12000.00))
                .andExpect(jsonPath("$.blockName").value("Lower Valley Block A"));
    }

    @Test
    @DisplayName("GET /api/v1/expenses - Filter by category")
    void getAllExpenses_FilterByCategory() throws Exception {
        ExpenseResponse r1 = new ExpenseResponse(
                1L, 1L, "Block A", ExpenseCategory.FERTILIZER,
                new BigDecimal("5000.00"), LocalDate.now(), null, null, null
        );
        when(expenseService.getAllExpenses(null, ExpenseCategory.FERTILIZER, null, null))
                .thenReturn(List.of(r1));

        mockMvc.perform(get("/api/v1/expenses?category=FERTILIZER"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].category").value("FERTILIZER"));
    }
}
