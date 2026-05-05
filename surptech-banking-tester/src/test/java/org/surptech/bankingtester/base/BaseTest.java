package org.surptech.bankingtester.base;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.surptech.bankingtester.client.DataAggregatorClient;
import org.surptech.bankingtester.config.TestConfiguration;

/**
 * Base test class providing common setup and utilities
 */
@Slf4j
public abstract class BaseTest {
    
    protected static TestConfiguration config;
    protected static DataAggregatorClient dataAggregatorClient;
    
    @BeforeAll
    public static void globalSetup() {
        log.info("=".repeat(80));
        log.info("SURPTECH BANKING SYSTEM - TEST SUITE INITIALIZATION");
        log.info("=".repeat(80));
        
        config = TestConfiguration.getInstance();
        dataAggregatorClient = new DataAggregatorClient();
        
        log.info("Test Configuration Loaded:");
        log.info("  - Data Aggregator URL: {}", config.getDataAggregatorBaseUrl());
        log.info("  - Valid Test SSN: {}", config.getValidSsn());
        log.info("=".repeat(80));
    }
    
    @BeforeEach
    public void testSetup(TestInfo testInfo) {
        String testId = testInfo.getTestMethod()
                .map(method -> method.getAnnotation(org.surptech.bankingtester.annotation.TestId.class))
                .map(org.surptech.bankingtester.annotation.TestId::value)
                .orElse("N/A");
        
        log.info("");
        log.info("-".repeat(80));
        log.info("Starting Test: {}", testInfo.getDisplayName());
        log.info("Test ID: {}", testId);
        log.info("Test Class: {}", testInfo.getTestClass().map(Class::getSimpleName).orElse("Unknown"));
        log.info("Test Method: {}", testInfo.getTestMethod().map(java.lang.reflect.Method::getName).orElse("Unknown"));
        log.info("-".repeat(80));
    }
    
    @AfterEach
    public void testTeardown(TestInfo testInfo) {
        log.info("-".repeat(80));
        log.info("Completed Test: {}", testInfo.getDisplayName());
        log.info("-".repeat(80));
        log.info("");
    }
}
