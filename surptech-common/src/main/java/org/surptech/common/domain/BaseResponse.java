package org.surptech.common.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Base class for all response DTOs.
 * Provides common timestamp field and ensures consistent response structure across all services.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class BaseResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Response timestamp using system default zone */
    private LocalDateTime timestamp;

    /**
     * Default constructor that sets timestamp to current time.
     */
    protected BaseResponse() {
        this.timestamp = LocalDateTime.now();
    }

    /**
     * Constructor that allows setting a custom timestamp.
     * Useful for testing and serialization scenarios.
     *
     * @param timestamp the timestamp to set, or null to use current time
     */
    protected BaseResponse(LocalDateTime timestamp) {
        this.timestamp = timestamp != null ? timestamp : LocalDateTime.now();
    }
}
