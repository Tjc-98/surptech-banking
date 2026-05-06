@echo off
REM SurpTech Banking System - Test Runner Script (Windows)

echo ================================================================================
echo            SurpTech Banking System - Test Execution Runner
echo ================================================================================
echo.

if "%1"=="-h" goto usage
if "%1"=="--help" goto usage

set TEST_TYPE=%1
if "%TEST_TYPE%"=="" set TEST_TYPE=all

set GENERATE_REPORT=false
set SERVE_REPORT=false

:parse_args
if "%2"=="" goto run_tests
if "%2"=="--report" set GENERATE_REPORT=true
if "%2"=="--serve" set SERVE_REPORT=true
shift
goto parse_args

:run_tests
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
set TEST_EXIT_CODE=%ERRORLEVEL%

echo.
echo ================================================================================

if %TEST_EXIT_CODE% EQU 0 (
    echo                        √ Tests Completed Successfully!
) else (
    echo                            × Tests Failed!
)

echo ================================================================================
echo.

echo Available Reports:
echo   1. Console logs:     target\surefire-reports\*.txt
echo   2. XML reports:      target\surefire-reports\TEST-*.xml
echo   3. Maven HTML:       mvn surefire-report:report (then open target\site\surefire-report.html^)
echo   4. Allure report:    mvn allure:report (then open target\allure-report\index.html^)
echo.

if "%GENERATE_REPORT%"=="true" goto generate_report
if "%SERVE_REPORT%"=="true" goto generate_report
goto final_check

:generate_report
echo Generating Allure report...
call mvn allure:report

if %ERRORLEVEL% EQU 0 (
    echo √ Allure report generated successfully!
    echo   Location: target\allure-report\index.html
    
    if "%SERVE_REPORT%"=="true" (
        echo.
        echo Opening Allure report in browser...
        call mvn allure:serve
    )
) else (
    echo × Failed to generate Allure report
)

:final_check
echo.

if %TEST_EXIT_CODE% NEQ 0 (
    echo Check the detailed logs above for failure information
    exit /b 1
)
goto end

:usage
echo Usage: %0 [test-type] [options]
echo.
echo Test Types:
echo   all          - Run all tests (default)
echo   smoke        - Run smoke tests only
echo   integration  - Run integration tests only
echo   error        - Run error handling tests only
echo   ci           - Run tests in CI mode (parallel)
echo.
echo Options:
echo   --report     - Generate and open Allure report after tests
echo   --serve      - Generate and serve Allure report (opens in browser)
echo.
echo Examples:
echo   %0                    # Run all tests
echo   %0 smoke              # Run smoke tests
echo   %0 all --report       # Run all tests and generate report
echo   %0 smoke --serve      # Run smoke tests and open report in browser
exit /b 1

:end
