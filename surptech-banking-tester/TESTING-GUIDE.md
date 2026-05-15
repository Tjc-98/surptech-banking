# Testing Guide - Comprehensive Reference

This guide provides detailed information about running tests, CI/CD integration, and best practices.

## Quick Reference

### Command-Line Options

```bash
# Single suite
mvn clean test -Dtest.suite=smoke
mvn clean test -Dtest.suite=integration
mvn clean test -Dtest.suite=error-handling
mvn clean test -Dtest.suite=health

# Multiple suites (comma-separated)
mvn clean test -Dtest.suite=smoke,integration

# All tests (default)
mvn clean test

# Using profiles (alternative)
mvn clean test -P smoke
mvn clean test -P ci

# Shell scripts
./run-tests.sh smoke              # Linux/Mac
./run-tests.sh integration --report
run-tests.bat smoke               # Windows
```

## Available Test Suites

| Suite | Tag | Description | Test Count |
|-------|-----|-------------|------------|
| **smoke** | `@Tag("smoke")` | Quick smoke tests for basic functionality | ~5 tests |
| **integration** | `@Tag("integration")` | Full integration tests | ~10 tests |
| **error-handling** | `@Tag("error-handling")` | Error scenario tests | ~8 tests |
| **health** | `@Tag("health")` | Service health checks | ~3 tests |
| **all** | All tags | All tests combined | ~26 tests |
| **ci** | smoke + integration | Optimized for CI/CD (parallel) | ~15 tests |

## CI/CD Integration

### GitHub Actions

The project includes a GitHub Actions workflow (`.github/workflows/run-tests.yml`) that:

1. **Automatic Triggers:**
   - Push to `main` or `develop` branches
   - Pull requests to `main` or `develop`
   - Runs `smoke` tests on PRs, `ci` suite on pushes

2. **Manual Trigger:**
   - Go to Actions tab → "Run Integration Tests"
   - Select suite from dropdown
   - Click "Run workflow"

3. **Features:**
   - Builds and starts all services automatically
   - Waits for services to be healthy
   - Runs selected test suite
   - Generates Allure reports
   - Uploads test results as artifacts
   - Publishes test report summary
   - Stops services after tests

### Jenkins Pipeline Example

```groovy
pipeline {
    agent any
    
    stages {
        stage('Build Common Library') {
            steps {
                dir('surptech-common') {
                    sh 'mvn clean install -DskipTests'
                }
            }
        }
        
        stage('Start Services') {
            parallel {
                stage('Customer Profile') {
                    steps {
                        dir('customer-profile') {
                            sh 'mvn clean package -DskipTests'
                            sh 'nohup java -jar target/customer-profile-*.jar &'
                        }
                    }
                }
                stage('Credit Profile') {
                    steps {
                        dir('credit-profile') {
                            sh 'mvn clean package -DskipTests'
                            sh 'nohup java -jar target/credit-profile-*.jar &'
                        }
                    }
                }
                stage('Data Aggregator') {
                    steps {
                        dir('data-aggregator') {
                            sh 'mvn clean package -DskipTests'
                            sh 'nohup java -jar target/data-aggregator-*.jar &'
                        }
                    }
                }
            }
        }
        
        stage('Wait for Services') {
            steps {
                sh 'sleep 30'
                sh 'curl --retry 10 --retry-delay 5 http://localhost:5551/actuator/health'
                sh 'curl --retry 10 --retry-delay 5 http://localhost:5552/actuator/health'
                sh 'curl --retry 10 --retry-delay 5 http://localhost:5555/data-aggregator/actuator/health'
            }
        }
        
        stage('Run Tests') {
            steps {
                dir('surptech-banking-tester') {
                    sh 'mvn clean test -Dtest.suite=${TEST_SUITE}'
                }
            }
        }
        
        stage('Generate Reports') {
            steps {
                dir('surptech-banking-tester') {
                    sh 'mvn allure:report'
                }
            }
        }
    }
    
    post {
        always {
            junit 'surptech-banking-tester/target/surefire-reports/TEST-*.xml'
            allure includeProperties: false,
                   jdk: '',
                   results: [[path: 'surptech-banking-tester/target/allure-results']]
        }
    }
}
```

### GitLab CI Example

```yaml
variables:
  MAVEN_OPTS: "-Dmaven.repo.local=$CI_PROJECT_DIR/.m2/repository"

cache:
  paths:
    - .m2/repository

stages:
  - build
  - test
  - report

build-common:
  stage: build
  script:
    - cd surptech-common
    - mvn clean install -DskipTests

start-services:
  stage: build
  script:
    - cd customer-profile && mvn clean package -DskipTests &
    - cd credit-profile && mvn clean package -DskipTests &
    - cd data-aggregator && mvn clean package -DskipTests &
    - wait

test-smoke:
  stage: test
  script:
    - cd surptech-banking-tester
    - mvn clean test -Dtest.suite=smoke
  artifacts:
    when: always
    reports:
      junit: surptech-banking-tester/target/surefire-reports/TEST-*.xml
    paths:
      - surptech-banking-tester/target/surefire-reports/
      - surptech-banking-tester/target/allure-results/

test-integration:
  stage: test
  script:
    - cd surptech-banking-tester
    - mvn clean test -Dtest.suite=integration
  artifacts:
    when: always
    reports:
      junit: surptech-banking-tester/target/surefire-reports/TEST-*.xml
    paths:
      - surptech-banking-tester/target/surefire-reports/
      - surptech-banking-tester/target/allure-results/

generate-report:
  stage: report
  script:
    - cd surptech-banking-tester
    - mvn allure:report
  artifacts:
    paths:
      - surptech-banking-tester/target/allure-report/
```

## Best Practices

### For Local Development
- Use shell scripts: `./run-tests.sh smoke`
- Or Maven profiles: `mvn test -P smoke`
- Generate reports: `./run-tests.sh smoke --serve`

### For CI/CD Pipelines
- Use `-Dtest.suite` property: `mvn test -Dtest.suite=smoke`
- Run `smoke` tests on every commit
- Run `integration` tests on PRs
- Run `all` tests before merging to main
- Use `ci` profile for parallel execution

### For Pull Requests
- Minimum: `smoke` suite
- Recommended: `smoke,integration`
- Full validation: `all`

### For Release Validation
- Run all suites: `mvn test -Dtest.suite=smoke,integration,error-handling,health`
- Or simply: `mvn test` (runs all by default)

## Troubleshooting

### Tests Fail with Connection Errors
```bash
# Check if services are running
curl http://localhost:5551/actuator/health
curl http://localhost:5552/actuator/health
curl http://localhost:5555/data-aggregator/actuator/health

# Start services manually if needed
cd customer-profile && mvn spring-boot:run &
cd credit-profile && mvn spring-boot:run &
cd data-aggregator && mvn spring-boot:run &
```

### Suite Parameter Not Working
```bash
# Make sure you're using the correct syntax
mvn clean test -Dtest.suite=smoke  # ✓ Correct
mvn clean test -Dsuite=smoke       # ✗ Wrong
mvn clean test --suite=smoke       # ✗ Wrong
```

### No Tests Found
```bash
# Verify the suite name is correct
mvn clean test -Dtest.suite=smoke          # ✓ Correct
mvn clean test -Dtest.suite=smoke-tests    # ✗ Wrong (use 'smoke')
```

## Adding New Test Suites

To add a new test suite:

1. **Add the tag to your test:**
   ```java
   @Test
   @Tag("performance")
   @DisplayName("Performance test")
   public void testPerformance() {
       // test code
   }
   ```

2. **Create a suite class (optional):**
   ```java
   @Suite
   @SuiteDisplayName("Performance Tests")
   @SelectPackages("org.surptech.bankingtester.tests")
   @IncludeTags("performance")
   public class PerformanceTestsSuite {
   }
   ```

3. **Add a Maven profile in pom.xml:**
   ```xml
   <profile>
       <id>performance</id>
       <properties>
           <test.suite>performance</test.suite>
       </properties>
   </profile>
   ```

4. **Update documentation:**
   - Add to this guide
   - Update README.md
   - Update shell scripts if needed

## Summary

**Recommended approach for CI/CD:**
```bash
mvn clean test -Dtest.suite=<suite-name>
```

**Why?**
- ✓ Simple and consistent
- ✓ Works across all CI/CD platforms
- ✓ Easy to parameterize in pipelines
- ✓ No need to remember profile names
- ✓ Supports multiple suites with comma separation
