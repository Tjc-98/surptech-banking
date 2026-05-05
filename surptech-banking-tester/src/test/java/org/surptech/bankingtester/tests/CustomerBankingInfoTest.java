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
        String ssn = config.getValidSsn();
        log.info("=".repeat(80));
        log.info("TEST: TC-001 - Get Customer Banking Info with Valid SSN");
        log.info("=".repeat(80));
        log.info("Testing with valid SSN: {}", ssn);
        
        // Act
        log.info("Sending POST request to /customer/info");
        log.info("Request Body: {{\"social_security_number\": \"{}\"}}", ssn);
        Response response = dataAggregatorClient.getCustomerBankingInfo(ssn);
        
        // Log response details
        log.info("-".repeat(80));
        log.info("Response received:");
        log.info("  Status Code: {}", response.getStatusCode());
        log.info("  Status Line: {}", response.getStatusLine());
        log.info("  Content-Type: {}", response.getContentType());
        log.info("  Response Time: {} ms", response.getTime());
        log.info("  Response Body: {}", response.getBody().asString());
        log.info("-".repeat(80));
        
        // Assert
        log.info("Validating response status code");
        assertEquals(200, response.getStatusCode(), 
                "Expected HTTP 200 OK for valid SSN");
        
        log.info("Parsing response body");
        CustomerBankingInfo bankingInfo = response.as(CustomerBankingInfo.class);
        assertNotNull(bankingInfo, "Banking info should not be null");
        
        // Validate Customer Info
        log.info("Validating customer profile data");
        log.info("  Expected SSN: {}, Actual: {}", config.getExpectedSsn(), bankingInfo.getSocialSecurityNumber());
        log.info("  Expected First Name: {}, Actual: {}", config.getExpectedFirstName(), bankingInfo.getFirstName());
        log.info("  Expected Last Name: {}, Actual: {}", config.getExpectedLastName(), bankingInfo.getLastName());
        log.info("  Expected Address: {}, Actual: {}", config.getExpectedAddress(), bankingInfo.getAddress());
        
        assertEquals(config.getExpectedSsn(), bankingInfo.getSocialSecurityNumber(),
                "SSN should match expected value");
        assertEquals(config.getExpectedFirstName(), bankingInfo.getFirstName(),
                "First name should match expected value");
        assertEquals(config.getExpectedLastName(), bankingInfo.getLastName(),
                "Last name should match expected value");
        assertEquals(config.getExpectedAddress(), bankingInfo.getAddress(),
                "Address should match expected value");
        
        // Validate Credit Info exists
        log.info("Validating credit profile data");
        log.info("  Current Balance: {}", bankingInfo.getCurrentBalance());
        log.info("  Spend Balance: {}", bankingInfo.getSpendBalance());
        log.info("  Interest Rate: {}", bankingInfo.getInterestRate());
        
        assertNotNull(bankingInfo.getCurrentBalance(), 
                "Current balance should not be null");
        assertNotNull(bankingInfo.getSpendBalance(), 
                "Spend balance should not be null");
        assertNotNull(bankingInfo.getInterestRate(), 
                "Interest rate should not be null");
        
        log.info("=".repeat(80));
        log.info("✓ Test passed: Customer banking information retrieved successfully");
        log.info("=".repeat(80));
    }
    
    @Test
    @TestId("TC-002")
    @Tag("integration")
    @DisplayName("TC-002: Verify customer profile data structure and completeness")
    public void testGetCustomerBankingInfo_ValidateCustomerProfileStructure() {
        // Arrange
        String ssn = config.getValidSsn();
        log.info("=".repeat(80));
        log.info("TEST: TC-002 - Validate Customer Profile Structure");
        log.info("=".repeat(80));
        log.info("Testing with SSN: {}", ssn);
        
        // Act
        log.info("Sending POST request to /customer/info");
        Response response = dataAggregatorClient.getCustomerBankingInfo(ssn);
        
        log.info("-".repeat(80));
        log.info("Response Status: {}", response.getStatusCode());
        log.info("Response Body: {}", response.getBody().asString());
        log.info("-".repeat(80));
        
        // Assert
        assertEquals(200, response.getStatusCode());
        
        CustomerBankingInfo bankingInfo = response.as(CustomerBankingInfo.class);
        
        log.info("Validating customer profile structure");
        log.info("  SSN: {}", bankingInfo.getSocialSecurityNumber());
        log.info("  First Name: {}", bankingInfo.getFirstName());
        log.info("  Last Name: {}", bankingInfo.getLastName());
        log.info("  Address: {}", bankingInfo.getAddress());
        
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
        
        log.info("=".repeat(80));
        log.info("✓ Test passed: Customer profile structure is valid");
        log.info("=".repeat(80));
    }
    
    @Test
    @TestId("TC-003")
    @Tag("integration")
    @DisplayName("TC-003: Verify credit profile data structure and completeness")
    public void testGetCustomerBankingInfo_ValidateCreditProfileStructure() {
        // Arrange
        String ssn = config.getValidSsn();
        log.info("=".repeat(80));
        log.info("TEST: TC-003 - Validate Credit Profile Structure");
        log.info("=".repeat(80));
        log.info("Testing with SSN: {}", ssn);
        
        // Act
        log.info("Sending POST request to /customer/info");
        Response response = dataAggregatorClient.getCustomerBankingInfo(ssn);
        
        log.info("-".repeat(80));
        log.info("Response Status: {}", response.getStatusCode());
        log.info("Response Body: {}", response.getBody().asString());
        log.info("-".repeat(80));
        
        // Assert
        assertEquals(200, response.getStatusCode());
        
        CustomerBankingInfo bankingInfo = response.as(CustomerBankingInfo.class);
        
        log.info("Validating credit profile structure");
        log.info("  SSN: {}", bankingInfo.getSocialSecurityNumber());
        log.info("  Current Balance: {}", bankingInfo.getCurrentBalance());
        log.info("  Spend Balance: {}", bankingInfo.getSpendBalance());
        log.info("  Interest Rate: {}", bankingInfo.getInterestRate());
        
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
        
        log.info("=".repeat(80));
        log.info("✓ Test passed: Credit profile structure is valid");
        log.info("=".repeat(80));
    }
    
    @Test
    @TestId("TC-004")
    @Tag("smoke")
    @DisplayName("TC-004: Verify response time is within acceptable limits")
    public void testGetCustomerBankingInfo_ResponseTime() {
        // Arrange
        String ssn = config.getValidSsn();
        long maxResponseTimeMs = 5000; // 5 seconds
        
        log.info("=".repeat(80));
        log.info("TEST: TC-004 - Validate Response Time");
        log.info("=".repeat(80));
        log.info("Testing with SSN: {}", ssn);
        log.info("Maximum acceptable response time: {} ms", maxResponseTimeMs);
        
        // Act
        log.info("Sending POST request to /customer/info");
        long startTime = System.currentTimeMillis();
        Response response = dataAggregatorClient.getCustomerBankingInfo(ssn);
        long endTime = System.currentTimeMillis();
        long responseTime = endTime - startTime;
        
        // Assert
        log.info("-".repeat(80));
        log.info("Response Status: {}", response.getStatusCode());
        log.info("Response Time: {} ms", responseTime);
        log.info("Response Time from REST Assured: {} ms", response.getTime());
        log.info("Max Allowed Time: {} ms", maxResponseTimeMs);
        log.info("-".repeat(80));
        
        assertEquals(200, response.getStatusCode());
        assertTrue(responseTime < maxResponseTimeMs, 
                String.format("Response time (%d ms) should be less than %d ms", 
                        responseTime, maxResponseTimeMs));
        
        log.info("=".repeat(80));
        log.info("✓ Test passed: Response time is acceptable ({} ms < {} ms)", responseTime, maxResponseTimeMs);
        log.info("=".repeat(80));
    }
}
