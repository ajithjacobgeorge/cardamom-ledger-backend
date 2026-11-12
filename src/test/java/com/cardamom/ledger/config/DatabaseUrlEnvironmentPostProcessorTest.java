package com.cardamom.ledger.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.core.env.MapPropertySource;
import org.springframework.mock.env.MockEnvironment;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class DatabaseUrlEnvironmentPostProcessorTest {

    private final DatabaseUrlEnvironmentPostProcessor processor = new DatabaseUrlEnvironmentPostProcessor();

    @Test
    @DisplayName("Converts postgres/postgresql DATABASE_URL into Spring JDBC properties")
    void convertsPostgresDatabaseUrl() {
        MockEnvironment env = new MockEnvironment();
        env.setProperty("DATABASE_URL", "postgresql://neon_user:p%40ss%3A123@ep-cool-leaf.neon.tech:5432/cardamom_db?sslmode=require");

        processor.postProcessEnvironment(env, new SpringApplication(DatabaseUrlEnvironmentPostProcessorTest.class));

        assertThat(env.getProperty("spring.datasource.url"))
                .isEqualTo("jdbc:postgresql://ep-cool-leaf.neon.tech:5432/cardamom_db?sslmode=require");
        assertThat(env.getProperty("spring.datasource.username")).isEqualTo("neon_user");
        assertThat(env.getProperty("spring.datasource.password")).isEqualTo("p@ss:123");
    }

    @Test
    @DisplayName("Skips conversion when SPRING_DATASOURCE_URL is explicitly provided")
    void skipsWhenExplicitJdbcUrlPresent() {
        MockEnvironment env = new MockEnvironment();
        env.getPropertySources().addFirst(new MapPropertySource("explicit", Map.of(
                "SPRING_DATASOURCE_URL", "jdbc:postgresql://custom-host:5432/custom_db",
                "DATABASE_URL", "postgres://user:pass@other-host:5432/other_db"
        )));

        processor.postProcessEnvironment(env, new SpringApplication(DatabaseUrlEnvironmentPostProcessorTest.class));

        assertThat(env.getProperty("spring.datasource.url")).isNull();
    }
}
