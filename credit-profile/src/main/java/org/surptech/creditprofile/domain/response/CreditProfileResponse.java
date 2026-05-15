package org.surptech.creditprofile.domain.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Data Transfer Object for credit profile API responses.
 *
 * This DTO represents the outgoing HTTP response payload for credit profile operations.
 * It contains the credit profile information that should be returned to the client.
 *
 * All fields use snake_case JSON property names to follow RESTful API naming conventions.
 * Null fields are excluded from JSON serialization via JsonInclude.Include.NON_NULL.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreditProfileResponse {

    /** The unique social security number of the customer */
    @JsonProperty("social_security_number")
    private String socialSecurityNumber;

    /** The full credit balance on the credit account */
    @JsonProperty("full_credit_balance")
    private BigDecimal fullCreditBalance;

    /** The available spending balance */
    @JsonProperty("spend_balance")
    private BigDecimal spendBalance;

    /** The interest rate applied to the credit account */
    @JsonProperty("interest_rate")
    private BigDecimal interestRate;
}
