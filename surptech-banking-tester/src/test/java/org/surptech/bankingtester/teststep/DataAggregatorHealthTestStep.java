package org.surptech.bankingtester.teststep;

import io.restassured.response.Response;
import org.surptech.common.tester.teststep.TestStep;
import org.surptech.bankingtester.client.DataAggregatorClient;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test step for verifying Data Aggregator service health
 */
public class DataAggregatorHealthTestStep extends TestStep<DataAggregatorHealthTestStep> {
    
    private final DataAggregatorClient client;
    private Integer expectedStatusCode = 200;
    private Long maxResponseTimeMs;
    
    public DataAggregatorHealthTestStep(DataAggregatorClient client) {
        this.client = client;
    }
    
    /**
     * Set the expected HTTP status code
     * 
     * @param statusCode Expected status code
     * @return This test step for chaining
     */
    public DataAggregatorHealthTestStep expectStatusCode(int statusCode) {
        this.expectedStatusCode = statusCode;
        return this;
    }
    
    /**
     * Set the maximum acceptable response time
     * 
     * @param maxResponseTimeMs Maximum response time in milliseconds
     * @return This test step for chaining
     */
    public DataAggregatorHealthTestStep expectResponseTimeUnder(long maxResponseTimeMs) {
        this.maxResponseTimeMs = maxResponseTimeMs;
        return this;
    }
    
    @Override
    protected Response execute() {
        return client.healthCheck();
    }
    
    @Override
    protected void verify() {
        assertNotNull(response, "Response should not be null");
        
        // Verify status code
        assertEquals(expectedStatusCode, response.getStatusCode(),
                String.format("Expected status code %d but got %d", expectedStatusCode, response.getStatusCode()));
        
        // Verify response time if specified
        if (maxResponseTimeMs != null) {
            long actualResponseTime = response.getTime();
            assertTrue(actualResponseTime < maxResponseTimeMs,
                    String.format("Response time (%d ms) should be less than %d ms", 
                            actualResponseTime, maxResponseTimeMs));
        }
    }
    
    @Override
    protected String getDescription() {
        if (description != null) {
            return description;
        }
        return "Verify Data Aggregator health endpoint";
    }
    
    @Override
    protected String getRequestDetails() {
        return "GET /management/health";
    }
}
