package org.surptech.bankingtester.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Credit Profile model for test assertions
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreditProfile {
    
    @JsonProperty("social_security_number")
    private String socialSecurityNumber;
    
    @JsonProperty("full_credit_balance")
    private Double fullCreditBalance;
    
    @JsonProperty("spend_balance")
    private Double spendBalance;
    
    @JsonProperty("interest_rate")
    private Double interestRate;
}
