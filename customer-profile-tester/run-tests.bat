@echo off
REM Customer Profile Service Test Runner Script
REM Usage: run-tests.bat [suite]
REM Suites: all, smoke, integration, error-handling, health

setlocal

set SUITE=%1
if "%SUITE%"=="" set SUITE=all

echo ╔════════════════════════════════════════════════════════════════════════════╗
echo ║           Customer Profile Service - Test Execution                       ║
echo ╚════════════════════════════════════════════════════════════════════════════╝
echo.
echo Suite: %SUITE%
echo.

if "%SUITE%"=="all" (
    call mvn clean test
    goto :end
)

if "%SUITE%"=="smoke" (
    call mvn clean test -Dtest.suite=smoke
    goto :end
)

if "%SUITE%"=="integration" (
    call mvn clean test -Dtest.suite=integration
    goto :end
)

if "%SUITE%"=="error-handling" (
    call mvn clean test -Dtest.suite=error-handling
    goto :end
)

if "%SUITE%"=="error" (
    call mvn clean test -Dtest.suite=error-handling
    goto :end
)

if "%SUITE%"=="health" (
    call mvn clean test -Dtest.suite=health
    goto :end
)

echo Error: Unknown suite '%SUITE%'
echo.
echo Available suites:
echo   all            - Run all tests (default)
echo   smoke          - Run smoke tests only
echo   integration    - Run integration tests only
echo   error-handling - Run error handling tests only
echo   health         - Run health check tests only
exit /b 1

:end
set EXIT_CODE=%ERRORLEVEL%
echo.
if %EXIT_CODE%==0 (
    echo ✓ Tests completed successfully!
) else (
    echo ✗ Tests failed with exit code %EXIT_CODE%
)

exit /b %EXIT_CODE%
