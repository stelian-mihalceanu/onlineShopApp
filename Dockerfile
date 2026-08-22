# Build stage
FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /app

# Copy Maven project
COPY onlinestore/pom.xml .
COPY onlinestore/src ./src
COPY onlinestore/mvnw .
COPY onlinestore/.mvn ./.mvn

# Build the JAR - REMOVED the -f flag
RUN chmod +x mvnw && ./mvnw clean package -DskipTests

# Run stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Update this path since pom.xml is at root
COPY --from=builder /app/target/*.jar app.jar

ENV SPRING_PROFILES_ACTIVE=prod
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]