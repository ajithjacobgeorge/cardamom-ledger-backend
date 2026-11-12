package com.cardamom.ledger.controller;

import com.cardamom.ledger.config.AuthProperties;
import com.cardamom.ledger.exception.ApiErrorResponse;
import com.cardamom.ledger.security.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "Estate login and JWT token issuance for mobile and web clients")
public class AuthController {

    private final AuthProperties authProperties;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthProperties authProperties,
                          JwtService jwtService,
                          PasswordEncoder passwordEncoder) {
        this.authProperties = authProperties;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate estate credentials and obtain a Bearer JWT token")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request, HttpServletRequest httpRequest) {
        String submittedIdentity = request.email() != null && !request.email().isBlank()
                ? request.email().trim()
                : (request.username() != null ? request.username().trim() : "");

        boolean usernameMatches = submittedIdentity.equalsIgnoreCase(authProperties.getUsername().trim());
        boolean passwordMatches = verifyPassword(request.password(), authProperties.getPassword());

        if (!usernameMatches || !passwordMatches) {
            ApiErrorResponse error = new ApiErrorResponse(
                    HttpStatus.UNAUTHORIZED.value(),
                    HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                    "Invalid email or password",
                    httpRequest.getRequestURI()
            );
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }

        JwtService.AuthUserPrincipal user = new JwtService.AuthUserPrincipal(
                authProperties.getUsername(),
                authProperties.getName(),
                authProperties.getRole(),
                authProperties.getEstate()
        );
        JwtService.IssuedToken issued = jwtService.issueToken(user);

        return ResponseEntity.ok(new AuthResponse(
                issued.token(),
                "Bearer",
                issued.expiresAt().toString(),
                new AuthUserDto(user.name(), user.email(), user.role(), user.estate())
        ));
    }

    @GetMapping("/me")
    @Operation(summary = "Get currently authenticated estate user profile")
    public ResponseEntity<AuthUserDto> me(Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof JwtService.AuthUserPrincipal principal) {
            return ResponseEntity.ok(new AuthUserDto(
                    principal.name(),
                    principal.email(),
                    principal.role(),
                    principal.estate()
            ));
        }
        return ResponseEntity.ok(new AuthUserDto(
                authProperties.getName(),
                authProperties.getUsername(),
                authProperties.getRole(),
                authProperties.getEstate()
        ));
    }

    private boolean verifyPassword(String rawPassword, String configuredPassword) {
        if (rawPassword == null || configuredPassword == null || configuredPassword.isBlank()) {
            return false;
        }
        if (configuredPassword.startsWith("$2a$") || configuredPassword.startsWith("$2b$") || configuredPassword.startsWith("$2y$")) {
            return passwordEncoder.matches(rawPassword, configuredPassword);
        }
        return MessageDigest.isEqual(
                rawPassword.getBytes(StandardCharsets.UTF_8),
                configuredPassword.getBytes(StandardCharsets.UTF_8)
        );
    }

    public record LoginRequest(
            String email,
            String username,
            @NotBlank(message = "Password is required") String password
    ) {}

    public record AuthUserDto(String name, String email, String role, String estate) {}

    public record AuthResponse(String token, String tokenType, String expiresAt, AuthUserDto user) {}
}
