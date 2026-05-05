package org.surptech.bankingtester.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Customer Profile model for test assertions
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerProfile {
    
    @JsonProperty("social_security_number")
    private String socialSecurityNumber;
    
    @JsonProperty("first_name")
    private String firstName;
    
    @JsonProperty("last_name")
    private String lastName;
    
    @JsonProperty("address")
    private String address;
}
