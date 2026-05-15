package org.surptech.bankingtester.tests;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.surptech.common.tester.annotation.TestId;
import org.surptech.bankingtester.base.BaseTest;

/**
 * Integration tests for Customer Banking Information retrieval
 */
@Slf4j
@DisplayName("Customer Banking Information Tests")
public class CustomerBankingInfoTest extends BaseTest {
    
    @Test
    @TestId("CBIT-01")
    @Tag("smoke")
    @Tag("integration")
    @DisplayName("CBIT-01: Verify successful retrieval of customer banking information with valid SSN")
    public void testGetCustomerBankingInfo_ValidSSN_Success() {
        getCustomerBankingInfo()
                .withSsn(config.getValidSsn())
                .expectStatusCode(200)
                .verifyResponseNotNull()
                .verifyCustomerProfile(
                        config.getExpectedSsn(),
                        config.getExpectedFirstName(),
                        config.getExpectedLastName(),
                        config.getExpectedAddress())
                .verifyCreditProfileExists()
                .run();
    }
    
    @Test
    @TestId("CBIT-02")
    @Tag("integration")
    @DisplayName("CBIT-02: Verify customer profile data structure and completeness")
    public void testGetCustomerBankingInfo_ValidateCustomerProfileStructure() {
        getCustomerBankingInfo()
                .withSsn(config.getValidSsn())
                .expectStatusCode(200)
                .verifyCustomerProfile(
                        config.getExpectedSsn(),
                        config.getExpectedFirstName(),
                        config.getExpectedLastName(),
                        config.getExpectedAddress())
                .withDescription("Validate customer profile structure and completeness")
                .run();
    }
    
    @Test
    @TestId("CBIT-03")
    @Tag("integration")
    @DisplayName("CBIT-03: Verify credit profile data structure and completeness")
    public void testGetCustomerBankingInfo_ValidateCreditProfileStructure() {
        getCustomerBankingInfo()
                .withSsn(config.getValidSsn())
                .expectStatusCode(200)
                .verifyCreditProfileExists()
                .withDescription("Validate credit profile structure and completeness")
                .run();
    }
    
    @Test
    @TestId("CBIT-04")
    @Tag("smoke")
    @DisplayName("CBIT-04: Verify response time is within acceptable limits")
    public void testGetCustomerBankingInfo_ResponseTime() {
        getCustomerBankingInfo()
                .withSsn(config.getValidSsn())
                .expectStatusCode(200)
                .expectResponseTimeUnder(5000)
                .withDescription("Validate response time is within acceptable limits (< 5000ms)")
                .run();
    }
}
