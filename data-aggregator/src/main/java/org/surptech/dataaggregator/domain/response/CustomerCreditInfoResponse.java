package org.surptech.dataaggregator.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonPropertyOrder({
        "social_security_number",
        "first_name",
        "last_name",
        "address",
        "current_balance",
        "spend_balance",
        "interest_rate"
})
@Builder(builderClassName = "Builder", toBuilder = true)
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
