package org.surptech.common.tester.listener;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Custom Logback appender that buffers log messages per test.
 *
 * Instead of attaching each log line as a separate Allure attachment (which
 * produces many collapsed items that must be expanded individually), this
 * appender accumulates all lines for the current test into a thread-local
 * buffer. TestLifecycleManager flushes the buffer at the end of each test
 * via {@link #flushToString()} and attaches the result as a single
 * pre-formatted text block — visible immediately without any expansion.
 */
public class AllureLogAppender extends AppenderBase<ILoggingEvent> {

    private static final DateTimeFormatter TIMESTAMP_FMT =
            DateTimeFormatter.ofPattern("HH:mm:ss.SSS").withZone(ZoneId.systemDefault());

    /** Per-thread log buffer — one buffer per running test. */
    private static final ThreadLocal<List<String>> LOG_BUFFER =
            ThreadLocal.withInitial(ArrayList::new);

    @Override
    protected void append(ILoggingEvent event) {
        try {
            String message = event.getFormattedMessage();
            if (message == null || message.isEmpty()) {
                return;
            }
            String timestamp = TIMESTAMP_FMT.format(Instant.ofEpochMilli(event.getTimeStamp()));
            String level     = String.format("%-5s", event.getLevel().toString());
            LOG_BUFFER.get().add(String.format("[%s] %s %s", timestamp, level, message));
        } catch (Exception ignored) {
            // Never let the appender break test execution
        }
    }

    /**
     * Drains the current thread's buffer and returns all accumulated log lines
     * as a single newline-separated string, then clears the buffer ready for
     * the next test.
     *
     * Called by {@link org.surptech.common.tester.base.TestLifecycleManager}
     * in its {@code @AfterEach} hook.
     *
     * @return formatted log output, or an empty string if nothing was logged
     */
    public static String flushToString() {
        List<String> lines = LOG_BUFFER.get();
        String result = String.join("\n", lines);
        lines.clear();
        return result;
    }
}
