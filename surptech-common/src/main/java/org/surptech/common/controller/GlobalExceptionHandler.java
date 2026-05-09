package org.surptech.common.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.surptech.common.domain.ErrorResponse;
import org.surptech.common.exception.ResourceNotFoundException;
import org.surptech.common.exception.ServiceCommunicationException;
import org.surptech.common.exception.ValidationException;

/**
 * Global exception handler for all REST controllers.
 * Converts exceptions into standardized error responses.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Error type constants for consistency
    private static final String ERROR_TYPE_NOT_FOUND = "Not Found";
    private static final String ERROR_TYPE_VALIDATION_FAILED = "Validation Failed";
    private static final String ERROR_TYPE_SERVICE_COMMUNICATION_ERROR = "Service Communication Error";
    private static final String ERROR_TYPE_INTERNAL_SERVER_ERROR = "Internal Server Error";

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
            ResourceNotFoundException exception, WebRequest request) {

        log.warn("Resource not found: {}", exception.getMessage());

        ErrorResponse errorResponse = ErrorResponse.of(
                HttpStatus.NOT_FOUND.value(),
                ERROR_TYPE_NOT_FOUND,
                exception.getMessage(),
                extractRequestPath(request)
        );
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            ValidationException exception, WebRequest request) {

        log.warn("Validation failed: {}", exception.getMessage());

        ErrorResponse errorResponse = ErrorResponse.of(
                HttpStatus.BAD_REQUEST.value(),
                ERROR_TYPE_VALIDATION_FAILED,
                exception.getMessage(),
                extractRequestPath(request),
                exception.getErrors()
        );
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(ServiceCommunicationException.class)
    public ResponseEntity<ErrorResponse> handleServiceCommunicationException(
            ServiceCommunicationException exception, WebRequest request) {

        log.error("Service communication error: {}", exception.getMessage());

        ErrorResponse errorResponse = ErrorResponse.of(
                exception.getStatusCode(),
                ERROR_TYPE_SERVICE_COMMUNICATION_ERROR,
                exception.getMessage(),
                extractRequestPath(request)
        );
        
        return ResponseEntity.status(exception.getStatusCode()).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(
            Exception exception, WebRequest request) {

        log.error("Unexpected error occurred", exception);

        ErrorResponse errorResponse = ErrorResponse.of(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ERROR_TYPE_INTERNAL_SERVER_ERROR,
                "An unexpected error occurred. Please try again later.",
                extractRequestPath(request)
        );
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    /**
     * Extracts the request path from WebRequest by removing the "uri=" prefix.
     *
     * @param request the web request
     * @return the extracted request path
     */
    private String extractRequestPath(WebRequest request) {
        return request.getDescription(false).replace("uri=", "");
    }
}
