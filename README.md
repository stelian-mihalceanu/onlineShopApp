# onlineShopApp

Spring Boot e‑commerce backend with REST + web MVC, PostgreSQL, Kafka, and Selenium E2E tests.
Built as a portfolio project for QA automation / mid‑level Java backend roles.

## Tech stack

- **Runtime**: Java 17, Spring Boot 3.3
- **Web**: Spring MVC, Thymeleaf (server‑rendered pages)
- **Data**: PostgreSQL (JPA/Hibernate)
- **Messaging**: Apache Kafka (cart events)
- **Security**: Spring Security (form login, password hashing)
- **Tests**:
  - Unit & integration tests (JUnit, Spring test)
  - E2E UI tests (Selenium WebDriver)
- **DevOps**: Docker & Docker Compose (app, Postgres, Kafka, Zookeeper)

## Architecture overview

```
┌────────────┐      ┌─────────────┐      ┌────────────┐
│  Browser   │─────▶│  Spring Boot│─────▶│ PostgreSQL │
│  (UI)      │      │  (onlinestore)    │  (onlinestore)
└────────────┘      └─────────────┘      └────────────┘
                           │
                           ▼
                      ┌────────────┐
                      │   Kafka    │
                      │ cart-events│
                      └────────────┘
                           │
                           ▼
                  (logging / analytics)
```

Key packages (under `onlinestore/src/main/java/com/onlinestore`):

- `controller` – REST & web controllers (`Auth*`, `Product*`, `Cart*`, `Home`)
- `service` – business logic (`CartService`, `ProductService`, `UserService`, Kafka producer/consumer)
- `repository` – Spring Data JPA repositories
- `model` – JPA entities (`User`, `Product`, `CartItem`)
- `security` – Spring Security config
- `event` – Kafka event DTOs (`CartEvent`)

## Features

- User registration & login
- Product browsing & details
- Shopping cart (add, update, remove items)
- Kafka events for cart operations (`ADD`, `UPDATE`, `REMOVE`)
- Selenium E2E tests for key user journeys

## Prerequisites

- Docker & Docker Compose
- Java 17+ (for local runs)
- Maven (bundled via `mvnw` wrapper)

## Running with Docker (recommended)

From the project root:

```bash
docker compose up --build
```

This starts:

- **App**: http://localhost:8080
- **PostgreSQL**: `jdbc:postgresql://localhost:5432/onlinestore`
  - Username: `user`
  - Password: `password`
- **Kafka**: `localhost:9092`
  - Topic: `cart-events`
- **Zookeeper**: `localhost:2181`

Profiles:
- The app runs with `SPRING_PROFILES_ACTIVE=docker` and uses `application-docker.properties`.

## Running locally (without Docker)

1. Start PostgreSQL locally and create a database `onlinestore`.
2. Set environment variables (or edit `application.properties`):

```bash
export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/onlinestore
export SPRING_DATASOURCE_USERNAME=user
export SPRING_DATASOURCE_PASSWORD=password
export SPRING_KAFKA_BOOTSTRAP_SERVERS=localhost:9092
```

3. Start Kafka & Zookeeper locally (e.g. via Docker or your own setup).
4. Run the app:

```bash
cd onlinestore
./mvnw spring-boot:run
```

Then open http://localhost:8080.

## Configuration

Main config files:

- `onlinestore/src/main/resources/application.properties` – default (local) config
- `onlinestore/src/main/resources/application-docker.properties` – Docker profile config
- `docker-compose.yml` – services and environment variables

Key properties:

- `spring.datasource.*` – PostgreSQL connection
- `spring.jpa.hibernate.ddl-auto=update` – auto‑migrate schema
- `spring.kafka.*` – Kafka bootstrap servers, consumer group, serializers

## Testing

### Unit & integration tests

```bash
cd onlinestore
./mvnw test
```

Tests use H2 in‑memory DB (test profile) and do not require external Kafka.

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
- Ensure the app is running (Docker or local) before E2E tests.
- Tests assume the app is reachable at `http://localhost:8080`.

## Kafka usage

Cart operations publish events to topic `cart-events`:

- `addToCart` → `ADD` or `UPDATE`
- `removeItem` → `REMOVE`

Event payload (JSON):

```json
{
  "userId": "string",
  "productId": "string",
  "quantity": 0,
  "type": "ADD|UPDATE|REMOVE"
}
```

Consumer (`CartEventConsumer`) logs events; you can extend it for:

- Analytics / dashboards
- Notifications
- Downstream microservices

## Database schema

Entities (JPA):

- `User` – application users (credentials, roles)
- `Product` – product catalog (id, name, price, etc.)
- `CartItem` – user cart entries (userId, productId, quantity, price)

Schema is auto‑created/updated by Hibernate (`ddl-auto=update`). For production, consider migration tools like Flyway or Liquibase.

## Project structure

```
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
│       │   │   ├── event/
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

## How this helps in interviews

You can talk about:

- Designing a layered Spring Boot backend (controllers, services, repositories).
- Securing endpoints with Spring Security.
- Modeling relational data with JPA/Hibernate.
- Adding event‑driven architecture with Kafka.
- Writing tests at multiple levels (unit, integration, E2E with Selenium).
- Containerizing the app and orchestrating services with Docker Compose.

## Next steps / ideas

Possible extensions:

- Add an `Order` entity and `OrderService`, publishing `order-placed` events.
- Add a separate consumer service (e.g., for analytics or email notifications).
- Introduce Flyway/Liquibase for explicit schema migrations.
- Add more E2E scenarios (login, checkout flow).
- Deploy to a cloud provider (e.g., Oracle Cloud, GCP, AWS) and document the setup.

## License

MIT (or your preferred license).
