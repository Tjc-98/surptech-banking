/**
 * Response DTO for customer credit information API calls.
 * 
 * This interface matches the JSON structure returned by the data-aggregator service.
 * Uses snake_case field names to match the backend API contract.
 */
export interface CustomerCreditInfoResponse {
  /** The unique social security number of the customer */
  social_security_number: string;
  
  /** The customer's first name */
  first_name: string;
  
  /** The customer's last name */
  last_name: string;
  
  /** The customer's residential address */
  address: string;
  
  /** The full credit balance on the credit account */
  full_credit_balance: number;
  
  /** The available spending balance */
  spend_balance: number;
  
  /** The interest rate applied to the credit account */
  interest_rate: number;
}
