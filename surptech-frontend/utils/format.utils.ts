/**
 * Formatting utilities for displaying data in the UI.
 * 
 * Provides consistent formatting functions for currency, percentages, and other data types.
 */

/**
 * Formats a number as USD currency.
 * 
 * @param amount - The amount to format
 * @returns Formatted currency string (e.g., "$1,234.56")
 */
export const formatCurrency = (amount: number): string => {
  return new Intl.NumberFormat('en-US', {
    style: 'currency',
    currency: 'USD',
    minimumFractionDigits: 2,
    maximumFractionDigits: 2,
  }).format(amount);
};

/**
 * Formats a decimal as a percentage.
 * 
 * @param rate - The rate as a decimal (e.g., 0.15 for 15%)
 * @param decimals - Number of decimal places to show (default: 2)
 * @returns Formatted percentage string (e.g., "15.00%")
 */
export const formatPercentage = (rate: number, decimals: number = 2): string => {
  return `${(rate * 100).toFixed(decimals)}%`;
};

/**
 * Formats a full name from first and last name.
 * 
 * @param firstName - The first name
 * @param lastName - The last name
 * @returns Full name in "First Last" format
 */
export const formatFullName = (firstName: string, lastName: string): string => {
  return `${firstName} ${lastName}`;
};

/**
 * Masks a social security number for display, showing only last 4 digits.
 * 
 * @param ssn - The SSN to mask (format: XXX-XX-XXXX)
 * @returns Masked SSN (format: ***-**-XXXX)
 */
export const maskSocialSecurityNumber = (ssn: string): string => {
  if (!ssn || ssn.length < 4) {
    return '***-**-****';
  }
  
  // If SSN is in XXX-XX-XXXX format
  if (ssn.includes('-')) {
    const parts = ssn.split('-');
    if (parts.length === 3) {
      return `***-**-${parts[2]}`;
    }
  }
  
  // If SSN is just digits, show last 4
  return `***-**-${ssn.slice(-4)}`;
};

/**
 * Truncates a long address for display.
 * 
 * @param address - The address to truncate
 * @param maxLength - Maximum length before truncation (default: 50)
 * @returns Truncated address with ellipsis if needed
 */
export const truncateAddress = (address: string, maxLength: number = 50): string => {
  if (!address || address.length <= maxLength) {
    return address;
  }
  
  return `${address.slice(0, maxLength)}...`;
};
