package org.surptech.customerprofile.domain.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.surptech.common.domain.BaseRequest;
import org.surptech.common.validation.ValidationUtils;

/**
 * Data Transfer Object for customer profile creation/update requests.
 *
 * This DTO represents the incoming HTTP request payload for creating or updating customer profiles.
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
public class CustomerProfileRequest extends BaseRequest {

    /** The social security number of the customer */
    @JsonProperty("social_security_number")
    private String socialSecurityNumber;

    /** The customer's first name */
    @JsonProperty("first_name")
    private String firstName;

    /** The customer's last name */
    @JsonProperty("last_name")
    private String lastName;

    /** The customer's residential address */
    @JsonProperty("address")
    private String address;

    /**
     * Validates all fields in the request.
     *
     * @throws org.surptech.common.exception.ValidationException if any validation fails
     */
    @Override
    public void validate() {
        ValidationUtils.validateAll(
            () -> ValidationUtils.validateSocialSecurityNumber(socialSecurityNumber),
            () -> ValidationUtils.validateNotEmpty(firstName, "First Name"),
            () -> ValidationUtils.validateNotEmpty(lastName, "Last Name"),
            () -> ValidationUtils.validateNotEmpty(address, "Address")
        );
    }
}
