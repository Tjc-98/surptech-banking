#!/bin/bash

# Customer Profile Service Test Runner Script
# Usage: ./run-tests.sh [suite]
# Suites: all, smoke, integration, error-handling, health

SUITE=${1:-all}

echo "╔════════════════════════════════════════════════════════════════════════════╗"
echo "║           Customer Profile Service - Test Execution                       ║"
echo "╚════════════════════════════════════════════════════════════════════════════╝"
echo ""
echo "Suite: $SUITE"
echo ""

case $SUITE in
    all)
        mvn clean test
        ;;
    smoke)
        mvn clean test -Dtest.suite=smoke
        ;;
    integration)
        mvn clean test -Dtest.suite=integration
        ;;
    error-handling|error)
        mvn clean test -Dtest.suite=error-handling
        ;;
    health)
        mvn clean test -Dtest.suite=health
        ;;
    *)
        echo "Error: Unknown suite '$SUITE'"
        echo ""
        echo "Available suites:"
        echo "  all            - Run all tests (default)"
        echo "  smoke          - Run smoke tests only"
        echo "  integration    - Run integration tests only"
        echo "  error-handling - Run error handling tests only"
        echo "  health         - Run health check tests only"
        exit 1
        ;;
esac

EXIT_CODE=$?

echo ""
if [ $EXIT_CODE -eq 0 ]; then
    echo "✓ Tests completed successfully!"
else
    echo "✗ Tests failed with exit code $EXIT_CODE"
fi

exit $EXIT_CODE
