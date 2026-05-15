package org.surptech.customerprofiletester.tests;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.surptech.common.tester.annotation.TestId;
import org.surptech.customerprofiletester.base.BaseCustomerProfileTest;

/**
 * Integration tests for getting customer profile
 */
@Slf4j
@DisplayName("Get Customer Profile Tests")
public class GetCustomerProfileTest extends BaseCustomerProfileTest {
    
    @Test
    @TestId("GCPT-01")
    @Tag("smoke")
    @Tag("integration")
    @DisplayName("GCPT-01: Verify successful retrieval of customer profile with valid SSN")
    public void testGetCustomerProfile_ValidSSN_Success() {
        getCustomerProfile()
                .withSsn(config.getValidSsn())
                .expectStatusCode(200)
                .verifyResponseNotNull()
                .verifyCustomerProfile(
                        config.getExpectedSsn(),
                        config.getExpectedFirstName(),
                        config.getExpectedLastName(),
                        config.getExpectedAddress())
                .run();
    }
    
    @Test
    @TestId("GCPT-02")
    @Tag("integration")
    @DisplayName("GCPT-02: Verify customer profile data structure and completeness")
    public void testGetCustomerProfile_ValidateDataStructure() {
        getCustomerProfile()
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
    @TestId("GCPT-03")
    @Tag("smoke")
    @DisplayName("GCPT-03: Verify response time is within acceptable limits")
    public void testGetCustomerProfile_ResponseTime() {
        getCustomerProfile()
                .withSsn(config.getValidSsn())
                .expectStatusCode(200)
                .expectResponseTimeUnder(3000)
                .withDescription("Validate response time is within acceptable limits (< 3000ms)")
                .run();
    }
    
    @Test
    @TestId("GCPT-04")
    @Tag("error-handling")
    @DisplayName("GCPT-04: Verify 404 response for non-existent customer profile")
    public void testGetCustomerProfile_NotFound() {
        getCustomerProfile()
                .withSsn(config.getInvalidSsn())
                .expectStatusCode(404)
                .withDescription("Verify 404 response for non-existent SSN")
                .run();
    }
    
    @Test
    @TestId("GCPT-05")
    @Tag("integration")
    @DisplayName("GCPT-05: Verify all customer profile fields are populated correctly")
    public void testGetCustomerProfile_AllFieldsPopulated() {
        getCustomerProfile()
                .withSsn(config.getValidSsn())
                .expectStatusCode(200)
                .verifyResponseNotNull()
                .verifyCustomerProfile(
                        config.getExpectedSsn(),
                        config.getExpectedFirstName(),
                        config.getExpectedLastName(),
                        config.getExpectedAddress())
                .withDescription("Verify all customer profile fields are correctly populated")
                .run();
    }
}
