package org.surptech.dataaggregator.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(builderClassName = "Builder", toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class CustomerInfoRequest {

    @JsonProperty("social_security_number")
    private String socialSecurityNumber;
}
