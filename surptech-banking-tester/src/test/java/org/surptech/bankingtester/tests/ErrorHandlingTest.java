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
        String invalidSsn = config.getInvalidSsn();
        log.info("=".repeat(80));
        log.info("TEST: TC-101 - Handle Invalid SSN");
        log.info("=".repeat(80));
        log.info("Testing with invalid SSN: {}", invalidSsn);
        
        // Act
        log.info("Sending POST request to /customer/info with invalid SSN");
        log.info("Request Body: {{\"social_security_number\": \"{}\"}}", invalidSsn);
        Response response = dataAggregatorClient.getCustomerBankingInfo(invalidSsn);
        
        // Assert
        log.info("-".repeat(80));
        log.info("Response Status Code: {}", response.getStatusCode());
        log.info("Response Status Line: {}", response.getStatusLine());
        log.info("Response Body: {}", response.getBody().asString());
        log.info("-".repeat(80));
        
        assertTrue(response.getStatusCode() == 404 || response.getStatusCode() == 400,
                "Expected HTTP 404 Not Found or 400 Bad Request for invalid SSN");
        
        log.info("=".repeat(80));
        log.info("✓ Test passed: Invalid SSN handled correctly with status {}", response.getStatusCode());
        log.info("=".repeat(80));
    }
    
    @Test
    @TestId("TC-102")
    @Tag("error-handling")
    @DisplayName("TC-102: Verify handling of null SSN")
    public void testGetCustomerBankingInfo_NullSSN() {
        // Arrange
        String nullSsn = null;
        log.info("=".repeat(80));
        log.info("TEST: TC-102 - Handle Null SSN");
        log.info("=".repeat(80));
        log.info("Testing with null SSN");
        
        // Act
        log.info("Sending POST request to /customer/info with null SSN");
        Response response = dataAggregatorClient.getCustomerBankingInfo(nullSsn);
        
        // Assert
        log.info("-".repeat(80));
        log.info("Response Status Code: {}", response.getStatusCode());
        log.info("Response Status Line: {}", response.getStatusLine());
        log.info("Response Body: {}", response.getBody().asString());
        log.info("-".repeat(80));
        
        assertTrue(response.getStatusCode() >= 400 && response.getStatusCode() < 500,
                "Expected HTTP 4xx error for null SSN");
        
        log.info("=".repeat(80));
        log.info("✓ Test passed: Null SSN handled correctly with status {}", response.getStatusCode());
        log.info("=".repeat(80));
    }
    
    @Test
    @TestId("TC-103")
    @Tag("error-handling")
    @DisplayName("TC-103: Verify handling of empty SSN")
    public void testGetCustomerBankingInfo_EmptySSN() {
        // Arrange
        String emptySsn = "";
        log.info("=".repeat(80));
        log.info("TEST: TC-103 - Handle Empty SSN");
        log.info("=".repeat(80));
        log.info("Testing with empty SSN");
        
        // Act
        log.info("Sending POST request to /customer/info with empty SSN");
        log.info("Request Body: {{\"social_security_number\": \"{}\"}}", emptySsn);
        Response response = dataAggregatorClient.getCustomerBankingInfo(emptySsn);
        
        // Assert
        log.info("-".repeat(80));
        log.info("Response Status Code: {}", response.getStatusCode());
        log.info("Response Status Line: {}", response.getStatusLine());
        log.info("Response Body: {}", response.getBody().asString());
        log.info("-".repeat(80));
        
        assertTrue(response.getStatusCode() >= 400 && response.getStatusCode() < 500,
                "Expected HTTP 4xx error for empty SSN");
        
        log.info("=".repeat(80));
        log.info("✓ Test passed: Empty SSN handled correctly with status {}", response.getStatusCode());
        log.info("=".repeat(80));
    }
    
    @Test
    @TestId("TC-104")
    @Tag("error-handling")
    @DisplayName("TC-104: Verify handling of malformed SSN format")
    public void testGetCustomerBankingInfo_MalformedSSN() {
        // Arrange
        String malformedSsn = "INVALID-SSN-FORMAT";
        log.info("=".repeat(80));
        log.info("TEST: TC-104 - Handle Malformed SSN");
        log.info("=".repeat(80));
        log.info("Testing with malformed SSN: {}", malformedSsn);
        
        // Act
        log.info("Sending POST request to /customer/info with malformed SSN");
        log.info("Request Body: {{\"social_security_number\": \"{}\"}}", malformedSsn);
        Response response = dataAggregatorClient.getCustomerBankingInfo(malformedSsn);
        
        // Assert
        log.info("-".repeat(80));
        log.info("Response Status Code: {}", response.getStatusCode());
        log.info("Response Status Line: {}", response.getStatusLine());
        log.info("Response Body: {}", response.getBody().asString());
        log.info("-".repeat(80));
        
        assertTrue(response.getStatusCode() >= 400,
                "Expected HTTP error for malformed SSN");
        
        log.info("=".repeat(80));
        log.info("✓ Test passed: Malformed SSN handled correctly with status {}", response.getStatusCode());
        log.info("=".repeat(80));
    }
}
