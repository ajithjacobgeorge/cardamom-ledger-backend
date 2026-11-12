package com.cardamom.ledger.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

/**
 * Lightweight liveness endpoint for Render health checks and mobile status probes.
 * Intentionally avoids database queries so serverless Postgres instances can scale to zero.
 */
@RestController
@Tag(name = "Health", description = "Service liveness check endpoint")
public class HealthController {

    @GetMapping({"/api/health", "/api/v1/health", "/actuator/health"})
    @Operation(summary = "Check backend service liveness")
    public ResponseEntity<Map<String, Object>> health() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "service", "cardamom-ledger-backend",
                "timestamp", Instant.now().toString()
        ));
    }
}

