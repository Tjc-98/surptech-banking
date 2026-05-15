package org.surptech.dataaggregator.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Internal domain entity for aggregated customer and credit information.
 * Used by service and procedure layers.
 * No Jackson annotations - not meant for serialization.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerCreditInfoEntity {

    private String socialSecurityNumber;
    private String firstName;
    private String lastName;
    private String address;
    private Double fullCreditBalance;
    private Double spendBalance;
    private Double interestRate;
}
