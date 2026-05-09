package org.surptech.common.validation;

import org.surptech.common.exception.ValidationException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Utility class for common validation operations.
 * Provides reusable validation methods for various data types.
 *
 * Note: Email validation uses a simplified regex pattern. For production systems,
 * consider using Apache Commons EmailValidator or similar libraries for RFC 5322 compliance.
 */
public class ValidationUtils {

    // SSN Pattern: USA format (XXX-XX-XXXX)
    private static final Pattern SSN_PATTERN_USA = Pattern.compile("^\\d{3}-\\d{2}-\\d{4}$");

    // Email pattern: Supports common email formats including subdomains
    // Pattern includes: local-part@domain.co.uk, user.name@example.com, etc.
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9][A-Za-z0-9+._%-]*@[A-Za-z0-9][A-Za-z0-9.-]*\\.[A-Za-z]{2,}$");

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
     * Validates a Social Security Number in USA format (XXX-XX-XXXX).
     * This method validates USA SSN format only.
     *
     * @param socialSecurityNumber the SSN to validate
     * @throws ValidationException if validation fails
     */
    public static void validateSocialSecurityNumber(String socialSecurityNumber) {
        validateSocialSecurityNumber(socialSecurityNumber, "USA");
    }

    /**
     * Validates a Social Security Number for a specific region/country.
     * Currently supports USA format (XXX-XX-XXXX).
     *
     * @param socialSecurityNumber the SSN to validate
     * @param region the region or country code (e.g., "USA")
     * @throws ValidationException if validation fails or region is not supported
     */
    public static void validateSocialSecurityNumber(String socialSecurityNumber, String region) {
        validateNotEmpty(socialSecurityNumber, "Social Security Number");

        switch (region.toUpperCase()) {
            case "USA" -> {
                if (!SSN_PATTERN_USA.matcher(socialSecurityNumber).matches()) {
                    throw new ValidationException("Social Security Number must be in USA format: XXX-XX-XXXX");
                }
            }
            default -> throw new ValidationException("Social Security Number validation for region '" + region + "' is not supported");
        }
    }

    /**
     * Validates an email address format.
     * Note: This uses a simplified regex pattern. The pattern supports common formats but
     * may not fully comply with RFC 5322. For strict compliance, use a dedicated email validator.
     *
     * @param email the email to validate
     * @throws ValidationException if validation fails
     */
    public static void validateEmail(String email) {
        validateNotEmpty(email, "Email");
        
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new ValidationException("Invalid email format. Expected format: user@example.com");
        }
    }

    /**
     * Validates that a number is positive (greater than 0).
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
     * Validates that a number is non-negative (greater than or equal to 0).
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
     * @param minimumLength minimum length (inclusive)
     * @param maximumLength maximum length (inclusive)
     * @throws ValidationException if validation fails
     */
    public static void validateLength(String value, String fieldName, int minimumLength, int maximumLength) {
        validateNotEmpty(value, fieldName);
        
        int length = value.length();
        if (length < minimumLength || length > maximumLength) {
            throw new ValidationException(
                    String.format("%s length must be between %d and %d characters, but got %d",
                            fieldName, minimumLength, maximumLength, length)
            );
        }
    }

    /**
     * Collects multiple validation errors and throws a single ValidationException if any validation fails.
     * This allows validation of multiple fields before throwing an exception.
     *
     * @param validations list of validation runnables to execute
     * @throws ValidationException if any validation fails, containing all accumulated errors
     */
    public static void validateAll(Runnable... validations) {
        List<String> errors = new ArrayList<>();
        
        for (Runnable validation : validations) {
            try {
                validation.run();
            } catch (ValidationException exception) {
                errors.addAll(exception.getErrors());
            }
        }
        
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }
}
