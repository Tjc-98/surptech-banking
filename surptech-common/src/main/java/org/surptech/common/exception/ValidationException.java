package org.surptech.common.exception;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Exception thrown when validation fails.
 * Can contain multiple validation error messages.
 */
@Getter
public class ValidationException extends RuntimeException {

    private final List<String> errors;

    public ValidationException(String message) {
        super(message);
        this.errors = List.of(message);
    }

    public ValidationException(List<String> errors) {
        super(String.join("; ", errors));
        this.errors = new ArrayList<>(errors);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
        this.errors = List.of(message);
    }
}
