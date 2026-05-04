package org.surptech.customerprofile.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Data;

@Data
@JsonPropertyOrder({
        "code",
        "message"
})
@Builder(builderClassName = "Builder", toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    @JsonProperty("code")
    private String code;

    @JsonProperty("message")
    private String message;

}
