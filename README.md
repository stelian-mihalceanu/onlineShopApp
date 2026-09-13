# onlineShopApp

Spring Boot e-commerce application with REST + web MVC, PostgreSQL, Spring Security, Thymeleaf, and Selenium E2E tests.
Built as a portfolio project for QA automation / mid-level Java backend roles.

## Tech stack

- **Runtime**: Java 17, Spring Boot 3.3
- **Web**: Spring MVC, Thymeleaf (server-rendered pages)
- **Data**: PostgreSQL (JPA/Hibernate)
- **Security**: Spring Security (form login, password hashing)
- **Validation**: Jakarta Bean Validation
- **Database migrations**: Flyway
- **Tests**:
  - Unit & integration tests (JUnit, Spring test)
  - External backend/API tests in [`onlineShopApp-test`](https://github.com/stelian-mihalceanu/onlineShopApp-test)
  - Frontend page tests in [`onlineShopApp-test`](https://github.com/stelian-mihalceanu/onlineShopApp-test)
  - E2E UI tests (Selenium WebDriver)
- **DevOps**: Docker & Docker Compose (app, PostgreSQL)

Kafka and Zookeeper are intentionally removed from the current MVP. The application does not require Kafka-related environment variables or messaging infrastructure at runtime.

## Architecture overview

```text
┌────────────┐      ┌─────────────┐      ┌────────────┐
│  Browser   │─────▶│  Spring Boot│─────▶│ PostgreSQL │
│   / REST   │      │  onlinestore│      │ onlinestore │
└────────────┘      └─────────────┘      └────────────┘
```

Key packages (under `onlinestore/src/main/java/com/onlinestore`):

- `controller` – REST & web controllers (`Auth*`, `Product*`, `Cart*`, `Home`)
- `service` – business logic (`CartService`, `ProductService`, `UserService`, `OrderService`)
- `repository` – Spring Data JPA repositories
- `model` – JPA entities (`User`, `Product`, `CartItem`, `Order`)
- `security` – Spring Security config

## Features

- User registration & login
- Product browsing & details
- Shopping cart (add, update, remove items)
- Checkout and order creation
- Server-rendered Thymeleaf UI
- REST endpoints for application operations
- Input validation and Spring Security
- Flyway database migrations

## Prerequisites

- Docker & Docker Compose
- Java 17+ (for local runs)
- Maven (bundled via `mvnw` wrapper)

## Running with Docker

From the project root:

```bash
docker compose up --build
```

This starts:

- **App**: http://localhost:8080
- **PostgreSQL**: `jdbc:postgresql://localhost:5432/onlinestore`
  - Username: `user`
  - Password: `password`

The app runs with `SPRING_PROFILES_ACTIVE=docker` and uses `application-docker.properties`.

## Running locally

1. Start PostgreSQL locally and create a database `onlinestore`.
2. Set environment variables:

```bash
export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/onlinestore
export SPRING_DATASOURCE_USERNAME=user
export SPRING_DATASOURCE_PASSWORD=password
```

3. Run the app:

```bash
cd onlinestore
./mvnw spring-boot:run
```

Then open http://localhost:8080.

## Configuration

Main config files:

- `onlinestore/src/main/resources/application.properties` – base config
- `onlinestore/src/main/resources/application-docker.properties` – Docker profile
- `onlinestore/src/main/resources/application-railway.properties` – Railway production profile
- `docker-compose.yml` – app + PostgreSQL services

Production uses:

- `spring.jpa.hibernate.ddl-auto=validate`
- Flyway migrations
- PostgreSQL credentials supplied through environment variables
- `server.forward-headers-strategy=framework` for reverse-proxy deployments

No Kafka or Zookeeper configuration is required.

## Testing

### Unit & integration tests

```bash
cd onlinestore
./mvnw test
```

Tests use H2 in-memory DB where configured and do not require external messaging infrastructure.

### External QA repository

Backend/API, frontend smoke tests, and end-to-end Selenium coverage are maintained separately in:

https://github.com/stelian-mihalceanu/onlineShopApp-test

The QA pipeline starts a real PostgreSQL-backed application and executes the external test suite against it.

### E2E Selenium tests

E2E tests can also live under `onlinestore/src/test/java/com/onlinestore/e2e` and assume the app is reachable at `http://localhost:8080`.

Run with:

```bash
./mvnw test -Dtest="*E2eTest"
```

## Database schema

The production profile uses Flyway migrations and Hibernate schema validation. Schema changes should be made through versioned migrations rather than `ddl-auto=update`.

## Project structure

```text
.
├── docker-compose.yml
├── Dockerfile
├── onlinestore/
│   ├── pom.xml
│   └── src/
│       ├── main/
│       │   ├── java/com/onlinestore/
│       │   │   ├── config/
│       │   │   ├── controller/
│       │   │   ├── model/
│       │   │   ├── repository/
│       │   │   ├── security/
│       │   │   └── service/
│       │   └── resources/
│       │       ├── application.properties
│       │       ├── application-docker.properties
│       │       ├── application-railway.properties
│       │       ├── db/migration/
│       │       └── templates/
│       └── test/
│           └── java/com/onlinestore/
└── README.md
```

## Deployment

Railway should provide the PostgreSQL connection through:

```text
SPRING_DATASOURCE_URL
SPRING_DATASOURCE_USERNAME
SPRING_DATASOURCE_PASSWORD
PORT
```

The Docker image starts Spring Boot with the Railway profile by default. No Kafka or Zookeeper service or environment variables are needed.

## Recommended next improvements

- Add pagination and filtering for the product catalog.
- Expand inventory/stock business rules.
- Improve API error handling with consistent problem responses.
- Add CI static analysis and dependency scanning.
- Introduce event-driven messaging only when there is a concrete use case.

## License

MIT (or your preferred license).
