/**
 * Service layer for customer-related business logic.
 * 
 * This service orchestrates API calls and data transformations,
 * providing a clean interface for components to interact with customer data.
 * 
 * Follows the service pattern from the backend architecture.
 */

import { dataAggregatorClient } from '@/client/DataAggregatorClient';
import { CustomerCreditInfo } from '@/domain/entity/CustomerCreditInfo';
import { mapToCustomerCreditInfo } from '@/mapper/CustomerCreditInfoMapper';
import { ApiError } from '@/domain/error/ApiError';

/**
 * Service for managing customer credit information operations.
 */
export class CustomerService {
  /**
   * Retrieves aggregated customer and credit information by SSN.
   * 
   * This method fetches data from the data-aggregator service and transforms
   * it into a domain entity for use in the application.
   * 
   * @param socialSecurityNumber - The SSN to query (format: XXX-XX-XXXX)
   * @returns Promise resolving to customer credit information entity
   * @throws ApiError if the request fails or customer is not found
   */
  public async getCustomerCreditInfo(
    socialSecurityNumber: string
  ): Promise<CustomerCreditInfo> {
    try {
      const response = await dataAggregatorClient.getCustomerCreditInfo(
        socialSecurityNumber
      );
      
      return mapToCustomerCreditInfo(response);
    } catch (error) {
      if (error instanceof ApiError) {
        throw error;
      }
      
      // Wrap unexpected errors
      throw new ApiError(
        'Failed to retrieve customer information',
        500,
        error
      );
    }
  }
  
  /**
   * Checks if the data aggregator service is available.
   * 
   * @returns Promise resolving to true if service is healthy, false otherwise
   */
  public async isServiceAvailable(): Promise<boolean> {
    try {
      const health = await dataAggregatorClient.checkHealth();
      return health.status === 'UP';
    } catch {
      return false;
    }
  }
}

/**
 * Singleton instance of the CustomerService.
 * Use this instance throughout the application for consistency.
 */
export const customerService = new CustomerService();
