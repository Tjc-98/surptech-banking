# Customer Profile Service - Integration Tester

Automated integration testing suite for the Customer Profile Service.

## Overview

This project provides comprehensive integration testing for the Customer Profile Service, focusing on testing customer profile retrieval and creation endpoints.

## Quick Start

### Prerequisites

1. Ensure Customer Profile service is running:
   - Customer Profile (port 5551, context path /customer-profile)

2. Java 25 and Maven installed

### Run Tests

```bash
# Run all tests
mvn clean test

# Run specific suite
mvn clean test -Dtest.suite=smoke
mvn clean test -Dtest.suite=integration

# Using profiles (alternative)
mvn clean test -P smoke

# Using shell scripts
./run-tests.sh smoke              # Linux/Mac
run-tests.bat smoke               # Windows
```

## Test Suites

| Suite | Command | Description |
|-------|---------|-------------|
| **smoke** | `-Dtest.suite=smoke` | Quick smoke tests (~5 tests) |
| **integration** | `-Dtest.suite=integration` | Full integration tests (~8 tests) |
| **error-handling** | `-Dtest.suite=error-handling` | Error scenario tests (~2 tests) |
| **health** | `-Dtest.suite=health` | Service health checks (~2 tests) |
| **all** | `mvn test` | All tests (default) |
| **ci** | `-P ci` | CI/CD optimized (parallel) |

## Project Structure

```
customer-profile-tester/
├── src/test/java/org/surptech/customerprofiletester/
│   ├── base/                # Base test classes
│   ├── client/              # REST client for Customer Profile service
│   ├── config/              # Test configuration
│   ├── model/               # Data models
│   ├── suite/               # Test suites
│   ├── tests/               # Test classes
│   ├── teststep/            # Test step implementations
│   └── TestRunner.java      # Command-line test runner
├── src/test/resources/
│   ├── application.properties  # Test configuration
│   └── logback-test.xml       # Logging configuration
├── pom.xml
└── README.md
```

## Test Coverage

### Service Health Tests
- **CPH-01**: Verify service health endpoint
- **CPH-02**: Verify health response structure

### Get Customer Profile Tests
- **GCPT-01**: Retrieve customer profile with valid SSN
- **GCPT-02**: Validate data structure and completeness
- **GCPT-03**: Verify response time performance
- **GCPT-04**: Handle non-existent customer (404)
- **GCPT-05**: Verify all fields are populated correctly

### Create Customer Profile Tests
- **CCPT-01**: Create new customer profile successfully
- **CCPT-02**: Create and then retrieve profile
- **CCPT-03**: Update existing customer profile

## CI/CD Integration

### GitHub Actions

A workflow is provided in `.github/workflows/customer-profile-tests.yml`:

**Manual Trigger Only:**
1. Go to Actions tab → "Run Customer Profile Tests"
2. Click "Run workflow"
3. Select test suite from dropdown
4. Click "Run workflow" button

**Features:**
- Builds and starts Customer Profile service automatically
- Runs selected test suite
- Generates and uploads test reports as artifacts
- Reports retained for 30 days

### Other CI/CD Platforms

```bash
# Jenkins, GitLab CI, Azure DevOps, etc.
mvn clean test -Dtest.suite=smoke
```

## Test Reports

### Local Development

```bash
# View Allure report (recommended)
mvn allure:serve

# Or generate static report
mvn allure:report
# Open target/allure-report/index.html in browser
```

### GitHub Actions

Reports are uploaded as workflow artifacts:
1. Go to Actions tab → workflow run
2. Download artifacts from "Artifacts" section
3. Extract and open `allure-report/index.html`

**Important:** Reports are NOT committed to the repository. They are:
- Generated in `target/` directory (excluded by `.gitignore`)
- Uploaded as artifacts in CI/CD systems
- Automatically cleaned by `mvn clean`

## Configuration

Edit `src/test/resources/application.properties`:

```properties
# Service URLs
customer.profile.base.url=http://localhost:5551/customer-profile

# Test Data
test.data.valid.ssn=123-45-6789
test.data.invalid.ssn=999-99-9999

# Expected Results
test.expected.firstName=James
test.expected.lastName=Smith
test.expected.address=456 Tailor Street, California, LA 56001
```

## Technologies

- **JUnit 5** - Testing framework
- **REST Assured** - REST API testing
- **Allure** - Test reporting
- **Lombok** - Reduce boilerplate
- **Jackson** - JSON processing
- **SLF4J + Logback** - Logging
- **Maven Surefire** - Test execution
- **SurpTech Common Tester** - Shared testing utilities

## Troubleshooting

### Service Not Available

```bash
# Check if service is running
curl http://localhost:5551/customer-profile/management/health
```

### Clean Build

```bash
# Clean and rebuild
mvn clean test
```

## Dependencies

This project depends on:
- **surptech-common-tester** - Shared testing utilities and base classes

Make sure to build the common tester first:

```bash
cd ../surptech-common-tester
mvn clean install
cd ../customer-profile-tester
```

## License

Copyright © 2024 SurpTech
