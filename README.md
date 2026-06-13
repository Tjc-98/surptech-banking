# SurpTech Banking

A full-stack banking application where you can look up customer accounts, view credit balances, and record transactions.

---

## What it does

The backend is a Spring Boot REST API that stores customer profiles, credit accounts, and transaction history in PostgreSQL. The frontend is a Next.js app where you can search for a customer by SSN, see their account details, add a deposit or withdrawal, and register new customers - all without leaving the page.

## Components

| Component | Tech | Port |
|-----------|------|------|
| [backend](backend/) | Java 25, Spring Boot 4.0.6, PostgreSQL | 8080 |
| [frontend](frontend/) | TypeScript, Next.js 16.2.6, React 19 | 3000 |

## Getting started

### What you need

- Java 25
- Maven 3.8+
- Node.js 22
- npm
- Docker Desktop (recommended) - or a local PostgreSQL 16 install

### 1. Start the database

The easiest way is Docker - one command and you're done:

```
docker compose up -d
```

That starts a PostgreSQL 16 container on port `5432` with a database called `surptechdb`. Data persists between restarts thanks to a named volume. To stop it later: `docker compose down`.

If you'd rather not use Docker, create the database manually in your local PostgreSQL:

```sql
CREATE DATABASE surptechdb;
```

Either way, the backend uses these defaults. Override them with environment variables if your setup is different:

| Variable | Default |
|----------|---------|
| `DB_URL` | `jdbc:postgresql://localhost:5432/surptechdb` |
| `DB_USERNAME` | `postgres` |
| `DB_PASSWORD` | `postgres` |

### 2. Start the backend

```
cd backend
mvn spring-boot:run
```

The API runs at `http://localhost:8080`. Hibernate creates the tables automatically on first run and the seed data is loaded straight after.

### 3. Start the frontend

```
cd frontend
npm install
npm run dev
```

Open `http://localhost:3000` and you're good to go.

## API endpoints

| Method | Endpoint | What it does |
|--------|----------|--------------|
| GET | `/api/customer/info?socialSecurityNumber=XXX` | Fetches a customer's full profile, credit account, and transaction history |
| POST | `/api/customer/create` | Registers a new customer (or updates an existing one) |
| POST | `/api/credit/create` | Creates or updates a credit account |
| POST | `/api/transaction/add` | Records a deposit or withdrawal |
| GET | `/api/transaction/history?socialSecurityNumber=XXX` | Returns transaction history, newest first |

All endpoints validate their inputs. If something's wrong, you'll get back a JSON `{ "error": "..." }` with a plain-English message explaining what's missing.

## Seed data

Two customers are loaded automatically when the app starts. Use these to try it out without creating anything first.

| SSN | Name | Credit limit | Interest rate |
|-----|------|-------------|---------------|
| `123-45-6789` | James Smith | $15,000 | 3.5% |
| `987-65-4321` | John Travolta | $28,000 | 8.5% |

Both already have a few transactions in their history.

## Running the tests

### Backend

No PostgreSQL needed - tests run against an in-memory H2 database via the `test` profile.

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

Two GitHub Actions workflows run automatically on every push.

| Workflow | Runs when | What it does |
|----------|-----------|--------------|
| CI | Every push and pull request | Runs backend and frontend tests in parallel and reports results |
| CD | Push to `main` | Builds a self-contained backend JAR and a standalone Next.js bundle, uploads both as downloadable artifacts |

To run the backend artifact: `java -jar surptech-banking-1.0.0.jar`
To run the frontend artifact: `node server.js`

## Project structure

```
surptech-banking/
├── docker-compose.yml          # spins up the PostgreSQL database
├── backend/                    # Spring Boot application
│   ├── src/main/java/          # controllers, services, entities, repositories
│   ├── src/main/resources/     # application.properties and seed data
│   ├── src/test/               # unit and integration tests
│   └── pom.xml
├── frontend/                   # Next.js application
│   ├── app/                    # pages and layout
│   ├── __tests__/              # component tests
│   └── package.json
└── .github/workflows/          # CI and CD pipelines
```

---

MIT License - see [LICENSE](LICENSE)
