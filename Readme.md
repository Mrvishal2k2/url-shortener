# Shortener

A URL shortener REST API built with Java and Spring Boot.

## Features
- Auto-generated or custom short ID creation
- Configurable URL expiration
- Global exception handling with standardized JSON error responses

## Tech Stack
- Java 21
- Spring Boot 4 (Web, Data JPA, Validation)
- Lombok
- Maven
- Redis
- MySql
- Flyway Migration

## Getting Started

1. Configure your database settings in `src/main/resources/application.properties`.
2. Build and run:
   ```bash
   mvn clean spring-boot:run
   ```
   
## TODO
- Add Redis caching
- Build test cases and increase coverage
- Implement wait based shortRedirect as well
- Dockerize and deploy at my server.
- Create Architecture Diagram
- Use env way instead of direct properties vars
- User based shorten functionality
- Better country level analysis.
- Better validation
- [✅] OpenApi/Swagger & Graphana
- Build minimal frontend (Optional)