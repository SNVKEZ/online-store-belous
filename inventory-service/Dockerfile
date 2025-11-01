# ---------- Stage 1: Build ----------
FROM maven:3.9.9-eclipse-temurin-17 AS builder
WORKDIR /app

COPY ../pom.xml .
COPY ../security-lib ./security-lib
COPY ./ ./inventory-service
COPY ../auth-service ./auth-service
COPY ../order-service ./order-service
COPY ../payment-service ./payment-service

RUN mvn -B clean install -DskipTests

# ---------- Stage 2: Runtime ----------
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=builder /app/inventory-service/target/*.jar app.jar
EXPOSE 8084
ENTRYPOINT ["java", "-jar", "app.jar"]
