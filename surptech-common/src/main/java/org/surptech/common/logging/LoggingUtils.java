package org.surptech.common.logging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

/**
 * Utility class for common logging operations.
 * Provides methods for structured logging of requests, responses, and objects.
 */
@Slf4j
public class LoggingUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private LoggingUtils() {
        // Utility class - prevent instantiation
    }

    /**
     * Logs an object as JSON at INFO level.
     *
     * @param message log message
     * @param object object to log
     */
    public static void logObject(String message, Object object) {
        if (log.isInfoEnabled()) {
            try {
                String json = objectMapper.writeValueAsString(object);
                log.info("{}: {}", message, json);
            } catch (JsonProcessingException e) {
                log.warn("Failed to serialize object for logging: {}", e.getMessage());
                log.info("{}: {}", message, object);
            }
        }
    }

    /**
     * Logs an object as JSON at DEBUG level.
     *
     * @param message log message
     * @param object object to log
     */
    public static void logObjectDebug(String message, Object object) {
        if (log.isDebugEnabled()) {
            try {
                String json = objectMapper.writeValueAsString(object);
                log.debug("{}: {}", message, json);
            } catch (JsonProcessingException e) {
                log.warn("Failed to serialize object for logging: {}", e.getMessage());
                log.debug("{}: {}", message, object);
            }
        }
    }

    /**
     * Logs a request with standard format.
     *
     * @param endpoint endpoint being called
     * @param request request object
     */
    public static void logRequest(String endpoint, Object request) {
        log.info("Request to {} - Starting", endpoint);
        logObjectDebug("Request payload", request);
    }

    /**
     * Logs a response with standard format.
     *
     * @param endpoint endpoint that was called
     * @param response response object
     */
    public static void logResponse(String endpoint, Object response) {
        log.info("Response from {} - Completed", endpoint);
        logObjectDebug("Response payload", response);
    }

    /**
     * Logs an error with standard format.
     *
     * @param endpoint endpoint where error occurred
     * @param error error message
     * @param exception exception that occurred
     */
    public static void logError(String endpoint, String error, Exception exception) {
        log.error("Error at {} - {}", endpoint, error, exception);
    }

    /**
     * Logs method entry with parameters.
     *
     * @param methodName method name
     * @param params method parameters
     */
    public static void logMethodEntry(String methodName, Object... params) {
        if (log.isDebugEnabled()) {
            log.debug("Entering method: {} with parameters: {}", methodName, params);
        }
    }

    /**
     * Logs method exit with return value.
     *
     * @param methodName method name
     * @param returnValue return value
     */
    public static void logMethodExit(String methodName, Object returnValue) {
        if (log.isDebugEnabled()) {
            log.debug("Exiting method: {} with return value: {}", methodName, returnValue);
        }
    }
}
