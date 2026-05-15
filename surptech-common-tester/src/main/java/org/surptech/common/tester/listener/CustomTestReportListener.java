package org.surptech.common.tester.listener;

import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;

/**
 * Custom test execution listener for enhanced test reporting.
 * Implements JUnit Platform's TestExecutionListener to capture test lifecycle events.
 */
public class CustomTestReportListener implements TestExecutionListener {
    
    private int totalTests = 0;
    private int passedTests = 0;
    private int failedTests = 0;
    private int skippedTests = 0;
    
    @Override
    public void executionStarted(TestIdentifier testIdentifier) {
        if (testIdentifier.isTest()) {
            totalTests++;
        }
    }
    
    @Override
    public void executionFinished(TestIdentifier testIdentifier, TestExecutionResult testExecutionResult) {
        if (testIdentifier.isTest()) {
            switch (testExecutionResult.getStatus()) {
                case SUCCESSFUL:
                    passedTests++;
                    break;
                case FAILED:
                    failedTests++;
                    break;
                case ABORTED:
                    skippedTests++;
                    break;
            }
        }
    }
    
    /**
     * Get the total number of tests executed
     * @return total test count
     */
    public int getTotalTests() {
        return totalTests;
    }
    
    /**
     * Get the number of passed tests
     * @return passed test count
     */
    public int getPassedTests() {
        return passedTests;
    }
    
    /**
     * Get the number of failed tests
     * @return failed test count
     */
    public int getFailedTests() {
        return failedTests;
    }
    
    /**
     * Get the number of skipped tests
     * @return skipped test count
     */
    public int getSkippedTests() {
        return skippedTests;
    }
}
