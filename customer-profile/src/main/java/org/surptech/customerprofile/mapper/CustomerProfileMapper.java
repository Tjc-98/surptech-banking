package org.surptech.customerprofile.mapper;

import org.surptech.customerprofile.domain.request.CustomerProfileRequest;
import org.surptech.customerprofile.domain.response.CustomerProfileResponse;
import org.surptech.customerprofile.domain.entity.CustomerProfileEntity;

/**
 * Mapper for converting between different representations of customer profile data.
 * Provides one-way conversion methods:
 * - {@link #toEntity(CustomerProfileRequest)} converts incoming request DTOs to internal entities
 * - {@link #toResponse(CustomerProfileEntity)} converts internal entities to outgoing response DTOs
 *
 * This mapper separates API contracts (DTOs) from internal domain models (entities).
 * Null-safe: returns null if input is null.
 */
public class CustomerProfileMapper {

    private CustomerProfileMapper() {
        // Utility class - prevent instantiation
    }

    /**
     * Converts a customer profile request DTO to an internal entity for persistence operations.
     *
     * @param request the incoming request DTO to convert
     * @return the converted entity, or null if the request is null
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
     * Converts an internal entity to a customer profile response DTO for API responses.
     *
     * @param entity the internal entity to convert
     * @return the converted response DTO, or null if the entity is null
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
