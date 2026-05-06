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
 * Tests for error handling scenarios
 */
@Slf4j
@DisplayName("Error Handling Tests")
public class ErrorHandlingTest extends BaseTest {
    
    @Test
    @TestId("TC-101")
    @Tag("error-handling")
    @DisplayName("TC-101: Verify handling of invalid SSN")
    public void testGetCustomerBankingInfo_InvalidSSN() {
        // Arrange
        logStep("Arrange - Prepare test data with invalid SSN");
        String invalidSsn = config.getInvalidSsn();
        log.info("  Using invalid SSN: {}", invalidSsn);
        
        // Act
        logStep("Act - Send POST request to /customer/info with invalid SSN");
        log.info("  Request Body: {{\"social_security_number\": \"{}\"}}", invalidSsn);
        Response response = dataAggregatorClient.getCustomerBankingInfo(invalidSsn);
        
        // Assert
        logStep("Verify - Log response details",
                "Response Status Code: " + response.getStatusCode(),
                "Response Status Line: " + response.getStatusLine(),
                "Response Body: " + response.getBody().asString());
        
        logStep("Assert - Validate error response status code (404 or 400)");
        assertTrue(response.getStatusCode() == 404 || response.getStatusCode() == 400,
                "Expected HTTP 404 Not Found or 400 Bad Request for invalid SSN");
        
        logStep("Test completed successfully - Invalid SSN handled with status {}", response.getStatusCode());
    }
    
    @Test
    @TestId("TC-102")
    @Tag("error-handling")
    @DisplayName("TC-102: Verify handling of null SSN")
    public void testGetCustomerBankingInfo_NullSSN() {
        // Arrange
        logStep("Arrange - Prepare test data with null SSN");
        String nullSsn = null;
        log.info("  Using null SSN");
        
        // Act
        logStep("Act - Send POST request to /customer/info with null SSN");
        Response response = dataAggregatorClient.getCustomerBankingInfo(nullSsn);
        
        // Assert
        logStep("Verify - Log response details",
                "Response Status Code: " + response.getStatusCode(),
                "Response Status Line: " + response.getStatusLine(),
                "Response Body: " + response.getBody().asString());
        
        logStep("Assert - Validate error response status code (4xx)");
        assertTrue(response.getStatusCode() >= 400 && response.getStatusCode() < 500,
                "Expected HTTP 4xx error for null SSN");
        
        logStep("Test completed successfully - Null SSN handled with status {}", response.getStatusCode());
    }
    
    @Test
    @TestId("TC-103")
    @Tag("error-handling")
    @DisplayName("TC-103: Verify handling of empty SSN")
    public void testGetCustomerBankingInfo_EmptySSN() {
        // Arrange
        logStep("Arrange - Prepare test data with empty SSN");
        String emptySsn = "";
        log.info("  Using empty SSN");
        
        // Act
        logStep("Act - Send POST request to /customer/info with empty SSN");
        log.info("  Request Body: {{\"social_security_number\": \"{}\"}}", emptySsn);
        Response response = dataAggregatorClient.getCustomerBankingInfo(emptySsn);
        
        // Assert
        logStep("Verify - Log response details",
                "Response Status Code: " + response.getStatusCode(),
                "Response Status Line: " + response.getStatusLine(),
                "Response Body: " + response.getBody().asString());
        
        logStep("Assert - Validate error response status code (4xx)");
        assertTrue(response.getStatusCode() >= 400 && response.getStatusCode() < 500,
                "Expected HTTP 4xx error for empty SSN");
        
        logStep("Test completed successfully - Empty SSN handled with status {}", response.getStatusCode());
    }
    
    @Test
    @TestId("TC-104")
    @Tag("error-handling")
    @DisplayName("TC-104: Verify handling of malformed SSN format")
    public void testGetCustomerBankingInfo_MalformedSSN() {
        // Arrange
        logStep("Arrange - Prepare test data with malformed SSN");
        String malformedSsn = "INVALID-SSN-FORMAT";
        log.info("  Using malformed SSN: {}", malformedSsn);
        
        // Act
        logStep("Act - Send POST request to /customer/info with malformed SSN");
        log.info("  Request Body: {{\"social_security_number\": \"{}\"}}", malformedSsn);
        Response response = dataAggregatorClient.getCustomerBankingInfo(malformedSsn);
        
        // Assert
        logStep("Verify - Log response details",
                "Response Status Code: " + response.getStatusCode(),
                "Response Status Line: " + response.getStatusLine(),
                "Response Body: " + response.getBody().asString());
        
        logStep("Assert - Validate error response status code (4xx or 5xx)");
        assertTrue(response.getStatusCode() >= 400,
                "Expected HTTP error for malformed SSN");
        
        logStep("Test completed successfully - Malformed SSN handled with status {}", response.getStatusCode());
    }
}
