# customer-profile-tester

Integration test suite for the Customer Profile service. Tests are written against the `customer-profile` service directly and validate both the GET and POST endpoints under normal conditions, error conditions, and edge cases.

## Purpose

Provides automated integration tests that verify the `customer-profile` API behaves correctly in isolation. Tests are organized into tagged suites so they can be run selectively in CI or locally.

## Prerequisites

The `customer-profile` service must be running before executing tests:

- `customer-profile` on port `5551`

See the [root README](../README.md) for instructions on starting the service.

## Test Suites

Tests are tagged with JUnit 5 `@Tag` annotations and grouped into Maven profiles.

| Suite | Tag | Profile | Description |
|---|---|---|---|
| Smoke | `smoke` | `smoke` | Quick sanity checks — verifies the service is reachable and returns expected data for a known SSN |
| Integration | `integration` | `integration` | Full happy-path scenarios for GET and POST endpoints |
| Error Handling | `error-handling` | `error-handling` | Validates correct HTTP status codes and error bodies for invalid inputs |
| Health | `health` | `health` | Verifies the `/management/health` endpoint responds correctly |
| CI | — | `ci` | Runs `smoke` + `integration` in parallel (4 threads) for CI pipelines |

## Test Classes

| Class | Test IDs | Description |
|---|---|---|
| `ServiceHealthTest` | — | Verifies the health endpoint returns `UP` |
| `GetCustomerProfileTest` | GCPT-01 to GCPT-05 | Retrieval scenarios: valid SSN, not found, invalid format |
| `CreateCustomerProfileTest` | CCPT-01 to CCPT-03 | Creation scenarios: new profile, duplicate SSN (upsert), invalid request |

## Running Tests

```bash
# Build the shared testing library first
cd ../surptech-common-tester
mvn clean install

# Run all suites (default)
cd ../customer-profile-tester
mvn clean test

# Run a specific suite
mvn clean test -P smoke
mvn clean test -P integration
mvn clean test -P error-handling
mvn clean test -P health
mvn clean test -P ci
```

## Test Data

Test data is configured in `src/test/resources/application.properties`.

| Property | Value | Description |
|---|---|---|
| `customer.profile.base.url` | `http://localhost:5551/customer-profile` | Target service URL |
| `test.data.valid.ssn` | `123-45-6789` | SSN with a seeded profile — expects James Smith |
| `test.data.invalid.ssn` | `999-99-9999` | SSN with no data — expects 404 |
| `test.data.new.ssn` | `111-22-3333` | SSN used for creation tests |
| `test.create.ssn` | `555-66-7777` | SSN for POST create tests |
| `test.create.firstName` | `John` | First name for create test data |
| `test.create.lastName` | `Doe` | Last name for create test data |
| `test.create.address` | `123 Main Street, New York, NY 10001` | Address for create test data |

## Test Reports

### Allure Report (recommended)

```bash
mvn allure:serve
```

Opens an interactive Allure report in the browser with test steps, request/response details, and history.

### Surefire HTML Report

```bash
# Report is generated automatically after mvn test
# Open target/site/surefire-report.html
```

### Raw XML Results

JUnit XML reports are written to `target/surefire-reports/` and are compatible with most CI systems.

## CI/CD

The GitHub Actions workflow `.github/workflows/customer-profile-tests.yml` can be triggered manually from the Actions tab. It:

1. Builds `surptech-common` and `surptech-common-tester`
2. Packages and starts the `customer-profile` service
3. Waits for the health check to pass
4. Runs the selected test suite
5. Uploads Allure results and Surefire XML as artifacts (retained 30 days)

Available suite options in the workflow: `smoke`, `integration`, `error-handling`, `health`.

## Technology Stack

| Technology | Version |
|---|---|
| Java | 25 |
| JUnit 5 (Jupiter) | 6.0.3 |
| REST Assured | 5.5.0 |
| Allure | 2.27.0 |
| Maven Surefire | 3.5.2 |
| Logback | 1.5.15 |
| surptech-common-tester | 1.0.0-SNAPSHOT |
