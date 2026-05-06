package org.surptech.bankingtester.tests;

import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.surptech.bankingtester.annotation.TestId;
import org.surptech.bankingtester.base.BaseTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Health check tests for services
 */
@Slf4j
@DisplayName("Service Health Tests")
public class ServiceHealthTest extends BaseTest {
    
    @Test
    @TestId("TC-201")
    @Tag("smoke")
    @Tag("health")
    @DisplayName("TC-201: Verify Data Aggregator service is healthy and accessible")
    public void testDataAggregatorHealth() {
        // Act
        logStep("Act - Send GET request to /management/health");
        Response response = dataAggregatorClient.healthCheck();
        
        // Assert
        logStep("Verify - Log response details",
                "Response Status Code: " + response.getStatusCode(),
                "Response Status Line: " + response.getStatusLine(),
                "Response Time: " + response.getTime() + " ms",
                "Response Body: " + response.getBody().asString());
        
        logStep("Assert - Validate service health status code is 200");
        assertEquals(200, response.getStatusCode(), 
                "Data Aggregator service should be healthy");
        
        logStep("Test completed successfully - Data Aggregator is healthy");
    }
}
