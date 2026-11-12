# syntax=docker/dockerfile:1

# =============================================================================
# Stage 1: Build & Extract Spring Boot 3.3+ Layered JAR
# =============================================================================
FROM maven:3.9.9-eclipse-temurin-21-alpine AS builder
WORKDIR /workspace

# Download dependencies first so Docker caches them across code-only changes
COPY pom.xml .
RUN mvn -B dependency:go-offline

# Build the application JAR (run tests locally / in CI before container build)
COPY src ./src
RUN mvn -B package -DskipTests \
    && JAR_PATH=$(ls target/*.jar | head -n 1) \
    && java -Djarmode=tools -jar "$JAR_PATH" extract --layers --destination extracted \
    && mkdir -p extracted/dependencies extracted/spring-boot-loader extracted/snapshot-dependencies extracted/application

# =============================================================================
# Stage 2: Minimal JRE Runtime Container (Tuned for Render 512 MB Free Tier)
# =============================================================================
FROM eclipse-temurin:21-jre-alpine AS runtime
WORKDIR /app

# Run as a non-root user
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Copy extracted layers from least to most frequently changed
COPY --from=builder --chown=appuser:appgroup /workspace/extracted/dependencies/ ./
COPY --from=builder --chown=appuser:appgroup /workspace/extracted/spring-boot-loader/ ./
COPY --from=builder --chown=appuser:appgroup /workspace/extracted/snapshot-dependencies/ ./
COPY --from=builder --chown=appuser:appgroup /workspace/extracted/application/ ./

USER appuser

# Render injects PORT at runtime (defaulting to 8080 for local Docker runs).
# Heap capped at 65% of container RAM to leave headroom for metaspace, threads, and OS in 512 MB.
ENV SPRING_PROFILES_ACTIVE=prod \
    PORT=8080 \
    JAVA_TOOL_OPTIONS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=65.0 -XX:InitialRAMPercentage=25.0 -XX:+UseSerialGC -XX:+ExitOnOutOfMemoryError -Djava.security.egd=file:/dev/urandom"

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=5s --start-period=45s --retries=3 \
    CMD wget -qO- "http://127.0.0.1:${PORT}/api/health" >/dev/null || exit 1

ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]

