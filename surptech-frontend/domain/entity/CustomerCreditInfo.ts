/**
 * Entity representing aggregated customer and credit information.
 * 
 * This entity combines customer profile data with credit profile data,
 * providing a complete view of a customer's banking information.
 * 
 * Corresponds to the backend CustomerCreditInfoResponse from the data-aggregator service.
 */
export interface CustomerCreditInfo {
  /** The unique social security number of the customer (USA format: XXX-XX-XXXX) */
  socialSecurityNumber: string;
  
  /** The customer's first name */
  firstName: string;
  
  /** The customer's last name */
  lastName: string;
  
  /** The customer's residential address */
  address: string;
  
  /** The full credit balance on the credit account */
  fullCreditBalance: number;
  
  /** The available spending balance */
  spendBalance: number;
  
  /** The interest rate applied to the credit account (as a decimal, e.g., 0.15 for 15%) */
  interestRate: number;
}
