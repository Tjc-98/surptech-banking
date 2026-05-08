package org.surptech.common.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Standardized error response structure for all services.
 * Provides consistent error information to clients.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    @JsonProperty("timestamp")
    private LocalDateTime timestamp;

    @JsonProperty("status")
    private int status;

    @JsonProperty("error")
    private String error;

    @JsonProperty("message")
    private String message;

    @JsonProperty("path")
    private String path;

    @JsonProperty("errors")
    private List<String> errors;

    /**
     * Creates an ErrorResponse with current timestamp.
     *
     * @param status HTTP status code
     * @param error error type
     * @param message error message
     * @param path request path
     * @return ErrorResponse instance
     */
    public static ErrorResponse of(int status, String error, String message, String path) {
        return ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status)
                .error(error)
                .message(message)
                .path(path)
                .build();
    }

    /**
     * Creates an ErrorResponse with multiple error messages.
     *
     * @param status HTTP status code
     * @param error error type
     * @param message main error message
     * @param path request path
     * @param errors list of detailed error messages
     * @return ErrorResponse instance
     */
    public static ErrorResponse of(int status, String error, String message, String path, List<String> errors) {
        return ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status)
                .error(error)
                .message(message)
                .path(path)
                .errors(errors)
                .build();
    }
}
