package org.surptech.dataaggregator.mapper;

import org.surptech.dataaggregator.domain.response.CreditProfileResponse;
import org.surptech.dataaggregator.domain.entity.CreditProfileEntity;

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
}
