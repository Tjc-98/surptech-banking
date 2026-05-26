/**
 * Request DTO for fetching customer credit information.
 * 
 * This interface defines the structure for requesting customer banking data
 * from the data-aggregator service.
 */
export interface CustomerInfoRequest {
  /** The social security number to query (USA format: XXX-XX-XXXX) */
  social_security_number: string;
}
