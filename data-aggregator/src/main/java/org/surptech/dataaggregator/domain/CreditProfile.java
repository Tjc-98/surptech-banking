package org.surptech.dataaggregator.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(builderClassName = "Builder", toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreditProfile {

    @JsonProperty("social_security_number")
    private String socialSecurityNumber;

    @JsonProperty("current_balance")
    private Double currentBalance;

    @JsonProperty("spend_balance")
    private Double spendBalance;

    @JsonProperty("interest_rate")
    private Double interestRate;
}
