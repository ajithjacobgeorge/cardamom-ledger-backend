package com.cardamom.ledger.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Converts standard cloud DATABASE_URL values (postgres:// or postgresql:// from Render/Neon)
 * into Spring JDBC datasource properties when SPRING_DATASOURCE_URL is not explicitly provided.
 */
public class DatabaseUrlEnvironmentPostProcessor implements EnvironmentPostProcessor, Ordered {

    private static final String PROPERTY_SOURCE_NAME = "cloudDatabaseUrl";

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        String explicitJdbcUrl = environment.getProperty("SPRING_DATASOURCE_URL");
        if (explicitJdbcUrl != null && !explicitJdbcUrl.isBlank()) {
            return;
        }

        String rawDatabaseUrl = environment.getProperty("DATABASE_URL");
        if (rawDatabaseUrl == null || rawDatabaseUrl.isBlank()) {
            return;
        }

        String trimmed = rawDatabaseUrl.trim();
        Map<String, Object> datasourceProps = new HashMap<>();

        if (trimmed.startsWith("jdbc:")) {
            datasourceProps.put("spring.datasource.url", trimmed);
            environment.getPropertySources().addFirst(new MapPropertySource(PROPERTY_SOURCE_NAME, datasourceProps));
            return;
        }

        if (!trimmed.startsWith("postgres://") && !trimmed.startsWith("postgresql://")) {
            return;
        }

        URI uri = URI.create(trimmed);
        String host = uri.getHost();
        String path = uri.getRawPath();
        if (host == null || path == null || path.isBlank()) {
            throw new IllegalStateException("Invalid DATABASE_URL format: missing host or database name");
        }

        StringBuilder jdbcUrl = new StringBuilder("jdbc:postgresql://").append(host);
        if (uri.getPort() != -1) {
            jdbcUrl.append(':').append(uri.getPort());
        }
        jdbcUrl.append(path);
        if (uri.getRawQuery() != null && !uri.getRawQuery().isBlank()) {
            jdbcUrl.append('?').append(uri.getRawQuery());
        }
        datasourceProps.put("spring.datasource.url", jdbcUrl.toString());

        String rawUserInfo = uri.getRawUserInfo();
        if (rawUserInfo != null && !rawUserInfo.isBlank()) {
            String[] credentials = rawUserInfo.split(":", 2);
            datasourceProps.put("spring.datasource.username", decode(credentials[0]));
            if (credentials.length > 1) {
                datasourceProps.put("spring.datasource.password", decode(credentials[1]));
            }
        }

        environment.getPropertySources().addFirst(new MapPropertySource(PROPERTY_SOURCE_NAME, datasourceProps));
    }

    private static String decode(String value) {
        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}

