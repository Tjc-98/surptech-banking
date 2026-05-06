package org.surptech.bankingtester.tests;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.surptech.bankingtester.annotation.TestId;
import org.surptech.bankingtester.base.BaseTest;

/**
 * Tests for error handling scenarios
 */
@Slf4j
@DisplayName("Error Handling Tests")
public class ErrorHandlingTest extends BaseTest {
    
    @Test
    @TestId("EHT-01")
    @Tag("error-handling")
    @DisplayName("EHT-01: Verify handling of invalid SSN")
    public void testGetCustomerBankingInfo_InvalidSSN() {
        getCustomerBankingInfo()
                .withSsn(config.getInvalidSsn())
                .expectStatusCode(404)
                .withDescription("Verify handling of invalid SSN - expect 404")
                .run();
    }
    
    @Test
    @TestId("EHT-02")
    @Tag("error-handling")
    @DisplayName("EHT-02: Verify handling of null SSN")
    public void testGetCustomerBankingInfo_NullSSN() {
        getCustomerBankingInfo()
                .withSsn(null)
                .expectStatusCode(400)
                .withDescription("Verify handling of null SSN - expect 400")
                .run();
    }
    
    @Test
    @TestId("EHT-03")
    @Tag("error-handling")
    @DisplayName("EHT-03: Verify handling of empty SSN")
    public void testGetCustomerBankingInfo_EmptySSN() {
        getCustomerBankingInfo()
                .withSsn("")
                .expectStatusCode(400)
                .withDescription("Verify handling of empty SSN - expect 400")
                .run();
    }
    
    @Test
    @TestId("EHT-04")
    @Tag("error-handling")
    @DisplayName("EHT-04: Verify handling of malformed SSN format")
    public void testGetCustomerBankingInfo_MalformedSSN() {
        getCustomerBankingInfo()
                .withSsn("INVALID-SSN-FORMAT")
                .expectStatusCode(400)
                .withDescription("Verify handling of malformed SSN - expect 400")
                .run();
    }
}
