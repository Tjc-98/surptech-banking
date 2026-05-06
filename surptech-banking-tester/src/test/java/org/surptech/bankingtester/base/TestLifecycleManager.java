package org.surptech.bankingtester.base;

import io.qameta.allure.Allure;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.surptech.bankingtester.listener.AllureLogAppender;

/**
 * Manages test lifecycle events including logging, setup, and teardown.
 * This class handles all test execution logging and initialization.
 */
@Slf4j
public abstract class TestLifecycleManager {
    
    // Thread-local step counter for tracking test execution steps
    private static final ThreadLocal<Integer> stepCounter = ThreadLocal.withInitial(() -> 0);
    
    @BeforeEach
    public void testSetup(TestInfo testInfo) {
        // Reset step counter for each test
        stepCounter.set(0);
        
        // Clear logs for new test
        AllureLogAppender.clearTestLogs();
        
        String testId = testInfo.getTestMethod()
                .map(method -> method.getAnnotation(org.surptech.bankingtester.annotation.TestId.class))
                .map(org.surptech.bankingtester.annotation.TestId::value)
                .orElse("N/A");
        
        String className = testInfo.getTestClass().map(Class::getSimpleName).orElse("Unknown");
        String methodName = testInfo.getTestMethod().map(java.lang.reflect.Method::getName).orElse("Unknown");
        
        log.info("");
        log.info("-".repeat(80));
        log.info("Starting Test: [{}] {}.{}", testId, className, methodName);
        log.info("-".repeat(80));
    }
    
    @AfterEach
    public void testTeardown(TestInfo testInfo) {
        String testId = testInfo.getTestMethod()
                .map(method -> method.getAnnotation(org.surptech.bankingtester.annotation.TestId.class))
                .map(org.surptech.bankingtester.annotation.TestId::value)
                .orElse("N/A");
        
        String className = testInfo.getTestClass().map(Class::getSimpleName).orElse("Unknown");
        String methodName = testInfo.getTestMethod().map(java.lang.reflect.Method::getName).orElse("Unknown");
        
        log.info("-".repeat(80));
        log.info("Completed Test: [{}] {}.{}", testId, className, methodName);
        log.info("-".repeat(80));
        log.info("");
        
        // Attach test logs to Allure
        String testLogs = AllureLogAppender.getTestLogs();
        if (testLogs != null && !testLogs.isEmpty()) {
            Allure.addAttachment("Test Execution Logs", "text/plain", testLogs, ".log");
        }
        
        // Clean up thread-local
        stepCounter.remove();
        AllureLogAppender.cleanup();
    }
    
    /**
     * Logs a test step with automatic numbering.
     * Each test starts with step 1 and increments automatically.
     * 
     * @param message The step description
     */
    protected void logStep(String message) {
        int step = stepCounter.get() + 1;
        stepCounter.set(step);
        log.info("Step {}: {}", step, message);
    }
    
    /**
     * Logs a test step with automatic numbering and additional details.
     * 
     * @param message The step description
     * @param details Additional details to log
     */
    protected void logStep(String message, Object... details) {
        int step = stepCounter.get() + 1;
        stepCounter.set(step);
        log.info("Step {}: {}", step, message);
        for (Object detail : details) {
            log.info("  {}", detail);
        }
    }
}
