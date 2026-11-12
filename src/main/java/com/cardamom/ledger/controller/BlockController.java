package com.cardamom.ledger.controller;

import com.cardamom.ledger.domain.enums.CardamomVariety;
import com.cardamom.ledger.dto.request.CreateBlockRequest;
import com.cardamom.ledger.dto.request.UpdateBlockRequest;
import com.cardamom.ledger.dto.response.BlockResponse;
import com.cardamom.ledger.service.BlockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/blocks")
@Tag(name = "Plantation Blocks", description = "Endpoints for managing plantation parcels, acreages, and cardamom varieties")
public class BlockController {

    private final BlockService blockService;

    public BlockController(BlockService blockService) {
        this.blockService = blockService;
    }

    @PostMapping
    @Operation(summary = "Register a new plantation block")
    public ResponseEntity<BlockResponse> createBlock(@Valid @RequestBody CreateBlockRequest request) {
        BlockResponse created = blockService.createBlock(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a plantation block by ID")
    public ResponseEntity<BlockResponse> getBlockById(@PathVariable Long id) {
        return ResponseEntity.ok(blockService.getBlockById(id));
    }

    @GetMapping
    @Operation(summary = "List all plantation blocks (optional filter by variety)")
    public ResponseEntity<List<BlockResponse>> getAllBlocks(
            @RequestParam(required = false) CardamomVariety variety) {
        return ResponseEntity.ok(blockService.getAllBlocks(variety));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing plantation block")
    public ResponseEntity<BlockResponse> updateBlock(
            @PathVariable Long id,
            @Valid @RequestBody UpdateBlockRequest request) {
        return ResponseEntity.ok(blockService.updateBlock(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an unused plantation block")
    public ResponseEntity<Void> deleteBlock(@PathVariable Long id) {
        blockService.deleteBlock(id);
        return ResponseEntity.noContent().build();
    }
}
