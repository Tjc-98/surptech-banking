package org.surptech.bankingtester.listener;

import lombok.extern.slf4j.Slf4j;
import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.TestPlan;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * Custom test execution listener for beautiful console reporting
 */
@Slf4j
public class CustomTestReportListener implements TestExecutionListener {
    
    private final Map<String, Instant> testStartTimes = new HashMap<>();
    private int totalTests = 0;
    private int passedTests = 0;
    private int failedTests = 0;
    private int skippedTests = 0;
    private Instant suiteStartTime;
    
    @Override
    public void testPlanExecutionStarted(TestPlan testPlan) {
        suiteStartTime = Instant.now();
        log.info("");
        log.info("╔" + "═".repeat(78) + "╗");
        log.info("║" + center("SURPTECH BANKING SYSTEM - TEST EXECUTION", 78) + "║");
        log.info("╚" + "═".repeat(78) + "╝");
        log.info("");
    }
    
    @Override
    public void executionStarted(TestIdentifier testIdentifier) {
        if (testIdentifier.isTest()) {
            testStartTimes.put(testIdentifier.getUniqueId(), Instant.now());
            totalTests++;
        }
    }
    
    @Override
    public void executionFinished(TestIdentifier testIdentifier, TestExecutionResult testExecutionResult) {
        if (testIdentifier.isTest()) {
            Instant startTime = testStartTimes.get(testIdentifier.getUniqueId());
            Duration duration = Duration.between(startTime, Instant.now());
            
            String status;
            String symbol;
            
            switch (testExecutionResult.getStatus()) {
                case SUCCESSFUL:
                    status = "PASSED";
                    symbol = "✓";
                    passedTests++;
                    break;
                case FAILED:
                    status = "FAILED";
                    symbol = "✗";
                    failedTests++;
                    break;
                case ABORTED:
                    status = "SKIPPED";
                    symbol = "⊘";
                    skippedTests++;
                    break;
                default:
                    status = "UNKNOWN";
                    symbol = "?";
            }
            
            log.info("");
            log.info("┌" + "─".repeat(78) + "┐");
            log.info("│ {} {} - {} ms", symbol, testIdentifier.getDisplayName(), duration.toMillis());
            log.info("│ Status: {}", status);
            
            if (testExecutionResult.getStatus() == TestExecutionResult.Status.FAILED) {
                testExecutionResult.getThrowable().ifPresent(throwable -> {
                    log.info("│ Error: {}", throwable.getMessage());
                });
            }
            
            log.info("└" + "─".repeat(78) + "┘");
        }
    }
    
    @Override
    public void testPlanExecutionFinished(TestPlan testPlan) {
        Duration totalDuration = Duration.between(suiteStartTime, Instant.now());
        
        log.info("");
        log.info("╔" + "═".repeat(78) + "╗");
        log.info("║" + center("TEST EXECUTION SUMMARY", 78) + "║");
        log.info("╠" + "═".repeat(78) + "╣");
        log.info("║ Total Tests:   " + padRight(String.valueOf(totalTests), 60) + "║");
        log.info("║ ✓ Passed:      " + padRight(String.valueOf(passedTests), 60) + "║");
        log.info("║ ✗ Failed:      " + padRight(String.valueOf(failedTests), 60) + "║");
        log.info("║ ⊘ Skipped:     " + padRight(String.valueOf(skippedTests), 60) + "║");
        log.info("║ Duration:      " + padRight(formatDuration(totalDuration), 60) + "║");
        log.info("╚" + "═".repeat(78) + "╝");
        log.info("");
        
        if (failedTests == 0) {
            log.info("🎉 All tests passed successfully!");
        } else {
            log.info("⚠️  {} test(s) failed. Please review the logs above.", failedTests);
        }
        log.info("");
    }
    
    private String center(String text, int width) {
        int padding = (width - text.length()) / 2;
        return " ".repeat(padding) + text + " ".repeat(width - text.length() - padding);
    }
    
    private String padRight(String text, int width) {
        return text + " ".repeat(Math.max(0, width - text.length()));
    }
    
    private String formatDuration(Duration duration) {
        long seconds = duration.getSeconds();
        long millis = duration.toMillis() % 1000;
        
        if (seconds > 0) {
            return String.format("%d.%03d seconds", seconds, millis);
        } else {
            return String.format("%d ms", millis);
        }
    }
}
