package com.cardamom.ledger.controller;

import com.cardamom.ledger.domain.enums.HarvestStatus;
import com.cardamom.ledger.dto.request.CreateHarvestBatchRequest;
import com.cardamom.ledger.dto.request.UpdateHarvestBatchRequest;
import com.cardamom.ledger.dto.response.HarvestBatchResponse;
import com.cardamom.ledger.service.HarvestBatchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/harvest-batches")
@Tag(name = "Harvest Batches", description = "Endpoints for managing green capsule yields, bhatti curing costs, and cured inventory")
public class HarvestBatchController {

    private final HarvestBatchService harvestBatchService;

    public HarvestBatchController(HarvestBatchService harvestBatchService) {
        this.harvestBatchService = harvestBatchService;
    }

    @PostMapping
    @Operation(summary = "Record a new harvest batch (green weight, bhatti cost, grade breakdown)")
    public ResponseEntity<HarvestBatchResponse> createBatch(@Valid @RequestBody CreateHarvestBatchRequest request) {
        HarvestBatchResponse created = harvestBatchService.createBatch(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a harvest batch by ID (includes outturn % and remaining unsold stock)")
    public ResponseEntity<HarvestBatchResponse> getBatchById(@PathVariable Long id) {
        return ResponseEntity.ok(harvestBatchService.getBatchById(id));
    }

    @GetMapping
    @Operation(summary = "List all harvest batches with optional filters (blockId, status, date range)")
    public ResponseEntity<List<HarvestBatchResponse>> getAllBatches(
            @RequestParam(required = false) Long blockId,
            @RequestParam(required = false) HarvestStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(harvestBatchService.getAllBatches(blockId, status, startDate, endDate));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a harvest batch (record cured dry weight, update status or bhatti cost)")
    public ResponseEntity<HarvestBatchResponse> updateBatch(
            @PathVariable Long id,
            @Valid @RequestBody UpdateHarvestBatchRequest request) {
        return ResponseEntity.ok(harvestBatchService.updateBatch(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an unsold harvest batch")
    public ResponseEntity<Void> deleteBatch(@PathVariable Long id) {
        harvestBatchService.deleteBatch(id);
        return ResponseEntity.noContent().build();
    }
}
