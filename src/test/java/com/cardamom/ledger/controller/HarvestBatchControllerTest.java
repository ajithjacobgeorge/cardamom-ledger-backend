package com.cardamom.ledger.controller;

import com.cardamom.ledger.domain.enums.HarvestStatus;
import com.cardamom.ledger.dto.request.CreateHarvestBatchRequest;
import com.cardamom.ledger.dto.response.HarvestBatchResponse;
import com.cardamom.ledger.service.HarvestBatchService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HarvestBatchController.class)
@AutoConfigureMockMvc(addFilters = false)
class HarvestBatchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private HarvestBatchService harvestBatchService;

    @Test
    @DisplayName("POST /api/v1/harvest-batches - Success")
    void createBatch_Success() throws Exception {
        CreateHarvestBatchRequest request = new CreateHarvestBatchRequest(
                1L,
                LocalDate.of(2026, 8, 15),
                new BigDecimal("500.00"),
                new BigDecimal("105.00"),
                new BigDecimal("2800.00"),
                "{\"AGEB\": 40.0}",
                HarvestStatus.STORED
        );

        HarvestBatchResponse response = new HarvestBatchResponse(
                1L, 1L, "Block A", LocalDate.of(2026, 8, 15),
                new BigDecimal("500.00"), new BigDecimal("105.00"), new BigDecimal("2800.00"),
                "{\"AGEB\": 40.0}", HarvestStatus.STORED,
                new BigDecimal("21.00"), new BigDecimal("105.00"),
                LocalDateTime.now(), LocalDateTime.now()
        );

        when(harvestBatchService.createBatch(any(CreateHarvestBatchRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/harvest-batches")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.greenWeightKg").value(500.00))
                .andExpect(jsonPath("$.dryWeightKg").value(105.00))
                .andExpect(jsonPath("$.outturnPercentage").value(21.00))
                .andExpect(jsonPath("$.bhattiCost").value(2800.00));
    }
}
