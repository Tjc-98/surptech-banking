# Customer Profile Service - Testing Guide

Comprehensive guide for testing the Customer Profile Service.

## Table of Contents

1. [Overview](#overview)
2. [Getting Started](#getting-started)
3. [Test Suites](#test-suites)
4. [Running Tests](#running-tests)
5. [Test Reports](#test-reports)
6. [CI/CD Integration](#cicd-integration)
7. [Writing New Tests](#writing-new-tests)
8. [Troubleshooting](#troubleshooting)

## Overview

The Customer Profile Tester is a comprehensive integration testing suite for the Customer Profile Service. It uses:

- **JUnit 5** for test framework
- **REST Assured** for API testing
- **Allure** for beautiful test reports
- **SurpTech Common Tester** for shared testing utilities

### Test Architecture

```
customer-profile-tester/
├── base/                    # Base test classes with common setup
├── client/                  # REST client for Customer Profile API
├── config/                  # Test configuration management
├── model/                   # Data models (request/response)
├── suite/                   # Test suite definitions
├── tests/                   # Actual test classes
└── teststep/               # Reusable test step implementations
```

## Getting Started

### Prerequisites

1. **Java 25** installed
2. **Maven 3.8+** installed
3. **Customer Profile Service** running on port 5551

### Initial Setup

```bash
# 1. Build shared common tester library
cd ../surptech-common-tester
mvn clean install

# 2. Navigate to customer-profile-tester
cd ../customer-profile-tester

# 3. Run tests
mvn clean test
```

### Verify Service is Running

```bash
# Check service health
curl http://localhost:5551/customer-profile/management/health

# Expected response:
# {"status":"UP"}
```

## Test Suites

### Smoke Tests (`smoke`)
Quick validation of critical functionality. Runs in ~10 seconds.

**Tests included:**
- Service health check
- Basic customer profile retrieval
- Response time validation
- Create customer profile

**When to use:**
- Before committing code
- Quick sanity checks
- Pre-deployment validation

```bash
mvn test -Dtest.suite=smoke
```

### Integration Tests (`integration`)
Comprehensive testing of all endpoints and scenarios. Runs in ~30 seconds.

**Tests included:**
- All smoke tests
- Data structure validation
- Field completeness checks
- Create and retrieve workflows
- Update existing profiles

**When to use:**
- Before merging to main branch
- Full regression testing
- Release validation

```bash
mvn test -Dtest.suite=integration
```

### Error Handling Tests (`error-handling`)
Tests error scenarios and edge cases. Runs in ~15 seconds.

**Tests included:**
- Non-existent customer (404)
- Invalid SSN formats
- Missing required fields
- Malformed requests

**When to use:**
- Testing error handling logic
- Validating API contracts
- Security testing

```bash
mvn test -Dtest.suite=error-handling
```

### Health Check Tests (`health`)
Service availability and health monitoring. Runs in ~5 seconds.

**Tests included:**
- Health endpoint availability
- Response structure validation

**When to use:**
- Monitoring service availability
- Pre-test environment validation
- CI/CD health gates

```bash
mvn test -Dtest.suite=health
```

## Running Tests

### Command Line

```bash
# Run all tests
mvn clean test

# Run specific suite
mvn clean test -Dtest.suite=smoke
mvn clean test -Dtest.suite=integration
mvn clean test -Dtest.suite=error-handling
mvn clean test -Dtest.suite=health

# Run with Maven profiles
mvn clean test -P smoke
mvn clean test -P integration

# Run specific test class
mvn test -Dtest=GetCustomerProfileTest

# Run specific test method
mvn test -Dtest=GetCustomerProfileTest#testGetCustomerProfile_ValidSSN_Success
```

### Shell Scripts

```bash
# Linux/Mac
./run-tests.sh smoke
./run-tests.sh integration
./run-tests.sh all

# Windows
run-tests.bat smoke
run-tests.bat integration
run-tests.bat all
```

### IDE (IntelliJ IDEA / Eclipse)

1. Right-click on test class or method
2. Select "Run" or "Debug"
3. View results in IDE test runner

## Test Reports

### Allure Reports (Recommended)

Allure provides beautiful, interactive test reports with:
- Test execution timeline
- Detailed step-by-step logs
- Request/response details
- Failure screenshots and logs
- Trend analysis

#### Generate and View

```bash
# Generate and open report in browser
mvn allure:serve

# Or generate static report
mvn allure:report
# Then open: target/allure-report/index.html
```

#### Report Features

- **Overview**: Summary of test execution
- **Suites**: Tests organized by suite
- **Graphs**: Visual representation of results
- **Timeline**: Execution timeline
- **Behaviors**: Tests organized by feature
- **Packages**: Tests organized by package

### Maven Surefire Reports

Standard Maven test reports in HTML format.

**Location:** `target/surefire-reports/`

**Files:**
- `TEST-*.xml` - JUnit XML reports
- `*.txt` - Text summaries
- HTML reports (after `mvn surefire-report:report`)

### Console Output

Real-time test execution logs with:
- Test names and descriptions
- Step-by-step execution
- Request/response details
- Assertions and validations
- Timing information

## CI/CD Integration

### GitHub Actions

#### Workflow File

`.github/workflows/customer-profile-tests.yml`

#### Manual Trigger

1. Go to **Actions** tab in GitHub
2. Select **"Run Customer Profile Tests"**
3. Click **"Run workflow"**
4. Select test suite from dropdown
5. Click **"Run workflow"** button

#### Features

- Automatic service startup
- Test execution with selected suite
- Allure report generation
- Artifact upload (reports retained 30 days)
- Service log capture

#### Downloading Reports

1. Go to **Actions** tab
2. Click on workflow run
3. Scroll to **Artifacts** section
4. Download `test-results-{suite}` artifact
5. Extract and open `allure-report/index.html`

### Jenkins

```groovy
pipeline {
    agent any
    
    stages {
        stage('Build Common Tester') {
            steps {
                dir('surptech-common-tester') {
                    sh 'mvn clean install -DskipTests'
                }
            }
        }
        
        stage('Start Service') {
            steps {
                dir('customer-profile') {
                    sh 'mvn clean package -DskipTests'
                    sh 'nohup java -jar target/customer-profile-*.jar &'
                    sh 'sleep 30'
                }
            }
        }
        
        stage('Run Tests') {
            steps {
                dir('customer-profile-tester') {
                    sh 'mvn clean test -Dtest.suite=smoke'
                }
            }
        }
        
        stage('Generate Report') {
            steps {
                dir('customer-profile-tester') {
                    sh 'mvn allure:report'
                }
            }
        }
    }
    
    post {
        always {
            allure includeProperties: false,
                   jdk: '',
                   results: [[path: 'customer-profile-tester/target/allure-results']]
        }
    }
}
```

### GitLab CI

```yaml
test:
  stage: test
  script:
    - cd surptech-common-tester && mvn clean install -DskipTests
    - cd ../customer-profile && mvn clean package -DskipTests
    - nohup java -jar target/customer-profile-*.jar &
    - sleep 30
    - cd ../customer-profile-tester && mvn clean test -Dtest.suite=smoke
    - mvn allure:report
  artifacts:
    paths:
      - customer-profile-tester/target/allure-report/
      - customer-profile-tester/target/surefire-reports/
    expire_in: 30 days
```

## Writing New Tests

### Test Class Structure

```java
@Slf4j
@DisplayName("My Feature Tests")
public class MyFeatureTest extends BaseCustomerProfileTest {
    
    @Test
    @TestId("MFT-01")
    @Tag("smoke")
    @Tag("integration")
    @DisplayName("MFT-01: Test description")
    public void testMyFeature() {
        // Use test step builders
        getCustomerProfile()
                .withSsn("123-45-6789")
                .expectStatusCode(200)
                .verifyResponseNotNull()
                .run();
    }
}
```

### Creating Test Steps

```java
public class MyTestStep extends TestStep<MyTestStep> {
    
    private final CustomerProfileClient client;
    private String parameter;
    
    public MyTestStep(CustomerProfileClient client) {
        this.client = client;
    }
    
    public MyTestStep withParameter(String parameter) {
        this.parameter = parameter;
        return this;
    }
    
    @Override
    protected Response execute() {
        return client.myApiCall(parameter);
    }
    
    @Override
    protected void verify() {
        assertNotNull(response);
        assertEquals(200, response.getStatusCode());
    }
    
    @Override
    protected String getDescription() {
        return "My test step with parameter: " + parameter;
    }
}
```

### Best Practices

1. **Use descriptive test IDs** - Format: `{FEATURE}-{NUMBER}` (e.g., `GCPT-01`)
2. **Add meaningful display names** - Include test ID and description
3. **Tag appropriately** - Use `@Tag` for suite organization
4. **Use test steps** - Encapsulate API calls in reusable test steps
5. **Verify thoroughly** - Check status codes, response structure, and data
6. **Log clearly** - Use descriptive messages for debugging
7. **Keep tests independent** - Each test should run standalone
8. **Clean up after tests** - Remove test data if needed

## Troubleshooting

### Service Not Running

**Problem:** Tests fail with connection refused

**Solution:**
```bash
# Check if service is running
curl http://localhost:5551/customer-profile/management/health

# Start the service
cd customer-profile
mvn spring-boot:run
```

### Port Already in Use

**Problem:** Service fails to start on port 5551

**Solution:**
```bash
# Find process using port
netstat -ano | findstr :5551  # Windows
lsof -i :5551                 # Linux/Mac

# Kill the process or change port in application.properties
```

### Tests Pass Locally but Fail in CI

**Problem:** Tests work on local machine but fail in CI/CD

**Possible causes:**
1. Service not fully started (increase wait time)
2. Different test data in CI environment
3. Network/firewall issues
4. Resource constraints (memory, CPU)

**Solution:**
- Add health check polling before tests
- Use consistent test data
- Check CI logs for service startup errors

### Allure Report Not Generating

**Problem:** `mvn allure:serve` fails

**Solution:**
```bash
# Install Allure command-line tool
# Or use the bundled version in .allure/

# Generate report manually
mvn allure:report
# Open target/allure-report/index.html
```

### Common Tester Dependency Not Found

**Problem:** Build fails with missing surptech-common-tester

**Solution:**
```bash
# Build and install common tester first
cd ../surptech-common-tester
mvn clean install
cd ../customer-profile-tester
mvn clean test
```

## Additional Resources

- [JUnit 5 Documentation](https://junit.org/junit5/docs/current/user-guide/)
- [REST Assured Documentation](https://rest-assured.io/)
- [Allure Documentation](https://docs.qameta.io/allure/)
- [Maven Surefire Plugin](https://maven.apache.org/surefire/maven-surefire-plugin/)

## Support

For issues or questions:
1. Check this guide
2. Review test logs in `target/surefire-reports/`
3. Check service logs
4. Contact the development team

---

**Last Updated:** 2024
**Version:** 1.0.0
