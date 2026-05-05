#!/bin/bash

# SurpTech Banking System - Test Runner Script

echo "=========================================="
echo "SurpTech Banking System - Test Runner"
echo "=========================================="
echo ""

# Function to display usage
usage() {
    echo "Usage: $0 [test-type]"
    echo ""
    echo "Test Types:"
    echo "  all          - Run all tests (default)"
    echo "  smoke        - Run smoke tests only"
    echo "  integration  - Run integration tests only"
    echo "  error        - Run error handling tests only"
    echo "  ci           - Run tests in CI mode (parallel)"
    echo ""
    echo "Examples:"
    echo "  $0              # Run all tests"
    echo "  $0 smoke        # Run smoke tests"
    echo "  $0 integration  # Run integration tests"
    exit 1
}

# Check if help is requested
if [ "$1" == "-h" ] || [ "$1" == "--help" ]; then
    usage
fi

# Determine test type
TEST_TYPE=${1:-all}

echo "Test Type: $TEST_TYPE"
echo ""

# Run tests based on type
case $TEST_TYPE in
    all)
        echo "Running all tests..."
        mvn clean test
        ;;
    smoke)
        echo "Running smoke tests..."
        mvn clean test -P smoke
        ;;
    integration)
        echo "Running integration tests..."
        mvn clean test -P integration
        ;;
    error)
        echo "Running error handling tests..."
        mvn clean test -P error-handling
        ;;
    ci)
        echo "Running tests in CI mode..."
        mvn clean test -P ci
        ;;
    *)
        echo "Error: Unknown test type '$TEST_TYPE'"
        echo ""
        usage
        ;;
esac

# Check exit code
if [ $? -eq 0 ]; then
    echo ""
    echo "=========================================="
    echo "✓ Tests completed successfully!"
    echo "=========================================="
    echo ""
    echo "View detailed HTML report:"
    echo "  mvn surefire-report:report"
    echo "  Open: target/site/surefire-report.html"
else
    echo ""
    echo "=========================================="
    echo "✗ Tests failed!"
    echo "=========================================="
    echo ""
    echo "Check the reports in target/surefire-reports/"
    exit 1
fi
