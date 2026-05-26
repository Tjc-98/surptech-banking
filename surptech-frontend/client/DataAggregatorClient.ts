/**
 * HTTP Client for the Data Aggregator Service.
 * 
 * This client encapsulates all HTTP communication with the data-aggregator backend service.
 * It handles request construction, error handling, and response parsing.
 * 
 * Follows the same client pattern as the backend's RestClient implementations.
 */

import { ApiConfig, buildApiUrl } from '@/config/api.config';
import { CustomerCreditInfoResponse } from '@/domain/response/CustomerCreditInfoResponse';
import { ApiError } from '@/domain/error/ApiError';

/**
 * Client for interacting with the Data Aggregator Service API.
 */
export class DataAggregatorClient {
  /**
   * Fetches customer credit information by social security number.
   * 
   * @param socialSecurityNumber - The SSN to query (format: XXX-XX-XXXX)
   * @returns Promise resolving to customer credit information
   * @throws ApiError if the request fails or returns an error status
   */
  public async getCustomerCreditInfo(
    socialSecurityNumber: string
  ): Promise<CustomerCreditInfoResponse> {
    const url = buildApiUrl(ApiConfig.endpoints.customerInfo);
    const queryParams = new URLSearchParams({
      socialSecurityNumber: socialSecurityNumber,
    });
    
    const fullUrl = `${url}?${queryParams.toString()}`;
    
    try {
      const controller = new AbortController();
      const timeoutId = setTimeout(() => controller.abort(), ApiConfig.timeout);
      
      const response = await fetch(fullUrl, {
        method: 'GET',
        headers: ApiConfig.defaultHeaders,
        signal: controller.signal,
      });
      
      clearTimeout(timeoutId);
      
      if (!response.ok) {
        await this.handleErrorResponse(response);
      }
      
      const data: CustomerCreditInfoResponse = await response.json();
      return data;
    } catch (error) {
      if (error instanceof ApiError) {
        throw error;
      }
      
      // Handle network errors, timeouts, etc.
      if (error instanceof Error) {
        if (error.name === 'AbortError') {
          throw new ApiError('Request timeout', 408);
        }
        throw new ApiError(`Network error: ${error.message}`, 0);
      }
      
      throw new ApiError('Unknown error occurred', 0);
    }
  }
  
  /**
   * Checks the health status of the data aggregator service.
   * 
   * @returns Promise resolving to health status object
   * @throws ApiError if the health check fails
   */
  public async checkHealth(): Promise<{ status: string }> {
    const url = buildApiUrl(ApiConfig.endpoints.health);
    
    try {
      const response = await fetch(url, {
        method: 'GET',
        headers: ApiConfig.defaultHeaders,
      });
      
      if (!response.ok) {
        throw new ApiError('Health check failed', response.status);
      }
      
      return await response.json();
    } catch (error) {
      if (error instanceof ApiError) {
        throw error;
      }
      throw new ApiError('Health check failed', 0);
    }
  }
  
  /**
   * Handles error responses from the API.
   * 
   * @param response - The failed HTTP response
   * @throws ApiError with appropriate message and status code
   */
  private async handleErrorResponse(response: Response): Promise<never> {
    let errorMessage = `Request failed with status ${response.status}`;
    let errorDetails: unknown = undefined;
    
    try {
      const contentType = response.headers.get('content-type');
      if (contentType && contentType.includes('application/json')) {
        errorDetails = await response.json();
        if (errorDetails && typeof errorDetails === 'object' && 'message' in errorDetails) {
          errorMessage = String(errorDetails.message);
        }
      } else {
        errorMessage = await response.text() || errorMessage;
      }
    } catch {
      // If parsing fails, use default error message
    }
    
    throw new ApiError(errorMessage, response.status, errorDetails);
  }
}

/**
 * Singleton instance of the DataAggregatorClient.
 * Use this instance throughout the application for consistency.
 */
export const dataAggregatorClient = new DataAggregatorClient();
