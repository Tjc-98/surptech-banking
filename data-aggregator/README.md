# Data Aggregator Service

The Data Aggregator service aggregates customer profile and credit profile information from multiple data stores and provides a unified response to clients.

## Configuration

The service runs on port **5555** with context path `/data-aggregator`.

External service URLs are configured in `application.properties`:
- Customer Profile Service: `http://localhost:5551`
- Credit Profile Service: `http://localhost:5552`
- Auth Server: `http://localhost:5553`

## API Endpoints

### External Services Health Check
```
GET /data-aggregator/services/management/health
```

Returns the health status of all external services (customer-profile, credit-profile, auth-server).

### Application Health Check
```
GET /data-aggregator/management/health
```

Standard Spring Boot Actuator health endpoint.

### Get Customer Credit Information
```
GET /data-aggregator/aggregate/customer-credit/{socialSecurityNumber}
```

**Headers:**
- `Authorization` (optional): Bearer token for authentication

## Building and Running

### Build
```bash
cd data-aggregator
mvn clean package
```

### Run
```bash
mvn spring-boot:run
```

Or run the JAR directly:
```bash
java -jar target/data-aggregator-1.0.0-SNAPSHOT.jar
```
