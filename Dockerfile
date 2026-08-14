# Build stage
FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /app

# Copy Maven project
COPY onlinestore/pom.xml .
COPY onlinestore/src ./src
COPY onlinestore/mvnw .
COPY onlinestore/.mvn ./.mvn

# Build the JAR
RUN chmod +x mvnw && ./mvnw -f onlinestore/pom.xml clean package -DskipTests

# Run stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Wildcard matches any JAR in target/
COPY --from=builder /app/onlinestore/target/*.jar app.jar

ENV SPRING_PROFILES_ACTIVE=prod
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]