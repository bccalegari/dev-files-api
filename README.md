# Dev Files API

> **Study project developed for learning purposes.**

This is the main API gateway and business logic layer of the **DevFiles** platform.  
It provides RESTful endpoints for user management, file operations, and coordination between other microservices like AI and Notification.

Built with **Java 21** and **Spring Boot**, it integrates with multiple services and tools, including PostgreSQL, Redis, RabbitMQ, ChromaDB, and Ollama LLMs. It also handles authentication and authorization via JWT.


> This repository/module is part of the [DevFiles Monorepo](https://github.com/bccalegari/dev-files-monorepo).

## Features

- RESTful API for managing users and files
- Authentication and JWT-based access control
- Sends notifications via RabbitMQ to the notification service
- Sends files and queries to the AI service
- Integrates with Redis for caching and PostgreSQL for persistent storage
- Stores user files in AWS S3
- Exposes endpoints for uploading and downloading files
- Clean architecture with separation of concerns
- Propagate Trace IDs for distributed tracing through HTTP headers and RabbitMQ messages headers
- Swagger documentation for API endpoints
- HATEOAS support for discoverable APIs
- Migration scripts for database schema management
- Circuit breaker pattern to handle HTTP communication with AI service, retry, fallback, timout and rate limiting policies
- Asynchronous processing and event-driven architecture
- Slugs for resources
- Refresh tokens for long-lived sessions
- Health check endpoint for service status
- Standardized HTTP responses with error handling
- Pagination, filtering, and sorting for list endpoints
- RabbitMQ dead-letter queue for failed message processing, retry logic and quorum queues for reliability
- Redisson lock for distributed locking of file operations
- Asynchronous processing with virtual threads (Project Loom)
- Content negotiation for JSON and XML responses
- Header versioning

## Technologies Used

- Java 21
- Spring Boot 3.4.1
- Spring Security + JWT (Auth0)
- Spring Data JPA (PostgreSQL)
- Spring AMQP (RabbitMQ)
- Spring Data Redis (Redisson)
- AWS S3 (via AWS SDK v2)
- Spring Cloud OpenFeign
- Spring Cloud Circuit Breaker (Resilience4j)
- Flyway (PostgreSQL migrations)
- SpringDoc OpenAPI 3 (Swagger UI)
- Docker

## Testing
- Unit tests with JUnit 5
- Integration tests with Testcontainers for integration with PostgreSQL, Redis, RabbitMQ, and LocalStack
- Mocking with Mockito
- WireMock for HTTP client tests
- Spring Boot Test

Unit tests, integration tests, and end-to-end tests are implemented to ensure the reliability of the API.

### CI/CD
This project uses GitHub Actions for CI/CD. The workflow `build-and-test-project.yml` includes:
- Building the application
- Running tests to ensure code quality

## Prerequisites
- Docker installed
- dev-files-api compose file running (for RabbitMQ)
- devfiles-network and devfiles-rabbitmq networks created

```bash
docker network create devfiles-network
docker network create devfiles-rabbitmq
```

## Migrations

Database migrations are managed using Flyway.
Run the following makefile command to see available migration commands:

```bash
make migrations help
```

## Running locally

1. **Clone the repository:**
   ```bash
   git clone https://github.com/bccalegari/dev-files-api.git
   ````

2. **Navigate to the project directory:**
   ```bash
    cd dev-files-api
    ```

3. **Create a `.env` file:**
    ```bash
    cp .env.example .env
    ```
    Copy the `.env.example` to `.env` and fill in the required environment variables.

4. **Run dev compose:**
    ```bash
    docker compose -f docker-compose.dev.yml up
    ```

5. **Run the application:**
    <br>
    The application will start automatically with the Docker Compose setup. You can access the logs to see if it's running correctly.

6. **Access Swagger UI:**
    <br>
   Open your browser and navigate to `http://localhost:8080/swagger-ui/index.html` to explore the API documentation.

## Logging
Logs are managed using Logback, and the configuration is set in `src/main/resources/logback-spring.xml`. The logs are output to the console by default, but you can configure it to write to a file or other appenders as needed.

Trace IDs are propagated through HTTP headers and RabbitMQ message headers for distributed tracing.

## Integration with Other Services

This API integrates with the following services:

- **Notification Service**: Sends notifications via RabbitMQ when certain events happen.

- **AI Service**: Requests AI processing for embedding files and query over the files through HTTP requests.

---

> **Study project developed for learning purposes.**

Built with ❤️ by Bruno Calegari