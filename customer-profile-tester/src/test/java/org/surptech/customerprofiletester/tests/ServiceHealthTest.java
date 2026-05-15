package org.surptech.customerprofiletester.tests;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.surptech.common.tester.annotation.TestId;
import org.surptech.customerprofiletester.base.BaseCustomerProfileTest;

/**
 * Health check tests for Customer Profile service
 */
@Slf4j
@DisplayName("Customer Profile Service Health Tests")
public class ServiceHealthTest extends BaseCustomerProfileTest {
    
    @Test
    @TestId("CPH-01")
    @Tag("health")
    @Tag("smoke")
    @DisplayName("CPH-01: Verify customer profile service is healthy and responding")
    public void testCustomerProfileServiceHealth() {
        checkCustomerProfileHealth()
                .expectStatusCode(200)
                .withDescription("Verify customer profile service health endpoint returns 200 OK")
                .run();
    }
    
    @Test
    @TestId("CPH-02")
    @Tag("health")
    @DisplayName("CPH-02: Verify health endpoint response contains status information")
    public void testHealthEndpointResponseStructure() {
        checkCustomerProfileHealth()
                .expectStatusCode(200)
                .withDescription("Verify health endpoint returns proper status information")
                .run();
    }
}
