package org.surptech.common.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

/**
 * Base class for all request DTOs.
 * Provides common functionality and ensures consistent request handling.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class BaseRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Validates the request data.
     * Override this method in subclasses to implement specific validation logic.
     *
     * @throws org.surptech.common.exception.ValidationException if validation fails
     */
    public void validate() {
        // Default implementation - override in subclasses
    }
}
