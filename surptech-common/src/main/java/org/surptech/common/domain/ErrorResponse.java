package org.surptech.common.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
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
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
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
