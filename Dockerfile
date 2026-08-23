# Build stage
FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /app

# Copy Maven project
COPY onlinestore/pom.xml .
COPY onlinestore/src ./src
COPY onlinestore/mvnw .
COPY onlinestore/.mvn ./.mvn

# Build production JAR without compiling/running test sources
RUN chmod +x mvnw && ./mvnw clean package -Dmaven.test.skip=true

# Run stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copy the packaged Spring Boot application
COPY --from=builder /app/target/*.jar app.jar

# Railway should override this with SPRING_PROFILES_ACTIVE=railway
ENV SPRING_PROFILES_ACTIVE=railway

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]