package org.surptech.creditprofile.mapper;

import org.surptech.creditprofile.domain.request.CreditProfileRequest;
import org.surptech.creditprofile.domain.response.CreditProfileResponse;
import org.surptech.creditprofile.domain.entity.CreditProfileEntity;

/**
 * Mapper for converting between different representations of credit profile data.
 * Provides one-way conversion methods:
 * - {@link #toEntity(CreditProfileRequest)} converts incoming request DTOs to internal entities
 * - {@link #toResponse(CreditProfileEntity)} converts internal entities to outgoing response DTOs
 *
 * This mapper separates API contracts (DTOs) from internal domain models (entities).
 * Null-safe: returns null if input is null.
 */
public class CreditProfileMapper {

    private CreditProfileMapper() {
        // Utility class - prevent instantiation
    }

    /**
     * Converts a credit profile request DTO to an internal entity for persistence operations.
     *
     * @param request the incoming request DTO to convert
     * @return the converted entity, or null if the request is null
     */
    public static CreditProfileEntity toEntity(CreditProfileRequest request) {
        if (request == null) {
            return null;
        }
        
        return CreditProfileEntity.builder()
                .socialSecurityNumber(request.getSocialSecurityNumber())
                .fullCreditBalance(request.getFullCreditBalance())
                .spendBalance(request.getSpendBalance())
                .interestRate(request.getInterestRate())
                .build();
    }

    /**
     * Converts an internal entity to a credit profile response DTO for API responses.
     *
     * @param entity the internal entity to convert
     * @return the converted response DTO, or null if the entity is null
     */
    public static CreditProfileResponse toResponse(CreditProfileEntity entity) {
        if (entity == null) {
            return null;
        }
        
        return CreditProfileResponse.builder()
                .socialSecurityNumber(entity.getSocialSecurityNumber())
                .fullCreditBalance(entity.getFullCreditBalance())
                .spendBalance(entity.getSpendBalance())
                .interestRate(entity.getInterestRate())
                .build();
    }
}
