# Dockerfile for Trading Service
FROM maven:3.9.0-eclipse-temurin-17 AS build

# Set working directory
WORKDIR /app

# Copy Maven files and source code
COPY pom.xml .
COPY src ./src

# Build the project and skip tests
RUN mvn clean package -DskipTests

# Final image
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copy the jar from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose port
EXPOSE 8080

# Run the jar
ENTRYPOINT ["java", "-jar", "app.jar"]
