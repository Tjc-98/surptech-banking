package org.surptech.dataaggregator.mapper;

import org.surptech.dataaggregator.domain.response.CustomerCreditInfoResponse;
import org.surptech.dataaggregator.domain.entity.CustomerCreditInfoEntity;

/**
 * Mapper to convert between aggregated customer credit info DTOs and internal entities.
 */
public class CustomerCreditInfoMapper {

    /**
     * Convert internal entity to response DTO
     */
    public static CustomerCreditInfoResponse toResponse(CustomerCreditInfoEntity entity) {
        if (entity == null) {
            return null;
        }
        
        return CustomerCreditInfoResponse.builder()
                .socialSecurityNumber(entity.getSocialSecurityNumber())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .address(entity.getAddress())
                .fullCreditBalance(entity.getFullCreditBalance())
                .spendBalance(entity.getSpendBalance())
                .interestRate(entity.getInterestRate())
                .build();
    }
}
