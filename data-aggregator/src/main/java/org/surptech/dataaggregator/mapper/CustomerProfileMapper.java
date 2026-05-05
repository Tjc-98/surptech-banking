package org.surptech.dataaggregator.mapper;

import org.surptech.dataaggregator.domain.CustomerProfileResponse;
import org.surptech.dataaggregator.entity.CustomerProfileEntity;

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

    /**
     * Convert internal entity to response DTO
     */
    public static CustomerProfileResponse toResponse(CustomerProfileEntity entity) {
        if (entity == null) {
            return null;
        }
        
        return CustomerProfileResponse.builder()
                .socialSecurityNumber(entity.getSocialSecurityNumber())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .address(entity.getAddress())
                .build();
    }
}
