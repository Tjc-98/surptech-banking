package org.surptech.dataaggregator.domain.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerInfoRequest {

    @JsonProperty("social_security_number")
    private String socialSecurityNumber;
}
