# ---------- Stage 1: Build ----------
FROM maven:3.9.9-eclipse-temurin-17 AS builder
WORKDIR /app

# Копируем родительский POM и все модули
COPY ../pom.xml .
COPY ../security-lib ./security-lib
COPY ./ ./auth-service
COPY ../order-service ./order-service
COPY ../payment-service ./payment-service
COPY ../inventory-service ./inventory-service

# Собираем весь проект (включая security-lib)
RUN mvn -B clean install -DskipTests

# ---------- Stage 2: Runtime ----------
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=builder /app/auth-service/target/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
