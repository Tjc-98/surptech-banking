# SurpTech Banking

A simple banking information lookup application with a Spring Boot backend and a Next.js frontend.

---

## About

Written in Java and TypeScript, this project lets users look up customer personal and credit information by Social Security Number. The backend exposes a REST API backed by an H2 in-memory database. The frontend is a Next.js application that calls the backend directly from the browser.

## Components

| Component | Tech | Port | Description |
|-----------|------|------|-------------|
| [backend](backend/) | Java 21, Spring Boot 3.3.5 | 8080 | REST API. Stores and serves customer and credit profiles. |
| [frontend](frontend/) | TypeScript, Next.js 14 | 3000 | Web UI. Lets users search for customer information by SSN. |

## Getting Started

### Prerequisites

- Java 21 or later
- Maven 3.8 or later
- Node.js 20 or later
- npm 9 or later

### 1. Start the backend

```
cd backend
mvn spring-boot:run
```

The API will be available at `http://localhost:8080`.

### 2. Start the frontend

```
cd frontend
npm install
npm run dev
```

Open `http://localhost:3000` in your browser.

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/customer/info?socialSecurityNumber=XXX` | Returns combined customer and credit info |
| POST | `/api/customer/create` | Creates or updates a customer profile |
| POST | `/api/credit/create` | Creates or updates a credit profile |

## Seed Data

Two records are loaded on startup.

| SSN | Name | Credit Balance | Interest Rate |
|-----|------|----------------|---------------|
| `123-45-6789` | James Smith | $15,000 | 3.5% |
| `987-65-4321` | John Travolta | $28,000 | 8.5% |

The H2 console is available at `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:surptechdb`).

## Running Tests

### Backend

```
cd backend
mvn clean test
```

### Frontend

```
cd frontend
npm test
```

## CI/CD

Two GitHub Actions workflows run automatically.

| Workflow | Trigger | What it does |
|----------|---------|--------------|
| CI | Every push and pull request | Runs backend unit tests and frontend unit tests in parallel |
| CD | Push to `main` | Builds the backend JAR and the Next.js production bundle, uploads both as artifacts |

## Project Structure

```
surptech-banking/
├── backend/                    # Spring Boot application
│   ├── src/main/java/          # Source code
│   ├── src/main/resources/     # application.properties, data.sql
│   ├── src/test/java/          # Unit and integration tests
│   └── pom.xml
├── frontend/                   # Next.js application
│   ├── app/                    # Next.js App Router pages
│   ├── __tests__/              # Frontend unit tests
│   ├── package.json
│   └── tsconfig.json
└── .github/workflows/          # CI and CD workflows
```

---

MIT License - see [LICENSE](LICENSE)
