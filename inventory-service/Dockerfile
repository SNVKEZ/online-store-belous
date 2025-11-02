# ---------- Stage 1: Build ----------
FROM maven:3.9.9-eclipse-temurin-17 AS builder
WORKDIR /app

# Копируем только POM-файлы для кэширования зависимостей
COPY pom.xml .
COPY inventory-service/pom.xml inventory-service/pom.xml
COPY auth-service/pom.xml auth-service/pom.xml
COPY payment-service/pom.xml payment-service/pom.xml
COPY order-service/pom.xml order-service/pom.xml
COPY security-lib/pom.xml security-lib/pom.xml

# Предварительно загружаем зависимости (чтобы слои кешировались)
RUN mvn -B dependency:go-offline -DskipTests

# Теперь копируем остальной код проекта
COPY . .

# Собираем только inventory-service (вместе с зависимыми модулями)
RUN mvn -B -pl inventory-service -am clean package -DskipTests

# ---------- Stage 2: Runtime ----------
FROM eclipse-temurin:17-jdk
WORKDIR /app

# Копируем собранный jar inventory-service
COPY --from=builder /app/inventory-service/target/*.jar app.jar

EXPOSE 8084

ENTRYPOINT ["java", "-jar", "app.jar"]
