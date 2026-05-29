# credit-profile

Spring Boot microservice responsible for storing and retrieving customer credit information. It is a data-layer service and is consumed by the `data-aggregator`.

## Purpose

Manages the credit records of banking customers. Each record is keyed by Social Security Number and holds the customer's total credit balance, current spend balance, and interest rate. The service exposes a REST API for creating and retrieving credit profiles, backed by a local SQLite database.

## API Reference

Base URL: `http://localhost:5552/credit-profile`

### GET /credit/get

Retrieves a credit profile by Social Security Number.

**Query Parameters**

| Parameter | Type | Required | Description |
|---|---|---|---|
| `socialSecurityNumber` | string | Yes | USA format: `XXX-XX-XXXX` |

**Responses**

| Status | Description |
|---|---|
| 200 OK | Returns `CreditProfileResponse` JSON |
| 400 Bad Request | Invalid SSN format |
| 404 Not Found | No credit profile exists for the given SSN |

**Example**

```
GET /credit-profile/credit/get?socialSecurityNumber=123-45-6789
```

```json
{
  "social_security_number": "123-45-6789",
  "full_credit_balance": 15000.00,
  "spend_balance": 5000.00,
  "interest_rate": 3.5
}
```

---

### POST /credit/create

Creates a new credit profile. If a profile with the same SSN already exists, it is updated (upsert).

**Request Body** (`application/json`)

```json
{
  "social_security_number": "555-66-7777",
  "full_credit_balance": 10000.00,
  "spend_balance": 0.00,
  "interest_rate": 5.0
}
```

All fields are required. The SSN must be in USA format `XXX-XX-XXXX`. Numeric fields must be non-negative.

**Responses**

| Status | Description |
|---|---|
| 201 Created | Returns the created/updated `CreditProfileResponse` |
| 400 Bad Request | Validation failed — response body contains error details |

---

### GET /management/health

Spring Actuator health endpoint. Returns service health status with details.

```
GET /credit-profile/management/health
```

## Database

The service uses an embedded SQLite database stored in `credit-profile.db` in the working directory.

**Schema**

```sql
CREATE TABLE credit_profile (
    social_security_number TEXT PRIMARY KEY NOT NULL,
    full_credit_balance    REAL NOT NULL,
    spend_balance          REAL NOT NULL,
    interest_rate          REAL NOT NULL
);
```

An index on `full_credit_balance` is created for faster range queries. The schema is initialized automatically on startup from `db/sqlite/credit_information.sql`.

**Seed Data**

Two records are pre-loaded for development and testing. SSNs match the seed data in `customer-profile`.

| SSN | Full Credit Balance | Spend Balance | Interest Rate |
|---|---|---|---|
| `123-45-6789` | $15,000.00 | $5,000.00 | 3.5% |
| `987-65-4321` | $28,000.00 | $12,000.00 | 8.5% |

## Configuration

`src/main/resources/application.properties`

| Property | Default | Description |
|---|---|---|
| `server.port` | `5552` | HTTP port |
| `server.servlet.context-path` | `/credit-profile` | URL context path |
| `spring.datasource.url` | `jdbc:sqlite:credit-profile.db` | SQLite file path |
| `database.type` | `sqlite` | Repository implementation selector |
| `management.endpoints.web.base-path` | `/management` | Actuator base path |

The `database.type` property controls which `CreditProfileRepository` implementation is loaded. Currently only `sqlite` is supported. The `RepositoryConfiguration` class uses a switch expression to select the implementation, making it straightforward to add PostgreSQL or MySQL support in the future.

## Architecture

```
CreditProfileController
  └─ executeProcedure(new GetCreditProfileProcedure(ssn))
       └─ BaseProcedure.execute()
            └─ executeProcedure()
                 └─ ApplicationContextProvider.getBean(CreditProfileRepository)
                      └─ SqliteCreditProfileRepository (JdbcTemplate → SQLite)
                           └─ CreditProfileMapper.toResponse(entity)
```

The controller delegates all business logic to procedure classes. Procedures are instantiated with `new` and resolve Spring beans via `ApplicationContextProvider`. The repository uses an upsert pattern: it checks for an existing record before deciding between INSERT and UPDATE.

**Note on numeric types:** The internal domain entity and repository use `BigDecimal` for financial precision. The `data-aggregator` service currently maps these fields to `Double` when aggregating responses.

## Technology Stack

| Technology | Version |
|---|---|
| Java | 25 |
| Spring Boot | 4.0.6 |
| Spring JDBC | (managed by Spring Boot) |
| SQLite JDBC | 3.47.1.0 |
| Lombok | 1.18.46 |
| surptech-common | 1.0.0-SNAPSHOT |

## Running

`surptech-common` must be installed first.

```bash
# From the repository root
cd surptech-common
mvn clean install

# Start the service
cd ../credit-profile
mvn spring-boot:run
```

The service starts on port `5552`. Verify it is running:

```bash
curl http://localhost:5552/credit-profile/management/health
```
