/**
 * Custom React hook for fetching and managing customer credit information.
 * 
 * This hook encapsulates the state management and side effects for retrieving
 * customer data, providing a clean interface for components.
 * 
 * Follows React hooks best practices for data fetching and state management.
 */

import { useState, useCallback } from 'react';
import { CustomerCreditInfo } from '@/domain/entity/CustomerCreditInfo';
import { customerService } from '@/services/CustomerService';
import { ApiError } from '@/domain/error/ApiError';

/**
 * State interface for the customer credit info hook.
 */
interface UseCustomerCreditInfoState {
  /** The fetched customer data, null if not yet loaded or not found */
  data: CustomerCreditInfo | null;
  
  /** Loading state indicator */
  loading: boolean;
  
  /** Error object if the request failed */
  error: ApiError | null;
  
  /** Function to fetch customer data by SSN */
  fetchCustomerInfo: (ssn: string) => Promise<void>;
  
  /** Function to reset the state */
  reset: () => void;
}

/**
 * Hook for fetching customer credit information.
 * 
 * @returns Object containing data, loading state, error, and fetch function
 * 
 * @example
 * ```tsx
 * const { data, loading, error, fetchCustomerInfo } = useCustomerCreditInfo();
 * 
 * const handleSearch = async (ssn: string) => {
 *   await fetchCustomerInfo(ssn);
 * };
 * ```
 */
export const useCustomerCreditInfo = (): UseCustomerCreditInfoState => {
  const [data, setData] = useState<CustomerCreditInfo | null>(null);
  const [loading, setLoading] = useState<boolean>(false);
  const [error, setError] = useState<ApiError | null>(null);
  
  /**
   * Fetches customer credit information by social security number.
   */
  const fetchCustomerInfo = useCallback(async (ssn: string): Promise<void> => {
    setLoading(true);
    setError(null);
    setData(null);
    
    try {
      const customerInfo = await customerService.getCustomerCreditInfo(ssn);
      setData(customerInfo);
    } catch (err) {
      if (err instanceof ApiError) {
        setError(err);
      } else {
        setError(new ApiError('An unexpected error occurred', 500));
      }
    } finally {
      setLoading(false);
    }
  }, []);
  
  /**
   * Resets the hook state to initial values.
   */
  const reset = useCallback((): void => {
    setData(null);
    setLoading(false);
    setError(null);
  }, []);
  
  return {
    data,
    loading,
    error,
    fetchCustomerInfo,
    reset,
  };
};
