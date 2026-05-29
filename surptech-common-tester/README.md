# surptech-common-tester

Shared testing library that provides base classes, utilities, and infrastructure for all SurpTech integration test suites. It is a required dependency for `surptech-banking-tester` and `customer-profile-tester`.

## Purpose

Eliminates duplication across test suites by centralizing test lifecycle management, step execution, configuration loading, and Allure reporting integration. Test suites depend on this library and extend its base classes rather than re-implementing boilerplate.

## Key Components

### Base Classes

| Class | Description |
|---|---|
| `BaseTest` | Extends `TestLifecycleManager`. Entry point for all test classes. Provides `initializeTestResources()` and inherits lifecycle hooks. |
| `TestLifecycleManager` | Manages `@BeforeEach` / `@AfterEach` hooks. Outputs formatted box-drawing log banners around each test. Provides `logStep()` which writes to both SLF4J and `Allure.step()` simultaneously. |
| `TestStep<T>` | Abstract builder-pattern base for reusable test steps. Subclasses implement `execute()` (send the request), `verify()` (assert the response), and `getDescription()`. The `run()` method orchestrates all three with logging. |

### Configuration

`BaseTestConfiguration` loads `application.properties` from the test classpath and exposes:

- `getProperty(key)` — returns a string property value
- `getIntProperty(key)` — returns an integer property value

Each test suite provides its own `application.properties` with service URLs and test data values.

### Annotations

`@TestId` — a `@Target(METHOD)` / `@Retention(RUNTIME)` annotation for attaching traceability IDs to test methods.

### Reporting

`AllureLogAppender` and `CustomTestReportListener` integrate with Allure to capture log output and test lifecycle events in the generated report.

## Technology Stack

| Technology | Version |
|---|---|
| Java | 25 |
| JUnit 5 (Jupiter) | 6.0.3 |
| JUnit Platform Suite | 6.0.3 |
| REST Assured | 5.5.0 |
| Allure JUnit 5 | 2.27.0 |
| Allure REST Assured | 2.27.0 |
| Jackson | 2.18.2 |
| Logback | 1.5.15 |
| Lombok | 1.18.46 |

## Building

This library must be installed to the local Maven repository before building any dependent test suite.

```bash
cd surptech-common-tester
mvn clean install
```

## Usage in Test Suites

Add the dependency to the test suite's `pom.xml`:

```xml
<dependency>
    <groupId>org.surptech</groupId>
    <artifactId>surptech-common-tester</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <scope>test</scope>
</dependency>
```

Extend `BaseTest` in your test classes and `TestStep<T>` for reusable step implementations.
