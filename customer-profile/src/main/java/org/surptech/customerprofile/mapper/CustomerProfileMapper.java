package org.surptech.customerprofile.mapper;

import org.surptech.customerprofile.domain.request.CustomerProfileRequest;
import org.surptech.customerprofile.domain.response.CustomerProfileResponse;
import org.surptech.customerprofile.domain.entity.CustomerProfileEntity;

/**
 * Mapper to convert between DTOs and internal entities.
 */
public class CustomerProfileMapper {

    /**
     * Convert request DTO to internal entity
     */
    public static CustomerProfileEntity toEntity(CustomerProfileRequest request) {
        if (request == null) {
            return null;
        }
        
        return CustomerProfileEntity.builder()
                .socialSecurityNumber(request.getSocialSecurityNumber())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .address(request.getAddress())
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

    /**
     * Update entity from request DTO
     */
    public static void updateEntityFromRequest(CustomerProfileEntity entity, CustomerProfileRequest request) {
        if (entity == null || request == null) {
            return;
        }
        
        entity.setSocialSecurityNumber(request.getSocialSecurityNumber());
        entity.setFirstName(request.getFirstName());
        entity.setLastName(request.getLastName());
        entity.setAddress(request.getAddress());
    }
}
