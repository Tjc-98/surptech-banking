package org.surptech.customerprofiletester.teststep;

import io.restassured.response.Response;
import org.surptech.common.tester.teststep.TestStep;
import org.surptech.customerprofiletester.client.CustomerProfileClient;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test step for checking customer profile service health
 */
public class CustomerProfileHealthTestStep extends TestStep<CustomerProfileHealthTestStep> {
    
    private final CustomerProfileClient client;
    private Integer expectedStatusCode = 200;
    
    public CustomerProfileHealthTestStep(CustomerProfileClient client) {
        this.client = client;
    }
    
    public CustomerProfileHealthTestStep expectStatusCode(int statusCode) {
        this.expectedStatusCode = statusCode;
        return this;
    }
    
    @Override
    protected Response execute() {
        return client.healthCheck();
    }
    
    @Override
    protected void verify() {
        assertNotNull(response, "Health check response should not be null");
        assertEquals(expectedStatusCode, response.getStatusCode(),
                "Health check should return status " + expectedStatusCode);
        
        String body = response.getBody().asString();
        assertNotNull(body, "Health check response body should not be null");
        assertTrue(body.contains("UP") || body.contains("status"), 
                "Health check response should contain status information");
    }
    
    @Override
    protected String getDescription() {
        if (description != null && !description.isEmpty()) {
            return description;
        }
        return "Check customer profile service health";
    }
    
    @Override
    protected String getRequestDetails() {
        return "GET /management/health";
    }
}
