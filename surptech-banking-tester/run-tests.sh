#!/bin/bash

# SurpTech Banking System - Test Runner Script

echo "╔════════════════════════════════════════════════════════════════════════════╗"
echo "║           SurpTech Banking System - Test Execution Runner                 ║"
echo "╚════════════════════════════════════════════════════════════════════════════╝"
echo ""

# Function to display usage
usage() {
    echo "Usage: $0 [test-type] [options]"
    echo ""
    echo "Test Types:"
    echo "  all          - Run all tests (default)"
    echo "  smoke        - Run smoke tests only"
    echo "  integration  - Run integration tests only"
    echo "  error        - Run error handling tests only"
    echo "  ci           - Run tests in CI mode (parallel)"
    echo ""
    echo "Options:"
    echo "  --report     - Generate and open Allure report after tests"
    echo "  --serve      - Generate and serve Allure report (opens in browser)"
    echo ""
    echo "Examples:"
    echo "  $0                    # Run all tests"
    echo "  $0 smoke              # Run smoke tests"
    echo "  $0 all --report       # Run all tests and generate report"
    echo "  $0 smoke --serve      # Run smoke tests and open report in browser"
    exit 1
}

# Check if help is requested
if [ "$1" == "-h" ] || [ "$1" == "--help" ]; then
    usage
fi

# Determine test type and options
TEST_TYPE=${1:-all}
GENERATE_REPORT=false
SERVE_REPORT=false

# Parse options
shift
while [ $# -gt 0 ]; do
    case "$1" in
        --report)
            GENERATE_REPORT=true
            ;;
        --serve)
            SERVE_REPORT=true
            ;;
        *)
            echo "Error: Unknown option '$1'"
            usage
            ;;
    esac
    shift
done

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
TEST_EXIT_CODE=$?

echo ""
echo "╔════════════════════════════════════════════════════════════════════════════╗"

if [ $TEST_EXIT_CODE -eq 0 ]; then
    echo "║                        ✓ Tests Completed Successfully!                     ║"
else
    echo "║                            ✗ Tests Failed!                                 ║"
fi

echo "╚════════════════════════════════════════════════════════════════════════════╝"
echo ""

# Generate reports
echo "📊 Available Reports:"
echo "  1. Console logs:     target/surefire-reports/*.txt"
echo "  2. XML reports:      target/surefire-reports/TEST-*.xml"
echo "  3. Maven HTML:       mvn surefire-report:report (then open target/site/surefire-report.html)"
echo "  4. Allure report:    mvn allure:report (then open target/allure-report/index.html)"
echo ""

# Generate Allure report if requested
if [ "$GENERATE_REPORT" = true ] || [ "$SERVE_REPORT" = true ]; then
    echo "Generating Allure report..."
    mvn allure:report
    
    if [ $? -eq 0 ]; then
        echo "✓ Allure report generated successfully!"
        echo "  Location: target/allure-report/index.html"
        
        if [ "$SERVE_REPORT" = true ]; then
            echo ""
            echo "🌐 Opening Allure report in browser..."
            mvn allure:serve
        fi
    else
        echo "✗ Failed to generate Allure report"
    fi
fi

echo ""

# Exit with test result code
if [ $TEST_EXIT_CODE -ne 0 ]; then
    echo "⚠️  Check the detailed logs above for failure information"
    exit 1
fi
