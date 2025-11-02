# ---------- Stage 1: Build ----------
FROM maven:3.9.9-eclipse-temurin-17 AS builder
WORKDIR /app

# Копируем POM'ы для кэширования зависимостей
COPY pom.xml .
COPY payment-service/pom.xml payment-service/pom.xml
COPY security-lib/pom.xml security-lib/pom.xml
COPY auth-service/pom.xml auth-service/pom.xml
COPY order-service/pom.xml order-service/pom.xml
COPY inventory-service/pom.xml inventory-service/pom.xml

# Загружаем зависимости (чтобы кешировать в Docker)
RUN mvn -B dependency:go-offline -DskipTests

# Теперь копируем остальной код
COPY . .

# Собираем только payment-service, но с зависимостями (-am)
RUN mvn -B -pl payment-service -am clean package -DskipTests

# ---------- Stage 2: Runtime ----------
FROM eclipse-temurin:17-jdk
WORKDIR /app

# Копируем готовый JAR
COPY --from=builder /app/payment-service/target/*.jar app.jar

EXPOSE 8083

ENTRYPOINT ["java", "-jar", "app.jar"]
