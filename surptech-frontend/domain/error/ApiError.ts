/**
 * Represents an error that occurred during an API call.
 * 
 * This class encapsulates error information from failed HTTP requests,
 * providing a consistent error handling interface across the application.
 */
export class ApiError extends Error {
  /** HTTP status code of the error response */
  public readonly statusCode: number;
  
  /** Additional error details or context */
  public readonly details?: unknown;
  
  /**
   * Creates a new ApiError instance.
   * 
   * @param message - Human-readable error message
   * @param statusCode - HTTP status code
   * @param details - Optional additional error information
   */
  constructor(message: string, statusCode: number, details?: unknown) {
    super(message);
    this.name = 'ApiError';
    this.statusCode = statusCode;
    this.details = details;
    
    // Maintains proper stack trace for where error was thrown (V8 only)
    if (Error.captureStackTrace) {
      Error.captureStackTrace(this, ApiError);
    }
  }
  
  /**
   * Checks if this is a client error (4xx status code).
   */
  public isClientError(): boolean {
    return this.statusCode >= 400 && this.statusCode < 500;
  }
  
  /**
   * Checks if this is a server error (5xx status code).
   */
  public isServerError(): boolean {
    return this.statusCode >= 500 && this.statusCode < 600;
  }
  
  /**
   * Checks if this is a not found error (404).
   */
  public isNotFound(): boolean {
    return this.statusCode === 404;
  }
}
