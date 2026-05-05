package org.surptech.dataaggregator.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Internal domain entity for customer profile.
 * Used by service and procedure layers.
 * No Jackson annotations - not meant for serialization.
 */
@Data
@Builder(builderClassName = "Builder", toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class CustomerProfileEntity {

    private String socialSecurityNumber;
    private String firstName;
    private String lastName;
    private String address;
}
