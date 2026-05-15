package org.surptech.common.exception;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Exception thrown when validation of input data fails.
 * Can contain a single error message or multiple validation error messages.
 *
 * This exception is a runtime exception (unchecked) to allow for flexible error handling
 * and clear separation of validation concerns from business logic.
 */
@Getter
public class ValidationException extends RuntimeException {

    /** List of validation error messages */
    private final List<String> errors;

    /**
     * Creates a validation exception with a single error message.
     *
     * @param message the error message describing the validation failure
     */
    public ValidationException(String message) {
        super(message);
        this.errors = List.of(message);
    }

    /**
     * Creates a validation exception with multiple error messages.
     * Useful for collecting multiple validation failures and reporting them together.
     *
     * @param errors list of error messages describing all validation failures
     */
    public ValidationException(List<String> errors) {
        super(String.join("; ", errors));
        this.errors = new ArrayList<>(errors);
    }

    /**
     * Creates a validation exception with a message and root cause.
     *
     * @param message the error message describing the validation failure
     * @param cause the underlying exception that caused this validation failure
     */
    public ValidationException(String message, Throwable cause) {
        super(message, cause);
        this.errors = List.of(message);
    }
}
