package org.surptech.common.exception;

/**
 * Exception thrown when a requested resource cannot be found.
 * This is a runtime exception used to indicate that a resource lookup failed.
 *
 * Provides convenience constructors for creating descriptive error messages with either
 * explicit messages or constructed messages based on resource type and identifier.
 */
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Creates a resource not found exception with a custom message.
     *
     * @param message the error message describing which resource was not found
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }

    /**
     * Creates a resource not found exception using the resource type and identifier.
     * Automatically formats a descriptive message.
     *
     * @param resourceType the type of resource that was not found (e.g., "Customer", "Profile")
     * @param identifier the identifier being searched for (e.g., SSN, ID)
     */
    public ResourceNotFoundException(String resourceType, String identifier) {
        super(String.format("%s not found with identifier: %s", resourceType, identifier));
    }

    /**
     * Creates a resource not found exception with a message and root cause.
     *
     * @param message the error message describing which resource was not found
     * @param cause the underlying exception that caused this lookup failure
     */
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
