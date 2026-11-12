package com.cardamom.ledger.controller;

import com.cardamom.ledger.domain.enums.CardamomVariety;
import com.cardamom.ledger.dto.request.CreateBlockRequest;
import com.cardamom.ledger.dto.request.UpdateBlockRequest;
import com.cardamom.ledger.dto.response.BlockResponse;
import com.cardamom.ledger.exception.DuplicateResourceException;
import com.cardamom.ledger.exception.ResourceNotFoundException;
import com.cardamom.ledger.service.BlockService;
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
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BlockController.class)
@AutoConfigureMockMvc(addFilters = false)
class BlockControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BlockService blockService;

    @Test
    @DisplayName("POST /api/v1/blocks - Success")
    void createBlock_Success() throws Exception {
        CreateBlockRequest request = new CreateBlockRequest(
                "Munnar Foothills Block",
                CardamomVariety.VAZHUKKA,
                new BigDecimal("10.50"),
                2020
        );

        BlockResponse response = new BlockResponse(
                1L, "Munnar Foothills Block", CardamomVariety.VAZHUKKA,
                new BigDecimal("10.50"), 2020, LocalDateTime.now(), LocalDateTime.now()
        );

        when(blockService.createBlock(any(CreateBlockRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/blocks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Munnar Foothills Block"))
                .andExpect(jsonPath("$.variety").value("VAZHUKKA"))
                .andExpect(jsonPath("$.totalAreaAcres").value(10.50));
    }

    @Test
    @DisplayName("POST /api/v1/blocks - Validation Failure (Missing name)")
    void createBlock_ValidationFailure() throws Exception {
        CreateBlockRequest request = new CreateBlockRequest(
                "",
                CardamomVariety.VAZHUKKA,
                new BigDecimal("10.50"),
                2020
        );

        mockMvc.perform(post("/api/v1/blocks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation Error"))
                .andExpect(jsonPath("$.validationErrors.name").exists());
    }

    @Test
    @DisplayName("GET /api/v1/blocks/{id} - Success")
    void getBlockById_Success() throws Exception {
        BlockResponse response = new BlockResponse(
                1L, "Munnar Foothills Block", CardamomVariety.VAZHUKKA,
                new BigDecimal("10.50"), 2020, LocalDateTime.now(), LocalDateTime.now()
        );

        when(blockService.getBlockById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/blocks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Munnar Foothills Block"));
    }

    @Test
    @DisplayName("GET /api/v1/blocks/{id} - Not Found")
    void getBlockById_NotFound() throws Exception {
        when(blockService.getBlockById(999L))
                .thenThrow(new ResourceNotFoundException("Block", "id", 999L));

        mockMvc.perform(get("/api/v1/blocks/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Block not found with id: '999'"));
    }

    @Test
    @DisplayName("GET /api/v1/blocks - List all")
    void getAllBlocks_Success() throws Exception {
        BlockResponse r1 = new BlockResponse(1L, "Block A", CardamomVariety.MALABAR, new BigDecimal("5.0"), 2018, null, null);
        when(blockService.getAllBlocks(null)).thenReturn(List.of(r1));

        mockMvc.perform(get("/api/v1/blocks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Block A"));
    }

    @Test
    @DisplayName("DELETE /api/v1/blocks/{id} - Success")
    void deleteBlock_Success() throws Exception {
        doNothing().when(blockService).deleteBlock(1L);

        mockMvc.perform(delete("/api/v1/blocks/1"))
                .andExpect(status().isNoContent());
    }
}
