package org.surptech.customerprofile.domain.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetCustomerProfileRequest {

    @JsonProperty("social_security_number")
    private String socialSecurityNumber;
}
