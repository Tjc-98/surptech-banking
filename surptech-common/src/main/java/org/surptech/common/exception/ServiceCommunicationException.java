package org.surptech.common.exception;

import lombok.Getter;

/**
 * Exception thrown when communication with an external service fails.
 * Includes HTTP status code, service name, and endpoint for detailed error handling.
 */
@Getter
public class ServiceCommunicationException extends RuntimeException {

    private final int statusCode;
    private final String serviceName;
    private final String endpoint;

    /**
     * Creates a ServiceCommunicationException with minimal information.
     * Maintains backward compatibility.
     *
     * @param message the error message
     * @param statusCode the HTTP status code
     */
    public ServiceCommunicationException(String message, int statusCode) {
        this(message, statusCode, null, null);
    }

    /**
     * Creates a ServiceCommunicationException with all details.
     *
     * @param message the error message
     * @param statusCode the HTTP status code
     * @param serviceName the name of the service that failed
     * @param endpoint the endpoint that failed
     */
    public ServiceCommunicationException(String message, int statusCode, String serviceName, String endpoint) {
        super(message);
        this.statusCode = statusCode;
        this.serviceName = serviceName;
        this.endpoint = endpoint;
    }

    /**
     * Creates a ServiceCommunicationException with cause.
     * Maintains backward compatibility.
     *
     * @param message the error message
     * @param statusCode the HTTP status code
     * @param cause the underlying exception
     */
    public ServiceCommunicationException(String message, int statusCode, Throwable cause) {
        this(message, statusCode, null, null, cause);
    }

    /**
     * Creates a ServiceCommunicationException with all details and cause.
     *
     * @param message the error message
     * @param statusCode the HTTP status code
     * @param serviceName the name of the service that failed
     * @param endpoint the endpoint that failed
     * @param cause the underlying exception
     */
    public ServiceCommunicationException(String message, int statusCode, String serviceName, String endpoint, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
        this.serviceName = serviceName;
        this.endpoint = endpoint;
    }
}
