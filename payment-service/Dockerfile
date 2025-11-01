# ---------- Stage 1: Build ----------
FROM maven:3.9.9-eclipse-temurin-17 AS builder
WORKDIR /app

COPY ../pom.xml .
COPY ../security-lib ./security-lib
COPY ./ ./payment-service
COPY ../auth-service ./auth-service
COPY ../order-service ./order-service
COPY ../inventory-service ./inventory-service

RUN mvn -B clean install -DskipTests

# ---------- Stage 2: Runtime ----------
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=builder /app/payment-service/target/*.jar app.jar
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "app.jar"]
