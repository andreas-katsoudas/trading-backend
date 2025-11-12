# Trading Backend Simulator

This repository contains a **trading simulator backend** built with Java/Kotlin and Spring Boot.  
It includes multiple microservices and a Docker setup for local development.

## Services

- **market-data-service**: Streams simulated market prices.  
- **market-trading-service**: Handles trading logic and orders.  
- **auth-service**: User authentication and role management.  

## Features

- Simulated market data streaming via WebSockets.  
- Trade execution and order logging.  
- Kafka integration for messaging between services.  
- Redis caching for fast data access.  
- PostgreSQL database
- Fully containerized with Docker for easy setup.

## Prerequisites

- Docker & Docker Compose  
- Java 17  
- Maven (for building services)

## Getting Started

Clone the repository:
```bash
git clone https://github.com/andreas-katsoudas/trading-backend.git
cd trading-backend
```

Build and run all services using Docker Compose:
```bash
docker-compose up --build
```

- Kafka, PostgreSQL, Redis, and all services will start automatically.  
- Access microservices on their respective ports (as defined in `docker-compose.yml`).

## Notes

- Make sure your local ports (Kafka, Postgres, Redis) are free before starting.  
- Each service can also be run individually using Maven:
```bash
cd market-data-service
mvn clean install
mvn spring-boot:run
```

## License

This project is licensed under the **MIT License**. See the [LICENSE](LICENSE) file for details.

