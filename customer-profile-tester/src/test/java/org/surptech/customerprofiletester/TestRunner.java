package org.surptech.customerprofiletester;

import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;

import java.io.PrintWriter;

import static org.junit.platform.engine.discovery.ClassNameFilter.includeClassNamePatterns;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectPackage;

/**
 * Command-line test runner for executing specific test suites.
 * Supports running tests via command-line arguments for CI/CD integration.
 * 
 * Usage:
 *   java -cp ... org.surptech.customerprofiletester.TestRunner [suite]
 * 
 * Suites:
 *   all          - Run all tests (default)
 *   smoke        - Run smoke tests only
 *   integration  - Run integration tests only
 *   error        - Run error handling tests only
 *   health       - Run health check tests only
 */
public class TestRunner {

    private static final String TESTS_PACKAGE = "org.surptech.customerprofiletester.tests";

    public static void main(String[] args) {
        String suite = args.length > 0 ? args[0].toLowerCase() : "all";
        
        System.out.println("╔════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║           Customer Profile Service - Test Runner                          ║");
        System.out.println("╚════════════════════════════════════════════════════════════════════════════╝");
        System.out.println();
        System.out.println("Suite: " + suite);
        System.out.println();

        LauncherDiscoveryRequest request = buildRequest(suite);
        
        if (request == null) {
            System.err.println("Error: Unknown suite '" + suite + "'");
            System.err.println();
            printUsage();
            System.exit(1);
        }

        Launcher launcher = LauncherFactory.create();
        SummaryGeneratingListener listener = new SummaryGeneratingListener();
        
        launcher.registerTestExecutionListeners(listener);
        launcher.execute(request);

        TestExecutionSummary summary = listener.getSummary();
        
        System.out.println();
        System.out.println("╔════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                           Test Execution Summary                           ║");
        System.out.println("╚════════════════════════════════════════════════════════════════════════════╝");
        
        summary.printTo(new PrintWriter(System.out));
        
        System.out.println();
        
        long failures = summary.getTestsFailedCount();
        if (failures > 0) {
            System.out.println("✗ Tests Failed: " + failures);
            System.exit(1);
        } else {
            System.out.println("✓ All Tests Passed!");
            System.exit(0);
        }
    }

    private static LauncherDiscoveryRequest buildRequest(String suite) {
        LauncherDiscoveryRequestBuilder builder = LauncherDiscoveryRequestBuilder.request();

        switch (suite) {
            case "all":
                builder.selectors(selectPackage(TESTS_PACKAGE));
                break;
                
            case "smoke":
                return LauncherDiscoveryRequestBuilder.request()
                        .selectors(org.junit.platform.engine.discovery.DiscoverySelectors
                                .selectClass("org.surptech.customerprofiletester.suite.SmokeTestsSuite"))
                        .build();
                
            case "integration":
                return LauncherDiscoveryRequestBuilder.request()
                        .selectors(org.junit.platform.engine.discovery.DiscoverySelectors
                                .selectClass("org.surptech.customerprofiletester.suite.IntegrationTestsSuite"))
                        .build();
                
            case "error":
            case "error-handling":
                return LauncherDiscoveryRequestBuilder.request()
                        .selectors(selectPackage(TESTS_PACKAGE))
                        .filters(org.junit.platform.launcher.TagFilter.includeTags("error-handling"))
                        .build();
                
            case "health":
                return LauncherDiscoveryRequestBuilder.request()
                        .selectors(selectPackage(TESTS_PACKAGE))
                        .filters(org.junit.platform.launcher.TagFilter.includeTags("health"))
                        .build();
                
            default:
                return null;
        }

        return builder.build();
    }

    private static void printUsage() {
        System.out.println("Usage: java -cp ... org.surptech.customerprofiletester.TestRunner [suite]");
        System.out.println();
        System.out.println("Available Suites:");
        System.out.println("  all          - Run all tests (default)");
        System.out.println("  smoke        - Run smoke tests only");
        System.out.println("  integration  - Run integration tests only");
        System.out.println("  error        - Run error handling tests only");
        System.out.println("  health       - Run health check tests only");
        System.out.println();
        System.out.println("Examples:");
        System.out.println("  java -cp ... org.surptech.customerprofiletester.TestRunner");
        System.out.println("  java -cp ... org.surptech.customerprofiletester.TestRunner smoke");
        System.out.println("  java -cp ... org.surptech.customerprofiletester.TestRunner integration");
    }
}
