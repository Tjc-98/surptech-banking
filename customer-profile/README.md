# customer-profile

Spring Boot microservice responsible for storing and retrieving customer personal information. It is a data-layer service and is consumed by the `data-aggregator`.

## Purpose

Manages the personal identity records of banking customers. Each record is keyed by Social Security Number and holds the customer's name and address. The service exposes a REST API for creating and retrieving profiles, backed by a local SQLite database.

## API Reference

Base URL: `http://localhost:5551/customer-profile`

### GET /customer/get

Retrieves a customer profile by Social Security Number.

**Query Parameters**

| Parameter | Type | Required | Description |
|---|---|---|---|
| `socialSecurityNumber` | string | Yes | USA format: `XXX-XX-XXXX` |

**Responses**

| Status | Description |
|---|---|
| 200 OK | Returns `CustomerProfileResponse` JSON |
| 400 Bad Request | Invalid SSN format |
| 404 Not Found | No profile exists for the given SSN |

**Example**

```
GET /customer-profile/customer/get?socialSecurityNumber=123-45-6789
```

```json
{
  "social_security_number": "123-45-6789",
  "first_name": "James",
  "last_name": "Smith",
  "address": "456 Tailor Street, California, LA 56001"
}
```

---

### POST /customer/create

Creates a new customer profile. If a profile with the same SSN already exists, it is updated (upsert).

**Request Body** (`application/json`)

```json
{
  "social_security_number": "555-66-7777",
  "first_name": "John",
  "last_name": "Doe",
  "address": "123 Main Street, New York, NY 10001"
}
```

All four fields are required. The SSN must be in USA format `XXX-XX-XXXX`.

**Responses**

| Status | Description |
|---|---|
| 201 Created | Returns the created/updated `CustomerProfileResponse` |
| 400 Bad Request | Validation failed — response body contains error details |

---

### GET /management/health

Spring Actuator health endpoint. Returns service health status with details.

```
GET /customer-profile/management/health
```

## Database

The service uses an embedded SQLite database stored in `customer-profile.db` in the working directory.

**Schema**

```sql
CREATE TABLE customer_profile (
    social_security_number TEXT PRIMARY KEY NOT NULL,
    first_name             TEXT NOT NULL,
    last_name              TEXT NOT NULL,
    address                TEXT NOT NULL
);
```

An index on `last_name` is created for faster lookups. The schema is initialized automatically on startup from `db/sqlite/customer_personal_information.sql`.

**Seed Data**

Two records are pre-loaded for development and testing:

| SSN | Name | Address |
|---|---|---|
| `123-45-6789` | James Smith | 456 Tailor Street, California, LA 56001 |
| `987-65-4321` | John Travolta | 123 West Street, New York, NY 875423 |

## Configuration

`src/main/resources/application.properties`

| Property | Default | Description |
|---|---|---|
| `server.port` | `5551` | HTTP port |
| `server.servlet.context-path` | `/customer-profile` | URL context path |
| `spring.datasource.url` | `jdbc:sqlite:customer-profile.db` | SQLite file path |
| `database.type` | `sqlite` | Repository implementation selector |
| `management.endpoints.web.base-path` | `/management` | Actuator base path |

The `database.type` property controls which `CustomerProfileRepository` implementation is loaded. Currently only `sqlite` is supported. The `RepositoryConfiguration` class uses a switch expression to select the implementation, making it straightforward to add PostgreSQL or MySQL support in the future.

## Architecture

```
CustomerProfileController
  └─ executeProcedure(new GetCustomerProfileProcedure(ssn))
       └─ BaseProcedure.execute()
            └─ executeProcedure()
                 └─ ApplicationContextProvider.getBean(CustomerProfileRepository)
                      └─ SqliteCustomerProfileRepository (JdbcTemplate → SQLite)
                           └─ CustomerProfileMapper.toResponse(entity)
```

The controller delegates all business logic to procedure classes. Procedures are instantiated with `new` and resolve Spring beans via `ApplicationContextProvider`. The repository uses an upsert pattern: it checks for an existing record before deciding between INSERT and UPDATE.

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
cd ../customer-profile
mvn spring-boot:run
```

The service starts on port `5551`. Verify it is running:

```bash
curl http://localhost:5551/customer-profile/management/health
```
