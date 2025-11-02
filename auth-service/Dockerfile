# ---------- Stage 1: Build ----------
FROM maven:3.9.9-eclipse-temurin-17 AS builder
WORKDIR /app

# Копируем только POM-файлы для кэширования зависимостей
COPY pom.xml .
COPY auth-service/pom.xml auth-service/pom.xml
COPY security-lib/pom.xml security-lib/pom.xml
COPY order-service/pom.xml order-service/pom.xml
COPY payment-service/pom.xml payment-service/pom.xml
COPY inventory-service/pom.xml inventory-service/pom.xml

# Предзагрузка зависимостей для ускорения сборки
RUN mvn -B dependency:go-offline -DskipTests

# Копируем оставшуюся часть проекта
COPY . .

# Собираем только auth-service (вместе с зависимыми модулями)
RUN mvn -B -pl auth-service -am clean package -DskipTests

# ---------- Stage 2: Runtime ----------
FROM eclipse-temurin:17-jdk
WORKDIR /app

# Копируем собранный JAR auth-service
COPY --from=builder /app/auth-service/target/*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]
