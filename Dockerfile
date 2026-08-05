# Build Stage
FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /app

# Resolve dependencies first — this layer stays cached until pom.xml changes,
# so source-only edits skip the download entirely.
COPY pom.xml .
RUN mvn -B dependency:go-offline

COPY src ./src

RUN mvn -B clean package -DskipTests

# Runtime Stage
FROM eclipse-temurin:21-jre

RUN apt-get update \
    && apt-get install -y --no-install-recommends curl \
    && rm -rf /var/lib/apt/lists/* \
    && useradd -r -u 1001 appuser

WORKDIR /app

COPY --from=build --chown=appuser:appuser /app/target/*.jar app.jar

# The app writes app.log into its working directory.
RUN chown appuser:appuser /app

USER appuser

EXPOSE 8080

HEALTHCHECK --interval=15s --timeout=3s --start-period=40s --retries=5 \
    CMD curl -fsS http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java","-jar","app.jar"]
