# SurpTech Banking

A simple banking information lookup application built with Spring Boot and Thymeleaf.

---

## About

Written in Java, this project is a single Spring Boot application that stores customer personal and credit information and lets users look it up by Social Security Number. It exposes both a web UI and a REST API. Data is stored in an H2 in-memory database that is seeded with two customers on startup.

## Usage

Open `http://localhost:8080` in your browser, enter a Social Security Number, and the application returns the matching customer and credit profile. The REST API can also be queried directly.

### REST API

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/customer/info?socialSecurityNumber=XXX-XX-XXXX` | Returns combined customer and credit info |
| POST | `/api/customer/create` | Creates or updates a customer profile |
| POST | `/api/credit/create` | Creates or updates a credit profile |

### Seed data

Two records are loaded on startup for development and testing.

| SSN | Name | Credit Balance | Interest Rate |
|-----|------|----------------|---------------|
| `123-45-6789` | James Smith | $15,000 | 3.5% |
| `987-65-4321` | John Travolta | $28,000 | 8.5% |

The H2 console is available at `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:surptechdb`).

## Getting Started

### Prerequisites

- Java 21 or later
- Maven 3.8 or later

### Building

**Unix / Windows**
```
mvn clean package
```

### Running

**Unix / Windows**
```
mvn spring-boot:run
```

Then open `http://localhost:8080` in your browser.

## Running Tests

```
mvn clean test
```

The test suite includes unit tests for the service layer and integration tests for the REST API (8 tests total).

## Project Structure

```
src/
├── main/
│   ├── java/org/surptech/banking/
│   │   ├── SurptechBankingApplication.java   # Entry point
│   │   ├── entity/                           # JPA entities
│   │   ├── repository/                       # Spring Data JPA repositories
│   │   ├── service/                          # Business logic
│   │   ├── dto/                              # Response objects
│   │   └── controller/                       # REST and web controllers
│   └── resources/
│       ├── application.properties            # Configuration
│       ├── data.sql                          # Seed data
│       └── templates/                        # Thymeleaf HTML pages
└── test/
    └── java/org/surptech/banking/
        ├── BankingInfoServiceTest.java        # Unit tests
        └── BankingApiControllerTest.java      # Integration tests
```

## Technology Stack

| Technology | Version |
|------------|---------|
| Java | 21 |
| Spring Boot | 3.3.5 |
| Spring Data JPA | - |
| Thymeleaf | - |
| H2 Database | - |
| JUnit 5 | - |
| Maven | 3.8+ |

---

MIT License - see [LICENSE](LICENSE)
