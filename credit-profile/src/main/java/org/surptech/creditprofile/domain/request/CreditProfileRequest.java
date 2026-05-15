package org.surptech.creditprofile.domain.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.surptech.common.domain.BaseRequest;
import org.surptech.common.validation.ValidationUtils;

import java.math.BigDecimal;

/**
 * Data Transfer Object for credit profile creation/update requests.
 *
 * This DTO represents the incoming HTTP request payload for creating or updating credit profiles.
 * It extends BaseRequest to inherit common request functionality.
 *
 * All fields use snake_case JSON property names to follow RESTful API naming conventions.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreditProfileRequest extends BaseRequest {

    /** The social security number of the customer */
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

    /**
     * Validates all fields in the request.
     *
     * @throws org.surptech.common.exception.ValidationException if any validation fails
     */
    @Override
    public void validate() {
        ValidationUtils.validateAll(
            () -> ValidationUtils.validateSocialSecurityNumber(socialSecurityNumber),
            () -> ValidationUtils.validateNotNull(fullCreditBalance, "Full Credit Balance"),
            () -> ValidationUtils.validateNotNull(spendBalance, "Spend Balance"),
            () -> ValidationUtils.validateNotNull(interestRate, "Interest Rate"),
            () -> ValidationUtils.validateNonNegative(fullCreditBalance, "Full Credit Balance"),
            () -> ValidationUtils.validateNonNegative(spendBalance, "Spend Balance"),
            () -> ValidationUtils.validateNonNegative(interestRate, "Interest Rate")
        );
    }
}
