package org.surptech.dataaggregator.domain.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerCreditInfoResponse {

    @JsonProperty("social_security_number")
    private String socialSecurityNumber;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("address")
    private String address;

    @JsonProperty("current_balance")
    private Double currentBalance;

    @JsonProperty("spend_balance")
    private Double spendBalance;

    @JsonProperty("interest_rate")
    private Double interestRate;
}
