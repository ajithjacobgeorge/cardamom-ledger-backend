package com.cardamom.ledger.controller;

import com.cardamom.ledger.dto.request.CreateSaleTransactionRequest;
import com.cardamom.ledger.dto.request.UpdateSaleTransactionRequest;
import com.cardamom.ledger.dto.response.SaleTransactionResponse;
import com.cardamom.ledger.service.SaleTransactionService;
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
@RequestMapping("/api/v1/sale-transactions")
@Tag(name = "Sale Transactions", description = "Endpoints for auction house trade executions, commission accounting, and net realization")
public class SaleTransactionController {

    private final SaleTransactionService saleTransactionService;

    public SaleTransactionController(SaleTransactionService saleTransactionService) {
        this.saleTransactionService = saleTransactionService;
    }

    @PostMapping
    @Operation(summary = "Execute a sale transaction against a cured batch")
    public ResponseEntity<SaleTransactionResponse> createSale(@Valid @RequestBody CreateSaleTransactionRequest request) {
        SaleTransactionResponse created = saleTransactionService.createSale(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a sale transaction by ID")
    public ResponseEntity<SaleTransactionResponse> getSaleById(@PathVariable Long id) {
        return ResponseEntity.ok(saleTransactionService.getSaleById(id));
    }

    @GetMapping
    @Operation(summary = "List all sales transactions with optional filters (batchId, buyer, date range)")
    public ResponseEntity<List<SaleTransactionResponse>> getAllSales(
            @RequestParam(required = false) Long batchId,
            @RequestParam(required = false) String buyer,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(saleTransactionService.getAllSales(batchId, buyer, startDate, endDate));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing sale transaction")
    public ResponseEntity<SaleTransactionResponse> updateSale(
            @PathVariable Long id,
            @Valid @RequestBody UpdateSaleTransactionRequest request) {
        return ResponseEntity.ok(saleTransactionService.updateSale(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete/void a sale transaction")
    public ResponseEntity<Void> deleteSale(@PathVariable Long id) {
        saleTransactionService.deleteSale(id);
        return ResponseEntity.noContent().build();
    }
}
