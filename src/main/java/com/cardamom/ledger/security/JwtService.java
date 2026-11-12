package com.cardamom.ledger.security;

import com.cardamom.ledger.config.AuthProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Stateless HS256 JWT service using standard JDK cryptography (no external JWT dependencies).
 */
@Service
public class JwtService {

    private static final Logger log = LoggerFactory.getLogger(JwtService.class);
    private static final String HMAC_ALGORITHM = "HmacSHA256";
    private static final String HEADER_JSON = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";
    private static final Base64.Encoder URL_ENCODER = Base64.getUrlEncoder().withoutPadding();
    private static final Base64.Decoder URL_DECODER = Base64.getUrlDecoder();

    private final byte[] signingKey;
    private final long expirationHours;
    private final ObjectMapper objectMapper;

    public JwtService(AuthProperties authProperties, ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.expirationHours = Math.max(1L, authProperties.getTokenExpirationHours());
        this.signingKey = resolveSigningKey(authProperties.getJwtSecret());
    }

    public IssuedToken issueToken(AuthUserPrincipal user) {
        Instant issuedAt = Instant.now();
        Instant expiresAt = issuedAt.plus(expirationHours, ChronoUnit.HOURS);

        Map<String, Object> claims = new LinkedHashMap<>();
        claims.put("sub", user.email());
        claims.put("name", user.name());
        claims.put("role", user.role());
        claims.put("estate", user.estate());
        claims.put("iat", issuedAt.getEpochSecond());
        claims.put("exp", expiresAt.getEpochSecond());

        try {
            String headerB64 = URL_ENCODER.encodeToString(HEADER_JSON.getBytes(StandardCharsets.UTF_8));
            String payloadB64 = URL_ENCODER.encodeToString(objectMapper.writeValueAsBytes(claims));
            String signingInput = headerB64 + "." + payloadB64;
            String signatureB64 = URL_ENCODER.encodeToString(sign(signingInput));
            return new IssuedToken(signingInput + "." + signatureB64, expiresAt);
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to issue JWT token", ex);
        }
    }

    public Optional<AuthUserPrincipal> verifyToken(String token) {
        if (token == null || token.isBlank()) {
            return Optional.empty();
        }
        String[] parts = token.split("\\.");
        if (parts.length != 3) {
            return Optional.empty();
        }

        try {
            String signingInput = parts[0] + "." + parts[1];
            byte[] expectedSig = sign(signingInput);
            byte[] providedSig = URL_DECODER.decode(parts[2]);
            if (!MessageDigest.isEqual(expectedSig, providedSig)) {
                return Optional.empty();
            }

            byte[] payloadBytes = URL_DECODER.decode(parts[1]);
            Map<String, Object> claims = objectMapper.readValue(payloadBytes, new TypeReference<>() {});
            Object expObj = claims.get("exp");
            if (!(expObj instanceof Number expNum)) {
                return Optional.empty();
            }
            if (Instant.now().getEpochSecond() >= expNum.longValue()) {
                return Optional.empty();
            }

            String email = stringClaim(claims, "sub");
            if (email == null || email.isBlank()) {
                return Optional.empty();
            }
            String name = stringClaim(claims, "name");
            String role = stringClaim(claims, "role");
            String estate = stringClaim(claims, "estate");
            return Optional.of(new AuthUserPrincipal(email, name, role, estate));
        } catch (Exception ex) {
            log.debug("Rejected invalid JWT token: {}", ex.getMessage());
            return Optional.empty();
        }
    }

    private byte[] sign(String signingInput) throws Exception {
        Mac mac = Mac.getInstance(HMAC_ALGORITHM);
        mac.init(new SecretKeySpec(signingKey, HMAC_ALGORITHM));
        return mac.doFinal(signingInput.getBytes(StandardCharsets.UTF_8));
    }

    private static String stringClaim(Map<String, Object> claims, String key) {
        Object value = claims.get(key);
        return value != null ? value.toString() : "";
    }

    private static byte[] resolveSigningKey(String configuredSecret) {
        if (configuredSecret != null && !configuredSecret.isBlank()) {
            try {
                // Hash configured secret with SHA-256 so any length passphrase yields a full 256-bit HMAC key
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                return digest.digest(configuredSecret.getBytes(StandardCharsets.UTF_8));
            } catch (Exception ex) {
                throw new IllegalStateException("Failed to initialize JWT signing key", ex);
            }
        }
        log.warn("APP_AUTH_JWT_SECRET is not set; generated an ephemeral signing key (tokens will reset on restart).");
        byte[] random = new byte[32];
        new SecureRandom().nextBytes(random);
        return random;
    }

    public record IssuedToken(String token, Instant expiresAt) {}

    public record AuthUserPrincipal(String email, String name, String role, String estate) {}
}

