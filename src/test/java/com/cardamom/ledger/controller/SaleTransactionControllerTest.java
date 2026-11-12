package com.cardamom.ledger.controller;

import com.cardamom.ledger.dto.request.CreateSaleTransactionRequest;
import com.cardamom.ledger.dto.response.SaleTransactionResponse;
import com.cardamom.ledger.service.SaleTransactionService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SaleTransactionController.class)
@AutoConfigureMockMvc(addFilters = false)
class SaleTransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SaleTransactionService saleTransactionService;

    @Test
    @DisplayName("POST /api/v1/sale-transactions - Success")
    void createSale_Success() throws Exception {
        CreateSaleTransactionRequest request = new CreateSaleTransactionRequest(
                1L,
                "Spices Board Auction House",
                new BigDecimal("50.00"),
                new BigDecimal("2300.00"),
                new BigDecimal("1150.00"),
                LocalDate.of(2026, 8, 30)
        );

        SaleTransactionResponse response = new SaleTransactionResponse(
                1L, 1L, "Spices Board Auction House",
                new BigDecimal("50.00"), new BigDecimal("2300.00"),
                new BigDecimal("115000.00"), new BigDecimal("1150.00"),
                new BigDecimal("113850.00"), LocalDate.of(2026, 8, 30),
                LocalDateTime.now(), LocalDateTime.now()
        );

        when(saleTransactionService.createSale(any(CreateSaleTransactionRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/sale-transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.buyerOrAuctionHouse").value("Spices Board Auction House"))
                .andExpect(jsonPath("$.grossAmount").value(115000.00))
                .andExpect(jsonPath("$.netRealizationAmount").value(113850.00));
    }
}
