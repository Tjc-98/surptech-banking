# surptech-banking-tester

Integration test suite for the SurpTech Banking System. Tests are written against the `data-aggregator` service and validate the full end-to-end flow from the API gateway through to the downstream data services.

## Purpose

Provides automated integration tests that verify the `data-aggregator` API behaves correctly under normal conditions, error conditions, and edge cases. Tests are organized into tagged suites so they can be run selectively in CI or locally.

## Prerequisites

All three backend services must be running before executing tests:

- `customer-profile` on port `5551`
- `credit-profile` on port `5552`
- `data-aggregator` on port `5555`

See the [root README](../README.md) for instructions on starting the services.

## Test Suites

Tests are tagged with JUnit 5 `@Tag` annotations and grouped into Maven profiles.

| Suite | Tag | Profile | Description |
|---|---|---|---|
| Smoke | `smoke` | `smoke` | Quick sanity checks — verifies the service is reachable and returns expected data for a known SSN |
| Integration | `integration` | `integration` | Full happy-path scenarios covering the aggregated response structure |
| Error Handling | `error-handling` | `error-handling` | Validates correct HTTP status codes and error response bodies for invalid inputs |
| Health | `health` | `health` | Verifies the `/services/management/health` endpoint reports downstream service status |
| CI | — | `ci` | Runs `smoke` + `integration` in parallel (4 threads) for CI pipelines |

## Running Tests

```bash
# Build the shared testing library first
cd ../surptech-common-tester
mvn clean install

# Run all suites (default)
cd ../surptech-banking-tester
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
| `data.aggregator.base.url` | `http://localhost:5555/data-aggregator` | Target service URL |
| `test.data.valid.ssn` | `123-45-6789` | SSN with both customer and credit profiles seeded |
| `test.data.invalid.ssn` | `999-99-9999` | SSN with no data — expects 404 |
| `test.expected.firstName` | `James` | Expected first name for valid SSN |
| `test.expected.lastName` | `Smith` | Expected last name for valid SSN |
| `test.expected.address` | `456 Tailor Street, California, LA 56001` | Expected address for valid SSN |

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

The GitHub Actions workflow `.github/workflows/run-tests.yml` can be triggered manually from the Actions tab. It:

1. Builds `surptech-common` and `surptech-common-tester`
2. Packages and starts all three backend services
3. Waits for each service health check to pass
4. Runs the selected test suite
5. Uploads Allure results and Surefire XML as artifacts (retained 30 days)
6. Publishes a test summary to the GitHub PR/commit via `dorny/test-reporter`

Available suite options in the workflow: `all`, `smoke`, `integration`, `error-handling`, `health`, `ci`.

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
