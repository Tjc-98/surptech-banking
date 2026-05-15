package org.surptech.bankingtester.tests;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.surptech.common.tester.annotation.TestId;
import org.surptech.bankingtester.base.BaseTest;

/**
 * Health check tests for services
 */
@Slf4j
@DisplayName("Service Health Tests")
public class ServiceHealthTest extends BaseTest {
    
    @Test
    @TestId("SHT-01")
    @Tag("smoke")
    @Tag("health")
    @DisplayName("SHT-01: Verify Data Aggregator service is healthy and accessible")
    public void testDataAggregatorHealth() {
        checkDataAggregatorHealth()
                .expectStatusCode(200)
                .run();
    }
}
