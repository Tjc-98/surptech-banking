/**
 * Validation utilities for form inputs and data validation.
 * 
 * Provides client-side validation functions that mirror the backend validation logic.
 */

/**
 * Validates a USA format social security number.
 * 
 * Expected format: XXX-XX-XXXX where X is a digit.
 * 
 * @param ssn - The social security number to validate
 * @returns true if valid, false otherwise
 */
export const isValidSocialSecurityNumber = (ssn: string): boolean => {
  if (!ssn || typeof ssn !== 'string') {
    return false;
  }
  
  // USA SSN format: XXX-XX-XXXX
  const ssnPattern = /^\d{3}-\d{2}-\d{4}$/;
  return ssnPattern.test(ssn.trim());
};

/**
 * Validates that a string is not empty or only whitespace.
 * 
 * @param value - The string to validate
 * @returns true if the string has content, false otherwise
 */
export const isNotEmpty = (value: string): boolean => {
  return value != null && value.trim().length > 0;
};

/**
 * Formats a social security number by adding dashes if missing.
 * 
 * @param ssn - The SSN to format (can be with or without dashes)
 * @returns Formatted SSN in XXX-XX-XXXX format, or original if invalid
 */
export const formatSocialSecurityNumber = (ssn: string): string => {
  if (!ssn) return ssn;
  
  // Remove all non-digit characters
  const digitsOnly = ssn.replace(/\D/g, '');
  
  // Check if we have exactly 9 digits
  if (digitsOnly.length !== 9) {
    return ssn;
  }
  
  // Format as XXX-XX-XXXX
  return `${digitsOnly.slice(0, 3)}-${digitsOnly.slice(3, 5)}-${digitsOnly.slice(5)}`;
};

/**
 * Validation error messages.
 */
export const ValidationMessages = {
  SSN_REQUIRED: 'Social Security Number is required',
  SSN_INVALID_FORMAT: 'Social Security Number must be in format XXX-XX-XXXX',
  FIELD_REQUIRED: (fieldName: string) => `${fieldName} is required`,
} as const;
