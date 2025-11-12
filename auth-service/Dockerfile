FROM maven:3.9.0-eclipse-temurin-17

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src

EXPOSE 8080

# Run Spring Boot in foreground and prevent Maven fork (container stays alive)
CMD ["mvn", "spring-boot:run", "-Dspring-boot.run.fork=false"]
