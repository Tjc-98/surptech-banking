package org.surptech.customerprofile.mapper;

import org.surptech.customerprofile.domain.request.CustomerProfileRequest;
import org.surptech.customerprofile.domain.response.CustomerProfileResponse;
import org.surptech.customerprofile.domain.entity.CustomerProfileEntity;

/**
 * Mapper to convert between DTOs and internal entities for customer profile operations.
 *
 * This mapper provides one-way conversion methods:
 * - CustomerProfileRequest (DTO) -> CustomerProfileEntity (internal)
 * - CustomerProfileEntity (internal) -> CustomerProfileResponse (DTO)
 */
public class CustomerProfileMapper {

    private CustomerProfileMapper() {
        // Utility class - should not be instantiated
    }

    /**
     * Converts a CustomerProfileRequest DTO to an internal CustomerProfileEntity.
     *
     * @param request the request DTO to convert
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
     * Converts a CustomerProfileEntity to a CustomerProfileResponse DTO.
     *
     * @param entity the entity to convert
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
