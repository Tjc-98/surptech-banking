# SurpTech Banking System - Integration Tester

Automated integration testing suite for the SurpTech Banking System.

## Overview

This project provides comprehensive integration testing for the SurpTech Banking System, focusing on testing the Data Aggregator gateway service and its interactions with backend services (Customer Profile and Credit Profile).

**Built with:** [surptech-common-tester](../surptech-common-tester) - Shared testing utilities and base classes

## Quick Start

### Prerequisites

1. Build the common tester library:
   ```bash
   cd ../surptech-common-tester
   mvn clean install
   ```

2. Ensure all services are running:
   - Data Aggregator (port 5555, context path /data-aggregator)
   - Customer Profile (port 5551)
   - Credit Profile (port 5552)

3. Java 25 and Maven installed

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
| **integration** | `-Dtest.suite=integration` | Full integration tests (~10 tests) |
| **error-handling** | `-Dtest.suite=error-handling` | Error scenario tests (~8 tests) |
| **health** | `-Dtest.suite=health` | Service health checks (~3 tests) |
| **all** | `mvn test` | All tests (default) |
| **ci** | `-P ci` | CI/CD optimized (parallel) |

## Project Structure

```
surptech-banking-tester/
├── src/test/java/org/surptech/bankingtester/
│   ├── annotation/          # Custom test annotations (@TestId)
│   ├── base/                # Base test classes
│   ├── client/              # REST clients for services
│   ├── config/              # Test configuration
│   ├── model/               # Data models
│   ├── suite/               # Test suites
│   └── tests/               # Test classes
├── src/test/resources/
│   ├── application.properties  # Test configuration
│   └── logback-test.xml       # Logging configuration
├── pom.xml
├── README.md
├── TESTING-GUIDE.md         # Comprehensive testing guide
└── run-tests.sh/bat         # Test runner scripts
```

## CI/CD Integration

### GitHub Actions

A workflow is provided in `.github/workflows/run-tests.yml`:

**Manual Trigger Only:**
1. Go to Actions tab → "Run Integration Tests"
2. Click "Run workflow"
3. Select test suite from dropdown
4. Click "Run workflow" button

**Features:**
- Builds and starts all services automatically
- Runs selected test suite
- Generates and uploads test reports as artifacts
- Reports retained for 30 days

### Other CI/CD Platforms

```bash
# Jenkins, GitLab CI, Azure DevOps, etc.
mvn clean test -Dtest.suite=smoke
```

See [TESTING-GUIDE.md](TESTING-GUIDE.md) for detailed CI/CD examples.

## Test Reports

### Local Development

```bash
# View Allure report (recommended)
mvn allure:serve

# Or generate static report
mvn allure:report
open target/allure-report/index.html
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
data.aggregator.base.url=http://localhost:5555/data-aggregator

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

## Documentation

- **[TESTING-GUIDE.md](TESTING-GUIDE.md)** - Comprehensive testing guide with CI/CD examples
- **[README.md](README.md)** - This file (quick start)

## Troubleshooting

### Services Not Available

```bash
# Check if services are running
curl http://localhost:5551/actuator/health
curl http://localhost:5552/actuator/health
curl http://localhost:5555/data-aggregator/actuator/health
```

### Clean Build

```bash
# Clean and rebuild
mvn clean test
```

### Common Tester Dependency Not Found

```bash
# Build common tester first
cd ../surptech-common-tester
mvn clean install
cd ../surptech-banking-tester
mvn clean test
```

For more help, see [TESTING-GUIDE.md](TESTING-GUIDE.md).
