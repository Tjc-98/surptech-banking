package org.surptech.customerprofiletester.tests;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.surptech.common.tester.annotation.TestId;
import org.surptech.customerprofiletester.base.BaseCustomerProfileTest;

/**
 * Integration tests for creating customer profile
 */
@Slf4j
@DisplayName("Create Customer Profile Tests")
public class CreateCustomerProfileTest extends BaseCustomerProfileTest {
    
    @Test
    @TestId("CCPT-01")
    @Tag("smoke")
    @Tag("integration")
    @DisplayName("CCPT-01: Verify successful creation of new customer profile")
    public void testCreateCustomerProfile_Success() {
        createCustomerProfile()
                .withCustomerData(
                        config.getCreateSsn(),
                        config.getCreateFirstName(),
                        config.getCreateLastName(),
                        config.getCreateAddress())
                .expectStatusCode(201)
                .verifyCreated()
                .run();
    }
    
    @Test
    @TestId("CCPT-02")
    @Tag("integration")
    @DisplayName("CCPT-02: Verify created customer profile can be retrieved")
    public void testCreateCustomerProfile_ThenRetrieve() {
        // First create the profile
        createCustomerProfile()
                .withCustomerData(
                        config.getCreateSsn(),
                        config.getCreateFirstName(),
                        config.getCreateLastName(),
                        config.getCreateAddress())
                .expectStatusCode(201)
                .verifyCreated()
                .withDescription("Create new customer profile")
                .run();
        
        // Then retrieve it to verify
        getCustomerProfile()
                .withSsn(config.getCreateSsn())
                .expectStatusCode(200)
                .verifyCustomerProfile(
                        config.getCreateSsn(),
                        config.getCreateFirstName(),
                        config.getCreateLastName(),
                        config.getCreateAddress())
                .withDescription("Retrieve the newly created customer profile")
                .run();
    }
    
    @Test
    @TestId("CCPT-03")
    @Tag("integration")
    @DisplayName("CCPT-03: Verify updating existing customer profile")
    public void testCreateCustomerProfile_UpdateExisting() {
        // Create or update profile
        createCustomerProfile()
                .withCustomerData(
                        config.getValidSsn(),
                        "Updated",
                        "Name",
                        "Updated Address")
                .expectStatusCode(201)
                .verifyCreated()
                .withDescription("Update existing customer profile")
                .run();
    }
}
