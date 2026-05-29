# surptech-common

Shared framework library that provides common infrastructure for all SurpTech backend services. It is distributed as a Spring Boot auto-configured JAR and is a required dependency for `customer-profile`, `credit-profile`, and `data-aggregator`.

## Purpose

Centralizes cross-cutting concerns so individual services stay focused on their own business logic. This includes HTTP client construction, request/response logging, exception handling, input validation, and the base classes that define the service's architectural patterns.

## Key Components

### Base Classes

| Class | Description |
|---|---|
| `BaseController` | Extends Spring `@RestController`. Provides `executeProcedure()` which runs a `BaseProcedure` and logs the request path and execution duration. |
| `BaseProcedure<REQUEST, RESPONSE>` | Template method base for all business logic. `execute()` wraps `executeProcedure()` with entry/exit logging and exception handling. Subclasses implement `executeProcedure()`. |
| `BaseRequest` | Abstract base for all request DTOs. Declares a `validate()` hook and is annotated with `@JsonIgnoreProperties(ignoreUnknown=true)`. |
| `BaseResponse` | Abstract base for all response DTOs. Automatically sets a `timestamp` field on construction. |

### HTTP Client

| Class | Description |
|---|---|
| `RestClientBuilder` | Fluent builder for Spring `RestClient`. Configures connect timeout (default 5s), read timeout (default 10s), a logging interceptor, and validates the base URL before building. |
| `RestClientLoggingInterceptor` | Logs HTTP method, URI, response status, and duration for every outbound call made through a `RestClient`. |

### Exception Handling

`GlobalExceptionHandler` (`@RestControllerAdvice`) catches exceptions thrown anywhere in a service and maps them to a standardized `ErrorResponse` JSON body.

| Exception | HTTP Status |
|---|---|
| `ResourceNotFoundException` | 404 Not Found |
| `ValidationException` | 400 Bad Request |
| `ServiceCommunicationException` | Variable (carries `statusCode` and `serviceName`) |
| `Exception` (catch-all) | 500 Internal Server Error |

The `ErrorResponse` DTO contains: `timestamp`, `status`, `error`, `message`, `path`, and an optional `errors[]` list for validation failures.

### Validation

`ValidationUtils` is a static utility class with the following methods:

| Method | Description |
|---|---|
| `validateNotEmpty(value, fieldName)` | Fails if null or blank after trimming. |
| `validateNotNull(value, fieldName)` | Fails if null. |
| `validateSocialSecurityNumber(ssn)` | Validates USA format `XXX-XX-XXXX`. |
| `validateSocialSecurityNumber(ssn, region)` | Region-aware SSN validation. Currently supports `"USA"`. |
| `validateEmail(email)` | Validates common email formats. Not full RFC 5322. |
| `validatePositive(value, fieldName)` | Fails if value ≤ 0. |
| `validateNonNegative(value, fieldName)` | Fails if value < 0. |
| `validateLength(value, fieldName, min, max)` | Validates string length within inclusive bounds. |
| `validateAll(Runnable... validations)` | Runs all validations and collects every error before throwing a single `ValidationException`. |

### Logging

| Class | Description |
|---|---|
| `RequestResponseLoggingFilter` | Servlet filter that logs full request and response details including headers and body. |
| `LoggingUtils` | Logs objects as JSON at INFO or DEBUG level. |

### Spring Integration

`ApplicationContextProvider` implements `ApplicationContextAware` and exposes a static `getBean()` method. This allows `BaseProcedure` subclasses (which are instantiated with `new` rather than injected) to resolve Spring beans at runtime.

`CommonAutoConfiguration` is registered via `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports` and component-scans `org.surptech.common`, so all beans are available automatically in any service that includes this library.

## Technology Stack

| Technology | Version |
|---|---|
| Java | 25 |
| Spring Boot | 4.0.6 |
| Lombok | 1.18.46 |
| Jackson | (managed by Spring Boot) |

## Building

This library must be installed to the local Maven repository before building any dependent service.

```bash
cd surptech-common
mvn clean install
```

## Usage in Dependent Services

Add the dependency to the service's `pom.xml`:

```xml
<dependency>
    <groupId>org.surptech</groupId>
    <artifactId>surptech-common</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

No additional Spring configuration is needed. Auto-configuration activates all shared beans automatically.
