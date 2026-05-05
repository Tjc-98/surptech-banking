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
        log.info("=".repeat(80));
        log.info("TEST: TC-201 - Data Aggregator Health Check");
        log.info("=".repeat(80));
        log.info("Checking Data Aggregator health");
        log.info("Sending GET request to /management/health");
        
        Response response = dataAggregatorClient.healthCheck();
        
        // Assert
        log.info("-".repeat(80));
        log.info("Response Status Code: {}", response.getStatusCode());
        log.info("Response Status Line: {}", response.getStatusLine());
        log.info("Response Time: {} ms", response.getTime());
        log.info("Response Body: {}", response.getBody().asString());
        log.info("-".repeat(80));
        
        assertEquals(200, response.getStatusCode(), 
                "Data Aggregator service should be healthy");
        
        log.info("=".repeat(80));
        log.info("✓ Test passed: Data Aggregator is healthy");
        log.info("=".repeat(80));
    }
}
