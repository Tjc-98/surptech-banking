package org.surptech.creditprofile.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Internal domain entity representing a credit profile.
 *
 * This entity is used exclusively by the repository and service layers for internal operations.
 * It does NOT contain any Jackson annotations as it is not meant for direct serialization to/from JSON.
 *
 * Data conversion to request/response DTOs is handled by CreditProfileMapper.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreditProfileEntity {

    /** The unique social security number identifying the customer */
    private String socialSecurityNumber;

    /** The full credit balance on the credit account */
    private BigDecimal fullCreditBalance;

    /** The available spending balance */
    private BigDecimal spendBalance;

    /** The interest rate applied to the credit account */
    private BigDecimal interestRate;
}
