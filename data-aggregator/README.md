# data-aggregator

Spring Boot service that acts as the API gateway for the SurpTech Banking System. It aggregates customer personal information and credit data from the downstream services into a single unified response.

## Purpose

Clients (the frontend and integration tests) interact only with this service. It fetches data from `customer-profile` and `credit-profile` in parallel, merges the results, and returns a combined view. It also provides a health dashboard that reports the status of all downstream services.

## Dependencies

This service requires both downstream services to be running before it can serve meaningful data:

- `customer-profile` on port `5551`
- `credit-profile` on port `5552`

An Auth Server on port `5553` is referenced in configuration but is **not yet implemented**. Token validation is currently a no-op that always returns `true`.

## API Reference

Base URL: `http://localhost:5555/data-aggregator`

### GET /customer/info

Returns aggregated customer and credit information for a given Social Security Number.

**Query Parameters**

| Parameter | Type | Required | Description |
|---|---|---|---|
| `socialSecurityNumber` | string | Yes | USA format: `XXX-XX-XXXX` |

**Request Headers**

| Header | Required | Description |
|---|---|---|
| `Authorization` | No | Bearer token. If provided, validated against the auth server. Currently a placeholder — always passes. |

**Responses**

| Status | Description |
|---|---|
| 200 OK | Returns `CustomerCreditInfoResponse` JSON |
| 400 Bad Request | Invalid SSN format |
| 401 Unauthorized | Invalid auth token (when auth server is implemented) |
| 404 Not Found | No customer or credit profile found for the given SSN |

**Example**

```
GET /data-aggregator/customer/info?socialSecurityNumber=123-45-6789
```

```json
{
  "social_security_number": "123-45-6789",
  "first_name": "James",
  "last_name": "Smith",
  "address": "456 Tailor Street, California, LA 56001",
  "full_credit_balance": 15000.0,
  "spend_balance": 5000.0,
  "interest_rate": 3.5
}
```

**Partial responses:** If only one of the two downstream profiles exists, the response is returned with the available fields populated and the missing fields omitted (`null`).

---

### GET /services/management/health

Returns the health status of all downstream services monitored by this aggregator.

```
GET /data-aggregator/services/management/health
```

**Example Response**

```json
{
  "status": "UP",
  "components": {
    "customerProfile": { "status": "UP", "service": "customer-profile", "url": "http://localhost:5551", "available": true },
    "creditProfile":   { "status": "UP", "service": "credit-profile",   "url": "http://localhost:5552", "available": true },
    "authServer":      { "status": "DOWN", "service": "auth-server",    "url": "http://localhost:5553", "available": false }
  }
}
```

Overall status is `UP` as long as `customer-profile` is reachable (it is the critical service).

---

### GET /management/health

Spring Actuator health endpoint for the data-aggregator service itself.

## Configuration

`src/main/resources/application.properties`

| Property | Default | Description |
|---|---|---|
| `server.port` | `5555` | HTTP port |
| `server.servlet.context-path` | `/data-aggregator` | URL context path |
| `service.customer-profile.url` | `http://localhost:5551` | Customer Profile service base URL |
| `service.credit-profile.url` | `http://localhost:5552` | Credit Profile service base URL |
| `service.auth-server.url` | `http://localhost:5553` | Auth Server base URL (not yet active) |
| `management.endpoints.web.base-path` | `/management` | Actuator base path |

## Architecture

```
CustomerBankingController
  └─ executeProcedure(new GetCustomerCreditInfoProcedure(ssn))
       └─ BaseProcedure.execute()
            └─ executeProcedure()
                 ├─ CustomerProfileClient → GET http://localhost:5551/customer-profile/customer/get
                 └─ CreditProfileClient  → GET http://localhost:5552/credit-profile/credit/get
                      └─ CustomerCreditInfoMapper.toResponse(entity)
```

Each downstream client is backed by a `RestClient` built with `RestClientBuilder` (5s connect / 10s read timeouts, with request/response logging). The three `RestClient` beans are configured in `RestClientConfiguration` and injected via `@Qualifier`.

On startup, `StartupHealthCheck` runs on `ApplicationReadyEvent` and logs the health status of all three downstream services.

## Technology Stack

| Technology | Version |
|---|---|
| Java | 25 |
| Spring Boot | 4.0.6 |
| Spring RestClient | (managed by Spring Boot) |
| Lombok | 1.18.46 |
| surptech-common | 1.0.0-SNAPSHOT |

## Running

`surptech-common`, `customer-profile`, and `credit-profile` must all be running first.

```bash
# From the repository root — build common library
cd surptech-common
mvn clean install

# Start downstream services (each in a separate terminal)
cd ../customer-profile
mvn spring-boot:run

cd ../credit-profile
mvn spring-boot:run

# Start the aggregator
cd ../data-aggregator
mvn spring-boot:run
```

Verify the service is running:

```bash
curl http://localhost:5555/data-aggregator/management/health
curl "http://localhost:5555/data-aggregator/customer/info?socialSecurityNumber=123-45-6789"
```
