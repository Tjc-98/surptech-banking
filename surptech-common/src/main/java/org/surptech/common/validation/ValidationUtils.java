package org.surptech.common.validation;

import org.surptech.common.exception.ValidationException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Utility class for common validation operations.
 * Provides reusable validation methods for various data types.
 */
public class ValidationUtils {

    private static final Pattern SSN_PATTERN = Pattern.compile("^\\d{3}-\\d{2}-\\d{4}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private ValidationUtils() {
        // Utility class - prevent instantiation
    }

    /**
     * Validates that a string is not null or empty.
     *
     * @param value the value to validate
     * @param fieldName the field name for error messages
     * @throws ValidationException if validation fails
     */
    public static void validateNotEmpty(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new ValidationException(fieldName + " cannot be null or empty");
        }
    }

    /**
     * Validates that an object is not null.
     *
     * @param value the value to validate
     * @param fieldName the field name for error messages
     * @throws ValidationException if validation fails
     */
    public static void validateNotNull(Object value, String fieldName) {
        if (value == null) {
            throw new ValidationException(fieldName + " cannot be null");
        }
    }

    /**
     * Validates a Social Security Number format (XXX-XX-XXXX).
     *
     * @param ssn the SSN to validate
     * @throws ValidationException if validation fails
     */
    public static void validateSocialSecurityNumber(String ssn) {
        validateNotEmpty(ssn, "Social Security Number");
        
        if (!SSN_PATTERN.matcher(ssn).matches()) {
            throw new ValidationException("Social Security Number must be in format XXX-XX-XXXX");
        }
    }

    /**
     * Validates an email address format.
     *
     * @param email the email to validate
     * @throws ValidationException if validation fails
     */
    public static void validateEmail(String email) {
        validateNotEmpty(email, "Email");
        
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new ValidationException("Invalid email format");
        }
    }

    /**
     * Validates that a number is positive.
     *
     * @param value the value to validate
     * @param fieldName the field name for error messages
     * @throws ValidationException if validation fails
     */
    public static void validatePositive(Number value, String fieldName) {
        validateNotNull(value, fieldName);
        
        if (value.doubleValue() <= 0) {
            throw new ValidationException(fieldName + " must be positive");
        }
    }

    /**
     * Validates that a number is non-negative.
     *
     * @param value the value to validate
     * @param fieldName the field name for error messages
     * @throws ValidationException if validation fails
     */
    public static void validateNonNegative(Number value, String fieldName) {
        validateNotNull(value, fieldName);
        
        if (value.doubleValue() < 0) {
            throw new ValidationException(fieldName + " cannot be negative");
        }
    }

    /**
     * Validates string length.
     *
     * @param value the value to validate
     * @param fieldName the field name for error messages
     * @param minLength minimum length (inclusive)
     * @param maxLength maximum length (inclusive)
     * @throws ValidationException if validation fails
     */
    public static void validateLength(String value, String fieldName, int minLength, int maxLength) {
        validateNotEmpty(value, fieldName);
        
        int length = value.length();
        if (length < minLength || length > maxLength) {
            throw new ValidationException(
                    String.format("%s length must be between %d and %d characters", fieldName, minLength, maxLength)
            );
        }
    }

    /**
     * Collects multiple validation errors and throws a single ValidationException.
     *
     * @param validations list of validation runnables
     * @throws ValidationException if any validation fails
     */
    public static void validateAll(Runnable... validations) {
        List<String> errors = new ArrayList<>();
        
        for (Runnable validation : validations) {
            try {
                validation.run();
            } catch (ValidationException e) {
                errors.addAll(e.getErrors());
            }
        }
        
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }
}
