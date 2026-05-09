package org.surptech.common.logging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;

/**
 * Utility class for common logging operations.
 * Provides methods for structured logging of requests, responses, and objects.
 * Thread-safe for use across multiple concurrent threads.
 */
@Slf4j
public class LoggingUtils {

    private static final ObjectMapper objectMapper = configureObjectMapper();

    private LoggingUtils() {
        // Utility class - prevent instantiation
    }

    /**
     * Configures the ObjectMapper with necessary modules and settings.
     * Includes support for Java 8+ date/time types.
     *
     * @return configured ObjectMapper instance
     */
    private static ObjectMapper configureObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }

    /**
     * Logs an object as JSON at INFO level.
     *
     * @param message log message prefix
     * @param object object to log as JSON
     */
    public static void logObject(String message, Object object) {
        if (log.isInfoEnabled()) {
            try {
                String jsonString = objectMapper.writeValueAsString(object);
                log.info("{}: {}", message, jsonString);
            } catch (JsonProcessingException exception) {
                logSerializationError(exception, message, object);
            }
        }
    }

    /**
     * Logs an object as JSON at DEBUG level.
     *
     * @param message log message prefix
     * @param object object to log as JSON
     */
    public static void logObjectAtDebugLevel(String message, Object object) {
        if (log.isDebugEnabled()) {
            try {
                String jsonString = objectMapper.writeValueAsString(object);
                log.debug("{}: {}", message, jsonString);
            } catch (JsonProcessingException exception) {
                logSerializationError(exception, message, object);
            }
        }
    }

    /**
     * Deprecated: Use {@link #logObjectAtDebugLevel(String, Object)} instead.
     * This method name is less clear about the logging level used.
     *
     * @param message log message prefix
     * @param object object to log as JSON
     */
    @Deprecated(since = "1.0.1", forRemoval = false)
    public static void logObjectDebug(String message, Object object) {
        logObjectAtDebugLevel(message, object);
    }

    /**
     * Logs a request with standard format.
     *
     * @param endpoint endpoint being called
     * @param request request object to log
     */
    public static void logRequest(String endpoint, Object request) {
        log.info("Request to {} - Starting", endpoint);
        logObjectAtDebugLevel("Request payload", request);
    }

    /**
     * Logs a response with standard format.
     *
     * @param endpoint endpoint that was called
     * @param response response object to log
     */
    public static void logResponse(String endpoint, Object response) {
        log.info("Response from {} - Completed", endpoint);
        logObjectAtDebugLevel("Response payload", response);
    }

    /**
     * Logs an error with standard format.
     *
     * @param endpoint endpoint where error occurred
     * @param errorMessage error description
     * @param exception exception that occurred
     */
    public static void logError(String endpoint, String errorMessage, Exception exception) {
        log.error("Error at {} - {}", endpoint, errorMessage, exception);
    }

    /**
     * Logs method entry with parameters at DEBUG level.
     *
     * @param methodName the name of the method being entered
     * @param methodParameters comma-separated parameter values
     */
    public static void logMethodEntry(String methodName, Object... methodParameters) {
        if (log.isDebugEnabled()) {
            log.debug("Entering method: {} with parameters: {}", methodName, (Object) methodParameters);
        }
    }

    /**
     * Logs method exit with return value at DEBUG level.
     *
     * @param methodName the name of the method being exited
     * @param returnValue the value being returned
     */
    public static void logMethodExit(String methodName, Object returnValue) {
        if (log.isDebugEnabled()) {
            log.debug("Exiting method: {} with return value: {}", methodName, returnValue);
        }
    }

    /**
     * Handles and logs JSON serialization errors gracefully.
     *
     * @param exception the JsonProcessingException that occurred
     * @param message the original log message prefix
     * @param object the object that failed to serialize
     */
    private static void logSerializationError(JsonProcessingException exception, String message, Object object) {
        log.warn("Failed to serialize object to JSON for logging: {}", exception.getMessage());
        if (object != null) {
            String objectIdentifier = object.getClass().getSimpleName() + "@" +
                    Integer.toHexString(System.identityHashCode(object));
            log.info("{}: [Serialization failed - {}]", message, objectIdentifier);
        }
    }
}
