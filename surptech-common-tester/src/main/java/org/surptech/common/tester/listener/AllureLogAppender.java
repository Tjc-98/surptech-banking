package org.surptech.common.tester.listener;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import io.qameta.allure.Allure;

/**
 * Custom Logback appender that forwards log messages to Allure reports.
 * This ensures that all log output is captured in the Allure test report.
 */
public class AllureLogAppender extends AppenderBase<ILoggingEvent> {
    
    @Override
    protected void append(ILoggingEvent event) {
        // Only attach logs if there's an active test context
        try {
            String formattedMessage = event.getFormattedMessage();
            if (formattedMessage != null && !formattedMessage.isEmpty()) {
                Allure.addAttachment(
                    "Log: " + event.getLevel(),
                    "text/plain",
                    formattedMessage,
                    ".txt"
                );
            }
        } catch (Exception e) {
            // Silently ignore if no active test context
        }
    }
}
