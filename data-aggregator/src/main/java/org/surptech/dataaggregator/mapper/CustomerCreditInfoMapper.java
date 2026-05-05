package org.surptech.dataaggregator.mapper;

import org.surptech.dataaggregator.domain.CustomerCreditInfoResponse;
import org.surptech.dataaggregator.entity.CustomerCreditInfoEntity;

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
                .currentBalance(entity.getCurrentBalance())
                .spendBalance(entity.getSpendBalance())
                .interestRate(entity.getInterestRate())
                .build();
    }

    /**
     * Convert response DTO to internal entity
     */
    public static CustomerCreditInfoEntity toEntity(CustomerCreditInfoResponse response) {
        if (response == null) {
            return null;
        }
        
        return CustomerCreditInfoEntity.builder()
                .socialSecurityNumber(response.getSocialSecurityNumber())
                .firstName(response.getFirstName())
                .lastName(response.getLastName())
                .address(response.getAddress())
                .currentBalance(response.getCurrentBalance())
                .spendBalance(response.getSpendBalance())
                .interestRate(response.getInterestRate())
                .build();
    }
}
