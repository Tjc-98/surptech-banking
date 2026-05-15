package org.surptech.common.tester.base;

import io.qameta.allure.Allure;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;

/**
 * Base class for managing test lifecycle events and logging.
 * Provides hooks for test setup, teardown, and step logging.
 */
@Slf4j
public abstract class TestLifecycleManager {
    
    protected TestInfo currentTestInfo;
    
    @BeforeEach
    public void beforeEachTest(TestInfo testInfo) {
        this.currentTestInfo = testInfo;
        
        String testName = testInfo.getDisplayName();
        String className = testInfo.getTestClass()
                .map(Class::getSimpleName)
                .orElse("Unknown");
        
        log.info("");
        log.info("╔" + "═".repeat(78) + "╗");
        log.info("║  TEST STARTED: {}", String.format("%-60s", testName));
        log.info("║  Class: {}", String.format("%-67s", className));
        log.info("╚" + "═".repeat(78) + "╝");
        log.info("");
    }
    
    @AfterEach
    public void afterEachTest(TestInfo testInfo) {
        String testName = testInfo.getDisplayName();
        
        log.info("");
        log.info("╔" + "═".repeat(78) + "╗");
        log.info("║  TEST COMPLETED: {}", String.format("%-58s", testName));
        log.info("╚" + "═".repeat(78) + "╝");
        log.info("");
    }
    
    /**
     * Log a test step with consistent formatting
     * 
     * @param message The step message to log
     */
    protected void logStep(String message) {
        log.info("  ▶ {}", message);
        Allure.step(message);
    }
    
    /**
     * Log an info message
     * 
     * @param message The message to log
     */
    protected void logInfo(String message) {
        log.info("    {}", message);
    }
    
    /**
     * Log a warning message
     * 
     * @param message The warning message to log
     */
    protected void logWarning(String message) {
        log.warn("    ⚠ {}", message);
    }
    
    /**
     * Log an error message
     * 
     * @param message The error message to log
     */
    protected void logError(String message) {
        log.error("    ✗ {}", message);
    }
}
