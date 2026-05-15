# SurpTech Banking System

A microservices-based banking system with comprehensive integration testing.

## 🏗️ Architecture

The system consists of multiple microservices:

- **Data Aggregator** (port 5555) - Aggregates customer and credit data
- **Customer Profile** (port 5551) - Manages customer personal information
- **Credit Profile** (port 5552) - Manages customer credit information
- **surptech-common** - Shared utilities and base classes

## 🧪 Testing Infrastructure

Comprehensive integration testing suite with shared utilities:

- **surptech-common-tester** - Shared testing utilities and base classes
- **surptech-banking-tester** - Integration tests for Data Aggregator
- **customer-profile-tester** - Integration tests for Customer Profile service

### Quick Start Testing

```bash
# Build common tester library
cd surptech-common-tester
mvn clean install

# Run smoke tests
cd ../surptech-banking-tester
mvn test -Dtest.suite=smoke

cd ../customer-profile-tester
mvn test -Dtest.suite=smoke
```

**📖 Documentation:**
- [Banking Tester README](surptech-banking-tester/README.md)
- [Customer Profile Tester README](customer-profile-tester/README.md)
- [Customer Profile Testing Guide](customer-profile-tester/TESTING-GUIDE.md)

## 🚀 Getting Started

### Prerequisites

- Java 25
- Maven 3.8+
- SQLite (included)

### Build and Run

```bash
# Build shared common library
cd surptech-common
mvn clean install

# Start Customer Profile service
cd customer-profile
mvn spring-boot:run

# Start Credit Profile service (in another terminal)
cd credit-profile
mvn spring-boot:run

# Start Data Aggregator service (in another terminal)
cd data-aggregator
mvn spring-boot:run
```

### Verify Services

```bash
# Check service health
curl http://localhost:5551/customer-profile/management/health
curl http://localhost:5552/credit-profile/management/health
curl http://localhost:5555/data-aggregator/management/health
```

## 📊 Test Reports

Generate beautiful test reports with Allure:

```bash
cd surptech-banking-tester
mvn allure:serve

cd customer-profile-tester
mvn allure:serve
```

## 🔄 CI/CD

GitHub Actions workflows for automated testing:

- **Run Integration Tests** - Tests Data Aggregator service
- **Run Customer Profile Tests** - Tests Customer Profile service

Trigger manually from the Actions tab in GitHub.

## 📁 Project Structure

```
surptech-banking/
├── surptech-common/              # Shared utilities
├── surptech-common-tester/       # Shared testing utilities
├── customer-profile/             # Customer Profile service
├── credit-profile/               # Credit Profile service
├── data-aggregator/              # Data Aggregator service
├── surptech-banking-tester/      # Integration tests (Data Aggregator)
├── customer-profile-tester/      # Integration tests (Customer Profile)
├── wiki/                         # Documentation and diagrams
├── .github/workflows/            # CI/CD workflows
└── README.md                     # This file
```

## 🛠️ Technologies

- **Java 25** - Programming language
- **Spring Boot** - Application framework
- **SQLite** - Database
- **Maven** - Build tool
- **JUnit 5** - Testing framework
- **REST Assured** - API testing
- **Allure** - Test reporting

## 📖 Documentation

- [Architecture Diagram](wiki/architecture/architecture-diagram.mmd)
- [Banking Tester Documentation](surptech-banking-tester/README.md)
- [Customer Profile Tester Documentation](customer-profile-tester/README.md)

## 📝 License

Copyright © 2024 SurpTech