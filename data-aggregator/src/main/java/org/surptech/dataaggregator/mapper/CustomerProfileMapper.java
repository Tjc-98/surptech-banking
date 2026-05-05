package org.surptech.dataaggregator.mapper;

import org.surptech.dataaggregator.domain.response.CustomerProfileResponse;
import org.surptech.dataaggregator.domain.entity.CustomerProfileEntity;

/**
 * Mapper to convert between customer profile DTOs and internal entities.
 */
public class CustomerProfileMapper {

    /**
     * Convert response DTO to internal entity
     */
    public static CustomerProfileEntity toEntity(CustomerProfileResponse response) {
        if (response == null) {
            return null;
        }
        
        return CustomerProfileEntity.builder()
                .socialSecurityNumber(response.getSocialSecurityNumber())
                .firstName(response.getFirstName())
                .lastName(response.getLastName())
                .address(response.getAddress())
                .build();
    }
}
