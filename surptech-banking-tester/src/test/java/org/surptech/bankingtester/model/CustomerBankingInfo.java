package org.surptech.bankingtester.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Combined Customer and Credit Information model for test assertions
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerBankingInfo {
    
    @JsonProperty("social_security_number")
    private String socialSecurityNumber;
    
    @JsonProperty("first_name")
    private String firstName;
    
    @JsonProperty("last_name")
    private String lastName;
    
    @JsonProperty("address")
    private String address;
    
    @JsonProperty("full_credit_balance")
    private Double fullCreditBalance;
    
    @JsonProperty("spend_balance")
    private Double spendBalance;
    
    @JsonProperty("interest_rate")
    private Double interestRate;
}
