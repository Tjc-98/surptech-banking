package org.surptech.customerprofile.domain.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for customer profile API responses.
 *
 * This DTO represents the outgoing HTTP response payload for customer profile operations.
 * It transmits customer profile information that has been retrieved or created.
 *
 * All fields use snake_case JSON property names to follow RESTful API naming conventions.
 * Null fields are excluded from JSON serialization via {@link JsonInclude#NON_NULL}.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerProfileResponse {

    /** The unique social security number of the customer (USA format: XXX-XX-XXXX) */
    @JsonProperty("social_security_number")
    private String socialSecurityNumber;

    /** The customer's first name */
    @JsonProperty("first_name")
    private String firstName;

    /** The customer's last name */
    @JsonProperty("last_name")
    private String lastName;

    /** The customer's residential address */
    @JsonProperty("address")
    private String address;
}
