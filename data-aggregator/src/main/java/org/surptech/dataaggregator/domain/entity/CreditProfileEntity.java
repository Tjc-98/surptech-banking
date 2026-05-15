package org.surptech.dataaggregator.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Internal domain entity for credit profile.
 * Used by service and procedure layers.
 * No Jackson annotations - not meant for serialization.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreditProfileEntity {

    private String socialSecurityNumber;
    private Double fullCreditBalance;
    private Double spendBalance;
    private Double interestRate;
}
