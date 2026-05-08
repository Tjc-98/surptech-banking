package org.surptech.common.exception;

import lombok.Getter;

/**
 * Exception thrown when communication with an external service fails.
 * Includes HTTP status code for detailed error handling.
 */
@Getter
public class ServiceCommunicationException extends RuntimeException {

    private final int statusCode;

    public ServiceCommunicationException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public ServiceCommunicationException(String message, int statusCode, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
    }
}
