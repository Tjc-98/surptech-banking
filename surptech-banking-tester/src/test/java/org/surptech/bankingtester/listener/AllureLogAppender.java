package org.surptech.bankingtester.listener;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Custom Logback appender that captures logs for Allure reporting
 */
public class AllureLogAppender extends AppenderBase<ILoggingEvent> {
    
    private static final ThreadLocal<List<String>> testLogs = ThreadLocal.withInitial(ArrayList::new);
    
    @Override
    protected void append(ILoggingEvent eventObject) {
        String logMessage = String.format("[%s] %s - %s",
                eventObject.getLevel(),
                eventObject.getLoggerName(),
                eventObject.getFormattedMessage());
        
        testLogs.get().add(logMessage);
    }
    
    /**
     * Get all logs for the current test
     */
    public static String getTestLogs() {
        List<String> logs = testLogs.get();
        return String.join("\n", logs);
    }
    
    /**
     * Clear logs for the current test
     */
    public static void clearTestLogs() {
        testLogs.get().clear();
    }
    
    /**
     * Clean up thread local
     */
    public static void cleanup() {
        testLogs.remove();
    }
}
