package org.surptech.common.tester.base;

import io.qameta.allure.Allure;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.surptech.common.tester.listener.AllureLogAppender;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

/**
 * Base class for managing test lifecycle events and logging.
 * Provides hooks for test setup, teardown, and step logging.
 *
 * Log lines are buffered by {@link AllureLogAppender} during each test and
 * flushed here as a single "Test Log" attachment so the full output is
 * visible in the Allure report without needing to expand individual items.
 */
@Slf4j
public abstract class TestLifecycleManager {

    protected TestInfo currentTestInfo;

    @BeforeEach
    public void beforeEachTest(TestInfo testInfo) {
        this.currentTestInfo = testInfo;

        String testName  = testInfo.getDisplayName();
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

        // Flush the buffered log lines as a single Allure attachment.
        // The content is attached as plain text so Allure renders it inline
        // (visible immediately, no expansion required).
        String logOutput = AllureLogAppender.flushToString();
        if (!logOutput.isEmpty()) {
            Allure.addAttachment(
                    "Test Log",
                    "text/plain",
                    new ByteArrayInputStream(logOutput.getBytes(StandardCharsets.UTF_8)),
                    "txt"
            );
        }
    }

    /**
     * Log a test step with consistent formatting and record it as an Allure step.
     *
     * @param message the step message to log
     */
    protected void logStep(String message) {
        log.info("  ▶ {}", message);
        Allure.step(message);
    }

    /**
     * Log an info message.
     *
     * @param message the message to log
     */
    protected void logInfo(String message) {
        log.info("    {}", message);
    }

    /**
     * Log a warning message.
     *
     * @param message the warning message to log
     */
    protected void logWarning(String message) {
        log.warn("    ⚠ {}", message);
    }

    /**
     * Log an error message.
     *
     * @param message the error message to log
     */
    protected void logError(String message) {
        log.error("    ✗ {}", message);
    }
}
