package org.surptech.customerprofile.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(builderClassName = "Builder", toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class GetCustomerProfileRequest {

    @JsonProperty("social_security_number")
    private String socialSecurityNumber;
}
