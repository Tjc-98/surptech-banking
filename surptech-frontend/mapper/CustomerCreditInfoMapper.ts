/**
 * Mapper for converting between API response DTOs and domain entities.
 * 
 * This mapper transforms snake_case API responses to camelCase domain entities,
 * following the separation of concerns principle and keeping API contracts
 * separate from internal domain models.
 */

import { CustomerCreditInfo } from '@/domain/entity/CustomerCreditInfo';
import { CustomerCreditInfoResponse } from '@/domain/response/CustomerCreditInfoResponse';

/**
 * Maps a CustomerCreditInfoResponse (API DTO) to a CustomerCreditInfo entity (domain model).
 * 
 * Transforms field names from snake_case (API convention) to camelCase (TypeScript convention).
 * 
 * @param response - The API response object with snake_case fields
 * @returns Domain entity with camelCase fields
 */
export const mapToCustomerCreditInfo = (
  response: CustomerCreditInfoResponse
): CustomerCreditInfo => {
  return {
    socialSecurityNumber: response.social_security_number,
    firstName: response.first_name,
    lastName: response.last_name,
    address: response.address,
    fullCreditBalance: response.full_credit_balance,
    spendBalance: response.spend_balance,
    interestRate: response.interest_rate,
  };
};

/**
 * Maps a CustomerCreditInfo entity to a CustomerCreditInfoResponse (for potential POST requests).
 * 
 * Transforms field names from camelCase (TypeScript convention) to snake_case (API convention).
 * 
 * @param entity - The domain entity with camelCase fields
 * @returns API DTO with snake_case fields
 */
export const mapFromCustomerCreditInfo = (
  entity: CustomerCreditInfo
): CustomerCreditInfoResponse => {
  return {
    social_security_number: entity.socialSecurityNumber,
    first_name: entity.firstName,
    last_name: entity.lastName,
    address: entity.address,
    full_credit_balance: entity.fullCreditBalance,
    spend_balance: entity.spendBalance,
    interest_rate: entity.interestRate,
  };
};
