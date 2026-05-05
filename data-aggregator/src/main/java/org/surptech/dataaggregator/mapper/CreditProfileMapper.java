package org.surptech.dataaggregator.mapper;

import org.surptech.dataaggregator.domain.CreditProfileResponse;
import org.surptech.dataaggregator.entity.CreditProfileEntity;

/**
 * Mapper to convert between credit profile DTOs and internal entities.
 */
public class CreditProfileMapper {

    /**
     * Convert response DTO to internal entity
     */
    public static CreditProfileEntity toEntity(CreditProfileResponse response) {
        if (response == null) {
            return null;
        }
        
        return CreditProfileEntity.builder()
                .socialSecurityNumber(response.getSocialSecurityNumber())
                .currentBalance(response.getCurrentBalance())
                .spendBalance(response.getSpendBalance())
                .interestRate(response.getInterestRate())
                .build();
    }

    /**
     * Convert internal entity to response DTO
     */
    public static CreditProfileResponse toResponse(CreditProfileEntity entity) {
        if (entity == null) {
            return null;
        }
        
        return CreditProfileResponse.builder()
                .socialSecurityNumber(entity.getSocialSecurityNumber())
                .currentBalance(entity.getCurrentBalance())
                .spendBalance(entity.getSpendBalance())
                .interestRate(entity.getInterestRate())
                .build();
    }
}
