package com.cardamom.ledger.controller;

import com.cardamom.ledger.dto.response.EstateSummaryResponse;
import com.cardamom.ledger.service.EstateAnalyticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/api/analytics", "/api/v1/analytics"})
@Tag(name = "Estate Analytics & KPIs", description = "Endpoints for overall plantation financial realization, curing recovery rates, and net operational profit")
public class AnalyticsController {

    private final EstateAnalyticsService analyticsService;

    public AnalyticsController(EstateAnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/summary")
    @Operation(summary = "Get consolidated plantation executive ledger summary")
    public ResponseEntity<EstateSummaryResponse> getEstateSummary() {
        return ResponseEntity.ok(analyticsService.getEstateSummary());
    }
}
