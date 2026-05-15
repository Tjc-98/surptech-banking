package org.surptech.common.exception;

import lombok.Getter;

/**
 * Exception thrown when communication with an external service or microservice fails.
 * Includes detailed information about the failure including HTTP status code, service name, and endpoint.
 *
 * This exception is typically thrown by service clients when HTTP requests fail or
 * return error status codes.
 */
@Getter
public class ServiceCommunicationException extends RuntimeException {

    /** HTTP status code from the failed request, or 0 if not applicable */
    private final int statusCode;

    /** Name of the service that was being communicated with */
    private final String serviceName;

    /** The specific endpoint/URL that failed */
    private final String endpoint;

    /**
     * Creates a service communication exception with minimal information.
     * Maintains backward compatibility with older code.
     *
     * @param message the error message describing the communication failure
     * @param statusCode the HTTP status code from the failed request
     */
    public ServiceCommunicationException(String message, int statusCode) {
        this(message, statusCode, null, null);
    }

    /**
     * Creates a service communication exception with complete details.
     *
     * @param message the error message describing the communication failure
     * @param statusCode the HTTP status code from the failed request
     * @param serviceName the name of the service that failed to communicate
     * @param endpoint the endpoint URL that was being accessed
     */
    public ServiceCommunicationException(String message, int statusCode, String serviceName, String endpoint) {
        super(message);
        this.statusCode = statusCode;
        this.serviceName = serviceName;
        this.endpoint = endpoint;
    }

    /**
     * Creates a service communication exception with cause and minimal details.
     * Maintains backward compatibility with older code.
     *
     * @param message the error message describing the communication failure
     * @param statusCode the HTTP status code from the failed request
     * @param cause the underlying exception that caused the communication failure
     */
    public ServiceCommunicationException(String message, int statusCode, Throwable cause) {
        this(message, statusCode, null, null, cause);
    }

    /**
     * Creates a service communication exception with complete details and cause.
     *
     * @param message the error message describing the communication failure
     * @param statusCode the HTTP status code from the failed request
     * @param serviceName the name of the service that failed to communicate
     * @param endpoint the endpoint URL that was being accessed
     * @param cause the underlying exception that caused the communication failure
     */
    public ServiceCommunicationException(String message, int statusCode, String serviceName, String endpoint, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
        this.serviceName = serviceName;
        this.endpoint = endpoint;
    }
}
