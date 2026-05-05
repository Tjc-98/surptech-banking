# SurpTech Banking System - Integration Tester

Automated integration testing suite for the SurpTech Banking System.

## Overview

This project provides comprehensive integration testing for the SurpTech Banking System, focusing on testing the Data Aggregator gateway service and its interactions with backend services (Customer Profile and Credit Profile).

## Architecture

The tester interacts with the system as shown in the architecture diagram:

```
Tester → Data Aggregator → [Customer Profile, Credit Profile]
```

## Project Structure

```
surptech-banking-tester/
├── src/test/
│   ├── java/org/surptech/bankingtester/
│   │   ├── annotation/          # Custom test annotations (@TestId)
│   │   ├── base/                # Base test classes
│   │   ├── client/              # REST clients for services
│   │   ├── config/              # Test configuration
│   │   ├── model/               # Data models
│   │   ├── suite/               # Test suites
│   │   └── tests/               # Test classes
│   └── resources/
│       ├── application.properties  # Test configuration
│       └── logback-test.xml       # Logging configuration
├── pom.xml
└── README.md
```

## Test Categories

Tests are organized with JUnit 5 tags:

- **`@Tag("smoke")`** - Quick smoke tests for basic functionality
- **`@Tag("integration")`** - Full integration tests
- **`@Tag("error-handling")`** - Error scenario tests
- **`@Tag("health")`** - Service health checks

## Test IDs

Each test has a unique ID using the `@TestId` annotation:

- **TC-001 to TC-099**: Integration tests
- **TC-101 to TC-199**: Error handling tests
- **TC-201 to TC-299**: Health check tests

## Running Tests

### Prerequisites

1. Ensure all services are running:
   - Data Aggregator (port 5555, context path /data-aggregator)
   - Customer Profile (port 5551)
   - Credit Profile (port 5552)

2. Java 25 and Maven installed

### Run All Tests

```bash
mvn clean test
```

### Run Specific Test Suites

```bash
# Smoke tests only
mvn clean test -P smoke

# Integration tests only
mvn clean test -P integration

# Error handling tests only
mvn clean test -P error-handling

# CI profile (parallel execution)
mvn clean test -P ci
```

### Run from IntelliJ IDEA

1. Open the project in IntelliJ
2. Navigate to any test class or suite
3. Right-click and select "Run" or "Debug"
4. Or use the green play button next to test methods

### Run Specific Test Class

```bash
mvn test -Dtest=CustomerBankingInfoTest
```

### Run Specific Test Method

```bash
mvn test -Dtest=CustomerBankingInfoTest#testGetCustomerBankingInfo_ValidSSN_Success
```

## Test Reports

### Console Output

Tests provide detailed console output with:
- Test execution progress
- Test IDs and names
- Detailed logging of requests/responses
- Summary of passed/failed tests

### HTML Reports

After running tests, HTML reports are generated in:

```
target/surefire-reports/
├── index.html                    # Main report index
├── TEST-*.xml                    # JUnit XML reports
└── org.surptech.bankingtester.tests.*.txt  # Text reports
```

To view the HTML report:

```bash
# Generate the site report
mvn surefire-report:report

# Open target/site/surefire-report.html in a browser
```

### CI/CD Integration

The test reports are in standard JUnit XML format, compatible with:
- Jenkins
- GitLab CI
- GitHub Actions
- Azure DevOps
- Any CI/CD tool supporting JUnit reports

## Configuration

Edit `src/test/resources/application.properties` to configure:

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

## Test Data

### Valid Test Case
- **SSN**: 123-45-6789
- **Expected Customer**:
  - First Name: James
  - Last Name: Smith
  - Address: 456 Tailor Street, California, LA 56001

### Invalid Test Cases
- Invalid SSN: 999-99-9999
- Null SSN
- Empty SSN
- Malformed SSN

## Technologies Used

- **JUnit 5** - Testing framework
- **REST Assured** - REST API testing
- **Lombok** - Reduce boilerplate code
- **Jackson** - JSON processing
- **SLF4J + Logback** - Logging
- **Maven Surefire** - Test execution and reporting

## Adding New Tests

1. Create a new test class in `src/test/java/org/surptech/bankingtester/tests/`
2. Extend `BaseTest`
3. Add `@TestId` annotation with unique ID
4. Add appropriate `@Tag` annotations
5. Add `@DisplayName` for readable test names
6. Implement test logic using REST Assured

Example:

```java
@Test
@TestId("TC-005")
@Tag("integration")
@DisplayName("TC-005: Test description")
public void testSomething() {
    // Arrange
    // Act
    // Assert
}
```

## Troubleshooting

### Services Not Available

If tests fail with connection errors:
1. Verify all services are running
2. Check service URLs in `application.properties`
3. Verify ports are not blocked by firewall

### Test Failures

1. Check the console output for detailed error messages
2. Review the HTML report in `target/site/surefire-report.html`
3. Check logs in `target/test-logs/test-execution.log`

## Future Enhancements

- Add authentication/authorization tests
- Add performance/load testing
- Add data-driven tests with multiple test data sets
- Add API contract testing
- Add test data management utilities
