# Stage 1: Build native image
FROM ghcr.io/graalvm/native-image-community:25 AS builder

WORKDIR /app

# Copy Gradle wrapper, build files and sources
COPY gradlew ./
COPY gradle/wrapper/ ./gradle/wrapper/
COPY build.gradle settings.gradle ./
COPY src/ src/

# Build native image
RUN chmod +x gradlew && ./gradlew nativeCompile --no-daemon

# Stage 2: Deployment
FROM debian:stable-slim

WORKDIR /app

# Install zlib (required for GraalVM native image)
RUN apt-get update && apt-get install -y --no-install-recommends zlib1g && rm -rf /var/lib/apt/lists/*

# Copy native binary
COPY --from=builder /app/build/native/nativeCompile/joi-delivery /app/joi-delivery

# Copy resources
COPY --from=builder /app/src/main/resources /app/resources

EXPOSE 8080

ENTRYPOINT ["/app/joi-delivery"]