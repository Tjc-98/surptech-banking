package org.surptech.common.tester.teststep;

import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;

/**
 * Abstract base class for test steps following the Builder pattern.
 * Each test step encapsulates:
 * 1. Execution (sending API request)
 * 2. Verification (validating response)
 * 3. Description (logging information)
 * 4. Run orchestration (execute + verify)
 * 
 * @param <T> The concrete TestStep type for builder chaining
 */
@Slf4j
public abstract class TestStep<T extends TestStep<T>> {
    
    protected Response response;
    protected String description;
    private StepLogger stepLogger;
    
    /**
     * Execute the test step (send API request and get response)
     * 
     * @return Response from the API call
     */
    protected abstract Response execute();
    
    /**
     * Verify the response meets expected criteria
     * Implementations should use assertions to validate response
     */
    protected abstract void verify();
    
    /**
     * Get the description of this test step for logging
     * 
     * @return Description string
     */
    protected abstract String getDescription();
    
    /**
     * Get request details for logging (optional, can be overridden)
     * 
     * @return Request details string or null
     */
    protected String getRequestDetails() {
        return null;
    }
    
    /**
     * Set the step logger for this test step
     * 
     * @param stepLogger The step logger instance
     */
    public void setStepLogger(StepLogger stepLogger) {
        this.stepLogger = stepLogger;
    }
    
    /**
     * Run the complete test step: execute, log, and verify
     * 
     * @return The response for further inspection if needed
     */
    public Response run() {
        // Log the step description
        String desc = getDescription();
        if (desc != null && !desc.isEmpty() && stepLogger != null) {
            stepLogger.logStep(desc);
        }
        
        // Log request details if available
        String requestDetails = getRequestDetails();
        if (requestDetails != null && !requestDetails.isEmpty()) {
            log.info("  Request: {}", requestDetails);
        }
        
        // Execute the API call
        this.response = execute();
        
        // Log response details
        if (response != null && stepLogger != null) {
            logResponseDetails();
        }
        
        // Verify the response
        verify();
        
        return this.response;
    }
    
    /**
     * Log detailed response information
     */
    private void logResponseDetails() {
        stepLogger.logStep(String.format("Response received - Status: %d, Time: %d ms", 
                response.getStatusCode(), response.getTime()));
        
        log.info("  Status Line: {}", response.getStatusLine());
        log.info("  Content-Type: {}", response.getContentType());
        
        // Log response body (truncate if too long)
        String body = response.getBody().asString();
        if (body != null && !body.isEmpty()) {
            if (body.length() > 500) {
                log.info("  Response Body: {}... (truncated)", body.substring(0, 500));
            } else {
                log.info("  Response Body: {}", body);
            }
        }
    }
    
    /**
     * Set a custom description for this test step
     * 
     * @param description The description to log
     * @return This test step for chaining
     */
    @SuppressWarnings("unchecked")
    public T withDescription(String description) {
        this.description = description;
        return (T) this;
    }
    
    /**
     * Get the response from the last execution
     * 
     * @return The response object
     */
    public Response getResponse() {
        return this.response;
    }
    
    /**
     * Functional interface for step logging
     */
    @FunctionalInterface
    public interface StepLogger {
        void logStep(String message);
    }
}
