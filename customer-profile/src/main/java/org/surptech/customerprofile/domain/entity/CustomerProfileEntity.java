package org.surptech.customerprofile.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Internal domain entity representing a customer profile.
 *
 * This entity is used exclusively by the repository and service layers for internal operations.
 * It does NOT contain any Jackson annotations as it is not meant for direct serialization to/from JSON.
 *
 * Data conversion to request/response DTOs is handled by CustomerProfileMapper.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerProfileEntity {

    /** The unique social security number identifying the customer */
    private String socialSecurityNumber;

    /** The customer's first name */
    private String firstName;

    /** The customer's last name */
    private String lastName;

    /** The customer's residential address */
    private String address;
}
