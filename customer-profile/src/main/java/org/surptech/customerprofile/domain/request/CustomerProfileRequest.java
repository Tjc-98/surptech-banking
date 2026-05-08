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

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerProfileRequest extends BaseRequest {

    @JsonProperty("social_security_number")
    private String socialSecurityNumber;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("address")
    private String address;

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
