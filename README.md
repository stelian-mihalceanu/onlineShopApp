# onlineShopApp

Spring Boot e-commerce application with REST + web MVC, PostgreSQL, Spring Security, Thymeleaf, and Selenium E2E tests.
Built as a portfolio project for QA automation / mid-level Java backend roles.

## Tech stack

- **Runtime**: Java 17, Spring Boot 3.3
- **Web**: Spring MVC, Thymeleaf (server-rendered pages)
- **Data**: PostgreSQL (JPA/Hibernate)
- **Security**: Spring Security (form login, password hashing)
- **Validation**: Jakarta Bean Validation
- **Tests**:
  - Unit & integration tests (JUnit, Spring test)
  - E2E UI tests (Selenium WebDriver)
- **DevOps**: Docker & Docker Compose (app, PostgreSQL)

Kafka and Zookeeper are intentionally removed from the current MVP to keep the application easier to run, test, and deploy. Event-driven messaging can be introduced later when there is a real downstream consumer such as analytics, notifications, or order processing.

## Architecture overview

```
┌────────────┐      ┌─────────────┐      ┌────────────┐
│  Browser   │─────▶│  Spring Boot│─────▶│ PostgreSQL │
│   / REST   │      │   onlinestore│      │ onlinestore│
└────────────┘      └─────────────┘      └────────────┘
```

Key packages (under `onlinestore/src/main/java/com/onlinestore`):

- `controller` – REST & web controllers (`Auth*`, `Product*`, `Cart*`, `Home`)
- `service` – business logic (`CartService`, `ProductService`, `UserService`)
- `repository` – Spring Data JPA repositories
- `model` – JPA entities (`User`, `Product`, `CartItem`)
- `security` – Spring Security config

## Features

- User registration & login
- Product browsing & details
- Shopping cart (add, update, remove items)
- Server-rendered Thymeleaf UI
- REST endpoints for application operations
- Input validation and Spring Security
- Selenium E2E tests for key user journeys

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

- `onlinestore/src/main/resources/application.properties` – default config
- `onlinestore/src/main/resources/application-docker.properties` – Docker profile config
- `docker-compose.yml` – app + PostgreSQL services

Key properties:

- `spring.datasource.*` – PostgreSQL connection
- `spring.jpa.hibernate.ddl-auto=update` – development-friendly schema updates

For production, replace `ddl-auto=update` with an explicit migration strategy such as Flyway or Liquibase.

## Testing

### Unit & integration tests

```bash
cd onlinestore
./mvnw test
```

Tests use H2 in-memory DB where configured and do not require external messaging infrastructure.

### E2E Selenium tests

E2E tests live under `src/test/java/com/onlinestore/e2e`:

- `HomePageE2eTest`
- `ProductE2eTest`
- `CartE2eTest`
- `RegisterE2eTest`

Run with:

```bash
./mvnw test -Dtest="*E2eTest"
```

Notes:
- Ensure the app is running before E2E tests.
- Tests assume the app is reachable at `http://localhost:8080`.

## Database schema

Entities (JPA):

- `User` – application users (credentials, roles)
- `Product` – product catalog (id, name, price, etc.)
- `CartItem` – user cart entries (userId, productId, quantity, price)

Schema is auto-created/updated by Hibernate (`ddl-auto=update`). For production, use explicit database migrations.

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
│       │       └── templates/
│       └── test/
│           └── java/com/onlinestore/
│               ├── controller/
│               ├── e2e/
│               └── service/
└── README.md
```

## Recommended next improvements

- Add `Order` and `OrderItem` entities with a checkout flow.
- Add stock/inventory validation and transactional checkout.
- Add pagination and filtering for the product catalog.
- Introduce Flyway/Liquibase migrations.
- Improve API error handling with consistent problem responses.
- Add CI checks for build, tests, and static analysis.
- Re-introduce event-driven messaging only when there is a concrete use case.

## License

MIT (or your preferred license).
