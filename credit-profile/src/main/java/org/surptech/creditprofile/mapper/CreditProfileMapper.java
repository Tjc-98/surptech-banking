package org.surptech.creditprofile.mapper;

import org.surptech.creditprofile.domain.request.CreditProfileRequest;
import org.surptech.creditprofile.domain.response.CreditProfileResponse;
import org.surptech.creditprofile.domain.entity.CreditProfileEntity;

/**
 * Mapper to convert between DTOs and internal entities for credit profile operations.
 *
 * This mapper provides one-way conversion methods:
 * - CreditProfileRequest (DTO) -> CreditProfileEntity (internal)
 * - CreditProfileEntity (internal) -> CreditProfileResponse (DTO)
 */
public class CreditProfileMapper {

    private CreditProfileMapper() {
        // Utility class - should not be instantiated
    }

    /**
     * Converts a CreditProfileRequest DTO to an internal CreditProfileEntity.
     *
     * @param request the request DTO to convert
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
     * Converts a CreditProfileEntity to a CreditProfileResponse DTO.
     *
     * @param entity the entity to convert
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
