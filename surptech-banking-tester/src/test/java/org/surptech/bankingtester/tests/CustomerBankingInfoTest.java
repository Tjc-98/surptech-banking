package org.surptech.bankingtester.tests;

import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.surptech.bankingtester.annotation.TestId;
import org.surptech.bankingtester.base.BaseTest;
import org.surptech.bankingtester.model.CustomerBankingInfo;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for Customer Banking Information retrieval
 */
@Slf4j
@DisplayName("Customer Banking Information Tests")
public class CustomerBankingInfoTest extends BaseTest {
    
    @Test
    @TestId("TC-001")
    @Tag("smoke")
    @Tag("integration")
    @DisplayName("TC-001: Verify successful retrieval of customer banking information with valid SSN")
    public void testGetCustomerBankingInfo_ValidSSN_Success() {
        // Arrange
        logStep("Arrange - Prepare test data with valid SSN");
        String ssn = config.getValidSsn();
        log.info("  Using SSN: {}", ssn);
        
        // Act
        logStep("Act - Send POST request to /customer/info");
        log.info("  Request Body: {{\"social_security_number\": \"{}\"}}", ssn);
        Response response = dataAggregatorClient.getCustomerBankingInfo(ssn);
        
        // Log response details
        logStep("Verify - Log response details");
        log.info("  Status Code: {}", response.getStatusCode());
        log.info("  Status Line: {}", response.getStatusLine());
        log.info("  Content-Type: {}", response.getContentType());
        log.info("  Response Time: {} ms", response.getTime());
        log.info("  Response Body: {}", response.getBody().asString());
        
        // Assert
        logStep("Assert - Validate response status code is 200");
        assertEquals(200, response.getStatusCode(), 
                "Expected HTTP 200 OK for valid SSN");
        
        logStep("Assert - Parse response body and verify not null");
        CustomerBankingInfo bankingInfo = response.as(CustomerBankingInfo.class);
        assertNotNull(bankingInfo, "Banking info should not be null");
        
        // Validate Customer Info
        logStep("Assert - Validate customer profile data",
                "Expected SSN: " + config.getExpectedSsn() + ", Actual: " + bankingInfo.getSocialSecurityNumber(),
                "Expected First Name: " + config.getExpectedFirstName() + ", Actual: " + bankingInfo.getFirstName(),
                "Expected Last Name: " + config.getExpectedLastName() + ", Actual: " + bankingInfo.getLastName(),
                "Expected Address: " + config.getExpectedAddress() + ", Actual: " + bankingInfo.getAddress());
        
        assertEquals(config.getExpectedSsn(), bankingInfo.getSocialSecurityNumber(),
                "SSN should match expected value");
        assertEquals(config.getExpectedFirstName(), bankingInfo.getFirstName(),
                "First name should match expected value");
        assertEquals(config.getExpectedLastName(), bankingInfo.getLastName(),
                "Last name should match expected value");
        assertEquals(config.getExpectedAddress(), bankingInfo.getAddress(),
                "Address should match expected value");
        
        // Validate Credit Info exists
        logStep("Assert - Validate credit profile data",
                "Current Balance: " + bankingInfo.getCurrentBalance(),
                "Spend Balance: " + bankingInfo.getSpendBalance(),
                "Interest Rate: " + bankingInfo.getInterestRate());
        
        assertNotNull(bankingInfo.getCurrentBalance(), 
                "Current balance should not be null");
        assertNotNull(bankingInfo.getSpendBalance(), 
                "Spend balance should not be null");
        assertNotNull(bankingInfo.getInterestRate(), 
                "Interest rate should not be null");
        
        logStep("Test completed successfully");
    }
    
    @Test
    @TestId("TC-002")
    @Tag("integration")
    @DisplayName("TC-002: Verify customer profile data structure and completeness")
    public void testGetCustomerBankingInfo_ValidateCustomerProfileStructure() {
        // Arrange
        logStep("Arrange - Prepare test data with valid SSN");
        String ssn = config.getValidSsn();
        log.info("  Using SSN: {}", ssn);
        
        // Act
        logStep("Act - Send POST request to /customer/info");
        Response response = dataAggregatorClient.getCustomerBankingInfo(ssn);
        
        logStep("Verify - Log response details");
        log.info("  Response Status: {}", response.getStatusCode());
        log.info("  Response Body: {}", response.getBody().asString());
        
        // Assert
        logStep("Assert - Validate response status code is 200");
        assertEquals(200, response.getStatusCode());
        
        logStep("Assert - Parse response body");
        CustomerBankingInfo bankingInfo = response.as(CustomerBankingInfo.class);
        
        logStep("Assert - Validate customer profile structure",
                "SSN: " + bankingInfo.getSocialSecurityNumber(),
                "First Name: " + bankingInfo.getFirstName(),
                "Last Name: " + bankingInfo.getLastName(),
                "Address: " + bankingInfo.getAddress());
        
        assertAll("Customer Profile Structure",
                () -> assertNotNull(bankingInfo.getSocialSecurityNumber(), 
                        "SSN should not be null"),
                () -> assertNotNull(bankingInfo.getFirstName(), 
                        "First name should not be null"),
                () -> assertNotNull(bankingInfo.getLastName(), 
                        "Last name should not be null"),
                () -> assertNotNull(bankingInfo.getAddress(), 
                        "Address should not be null"),
                () -> assertFalse(bankingInfo.getFirstName().isEmpty(), 
                        "First name should not be empty"),
                () -> assertFalse(bankingInfo.getLastName().isEmpty(), 
                        "Last name should not be empty")
        );
        
        logStep("Test completed successfully");
    }
    
    @Test
    @TestId("TC-003")
    @Tag("integration")
    @DisplayName("TC-003: Verify credit profile data structure and completeness")
    public void testGetCustomerBankingInfo_ValidateCreditProfileStructure() {
        // Arrange
        logStep("Arrange - Prepare test data with valid SSN");
        String ssn = config.getValidSsn();
        log.info("  Using SSN: {}", ssn);
        
        // Act
        logStep("Act - Send POST request to /customer/info");
        Response response = dataAggregatorClient.getCustomerBankingInfo(ssn);
        
        logStep("Verify - Log response details");
        log.info("  Response Status: {}", response.getStatusCode());
        log.info("  Response Body: {}", response.getBody().asString());
        
        // Assert
        logStep("Assert - Validate response status code is 200");
        assertEquals(200, response.getStatusCode());
        
        logStep("Assert - Parse response body");
        CustomerBankingInfo bankingInfo = response.as(CustomerBankingInfo.class);
        
        logStep("Assert - Validate credit profile structure",
                "SSN: " + bankingInfo.getSocialSecurityNumber(),
                "Current Balance: " + bankingInfo.getCurrentBalance(),
                "Spend Balance: " + bankingInfo.getSpendBalance(),
                "Interest Rate: " + bankingInfo.getInterestRate());
        
        assertAll("Credit Profile Structure",
                () -> assertNotNull(bankingInfo.getSocialSecurityNumber(), 
                        "SSN should not be null"),
                () -> assertNotNull(bankingInfo.getCurrentBalance(), 
                        "Current balance should not be null"),
                () -> assertNotNull(bankingInfo.getSpendBalance(), 
                        "Spend balance should not be null"),
                () -> assertNotNull(bankingInfo.getInterestRate(), 
                        "Interest rate should not be null")
        );
        
        logStep("Test completed successfully");
    }
    
    @Test
    @TestId("TC-004")
    @Tag("smoke")
    @DisplayName("TC-004: Verify response time is within acceptable limits")
    public void testGetCustomerBankingInfo_ResponseTime() {
        // Arrange
        logStep("Arrange - Prepare test data and set response time limit");
        String ssn = config.getValidSsn();
        long maxResponseTimeMs = 5000; // 5 seconds
        log.info("  Using SSN: {}", ssn);
        log.info("  Maximum acceptable response time: {} ms", maxResponseTimeMs);
        
        // Act
        logStep("Act - Send POST request to /customer/info and measure response time");
        long startTime = System.currentTimeMillis();
        Response response = dataAggregatorClient.getCustomerBankingInfo(ssn);
        long endTime = System.currentTimeMillis();
        long responseTime = endTime - startTime;
        
        // Assert
        logStep("Verify - Log response details",
                "Response Status: " + response.getStatusCode(),
                "Response Time (measured): " + responseTime + " ms",
                "Response Time (REST Assured): " + response.getTime() + " ms",
                "Max Allowed Time: " + maxResponseTimeMs + " ms");
        
        logStep("Assert - Validate response status code is 200");
        assertEquals(200, response.getStatusCode());
        
        logStep("Assert - Validate response time is within acceptable limits");
        assertTrue(responseTime < maxResponseTimeMs, 
                String.format("Response time (%d ms) should be less than %d ms", 
                        responseTime, maxResponseTimeMs));
        
        logStep("Test completed successfully - Response time: {} ms", responseTime);
    }
}
