# SurpTech Common Tester

Common testing utilities and base classes for SurpTech test suites.

## Overview

This library provides shared testing infrastructure used across all SurpTech testing projects. It includes:

- **Base Test Classes** - Common test lifecycle management and logging
- **Test Step Framework** - Builder pattern for API test steps
- **Configuration Management** - Base configuration loader
- **Custom Annotations** - Test ID tracking
- **Listeners** - Allure integration and custom reporting

## Usage

### Maven Dependency

Add this dependency to your test project's `pom.xml`:

```xml
<dependency>
    <groupId>org.surptech</groupId>
    <artifactId>surptech-common-tester</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <scope>test</scope>
</dependency>
```

### Extending Base Classes

```java
// Extend BaseTest for test lifecycle management
public class MyTest extends BaseTest {
    
    @BeforeAll
    public static void setup() {
        initializeTestResources();
        // Your setup code
    }
    
    @Test
    @TestId("MY-01")
    @DisplayName("My test case")
    public void testSomething() {
        logStep("Performing test step");
        // Your test code
    }
}
```

### Creating Test Steps

```java
// Extend TestStep for API test steps
public class MyTestStep extends TestStep<MyTestStep> {
    
    private final MyClient client;
    private String param;
    
    public MyTestStep(MyClient client) {
        this.client = client;
    }
    
    public MyTestStep withParam(String param) {
        this.param = param;
        return this;
    }
    
    @Override
    protected Response execute() {
        return client.callApi(param);
    }
    
    @Override
    protected void verify() {
        // Add assertions
    }
    
    @Override
    protected String getDescription() {
        return "Calling API with param: " + param;
    }
}
```

### Configuration

```java
// Extend BaseTestConfiguration
public class MyTestConfiguration extends BaseTestConfiguration {
    
    private static MyTestConfiguration instance;
    
    public static MyTestConfiguration getInstance() {
        if (instance == null) {
            instance = new MyTestConfiguration();
        }
        return instance;
    }
    
    public String getServiceUrl() {
        return getProperty("service.url");
    }
}
```

## Components

### Base Classes

- `BaseTest` - Base test class with lifecycle management
- `TestLifecycleManager` - Test lifecycle hooks and logging
- `BaseTestConfiguration` - Configuration loader

### Test Step Framework

- `TestStep<T>` - Abstract base for test steps with builder pattern

### Annotations

- `@TestId` - Mark tests with unique identifiers

### Listeners

- `AllureLogAppender` - Forward logs to Allure reports
- `CustomTestReportListener` - Enhanced test reporting

## Building

```bash
# Install to local Maven repository
mvn clean install

# Skip tests (if any)
mvn clean install -DskipTests
```

## Technologies

- **JUnit 5** - Testing framework
- **REST Assured** - REST API testing
- **Allure** - Test reporting
- **Lombok** - Reduce boilerplate
- **Jackson** - JSON processing
- **SLF4J + Logback** - Logging

## License

Copyright © 2024 SurpTech
