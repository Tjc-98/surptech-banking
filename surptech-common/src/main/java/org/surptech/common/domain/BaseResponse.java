package org.surptech.common.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Base class for all response DTOs.
 * Provides common fields like timestamp and ensures consistent response structure.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class BaseResponse implements Serializable {

    private LocalDateTime timestamp;

    protected BaseResponse() {
        this.timestamp = LocalDateTime.now();
    }
}
