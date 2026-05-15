package org.surptech.customerprofile.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Internal domain entity representing a customer profile in the data persistence layer.
 *
 * This entity is used exclusively by the repository and procedure layers for internal operations.
 * It contains no Jackson annotations and should not be directly exposed in HTTP responses.
 *
 * Conversion to/from request and response DTOs is handled by {@link
 * org.surptech.customerprofile.mapper.CustomerProfileMapper}.
 *
 * The use of a separate entity class allows for:
 * - Clean separation between API contracts (DTOs) and internal domain models
 * - Flexibility to change the internal representation without affecting the public API
 * - Database-specific constraints and optimizations
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerProfileEntity {

    /** Unique identifier: the customer's social security number (USA format: XXX-XX-XXXX) */
    private String socialSecurityNumber;

    /** Customer's first name */
    private String firstName;

    /** Customer's last name */
    private String lastName;

    /** Customer's residential address */
    private String address;
}
