@echo off
REM SurpTech Banking System - Test Runner Script (Windows)

echo ==========================================
echo SurpTech Banking System - Test Runner
echo ==========================================
echo.

if "%1"=="-h" goto usage
if "%1"=="--help" goto usage

set TEST_TYPE=%1
if "%TEST_TYPE%"=="" set TEST_TYPE=all

echo Test Type: %TEST_TYPE%
echo.

if "%TEST_TYPE%"=="all" goto run_all
if "%TEST_TYPE%"=="smoke" goto run_smoke
if "%TEST_TYPE%"=="integration" goto run_integration
if "%TEST_TYPE%"=="error" goto run_error
if "%TEST_TYPE%"=="ci" goto run_ci

echo Error: Unknown test type '%TEST_TYPE%'
echo.
goto usage

:run_all
echo Running all tests...
call mvn clean test
goto check_result

:run_smoke
echo Running smoke tests...
call mvn clean test -P smoke
goto check_result

:run_integration
echo Running integration tests...
call mvn clean test -P integration
goto check_result

:run_error
echo Running error handling tests...
call mvn clean test -P error-handling
goto check_result

:run_ci
echo Running tests in CI mode...
call mvn clean test -P ci
goto check_result

:check_result
if %ERRORLEVEL% EQU 0 (
    echo.
    echo ==========================================
    echo √ Tests completed successfully!
    echo ==========================================
    echo.
    echo View detailed HTML report:
    echo   mvn surefire-report:report
    echo   Open: target\site\surefire-report.html
) else (
    echo.
    echo ==========================================
    echo × Tests failed!
    echo ==========================================
    echo.
    echo Check the reports in target\surefire-reports\
    exit /b 1
)
goto end

:usage
echo Usage: %0 [test-type]
echo.
echo Test Types:
echo   all          - Run all tests (default)
echo   smoke        - Run smoke tests only
echo   integration  - Run integration tests only
echo   error        - Run error handling tests only
echo   ci           - Run tests in CI mode (parallel)
echo.
echo Examples:
echo   %0              # Run all tests
echo   %0 smoke        # Run smoke tests
echo   %0 integration  # Run integration tests
exit /b 1

:end
