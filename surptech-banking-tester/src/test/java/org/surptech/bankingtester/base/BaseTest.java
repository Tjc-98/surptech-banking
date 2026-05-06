package org.surptech.bankingtester.base;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.surptech.bankingtester.client.DataAggregatorClient;
import org.surptech.bankingtester.config.TestConfiguration;
import org.surptech.bankingtester.teststep.DataAggregatorHealthTestStep;
import org.surptech.bankingtester.teststep.GetCustomerBankingInfoTestStep;
import org.surptech.bankingtester.teststep.TestStep;

/**
 * Base test class providing common clients, configuration, and utilities.
 * Extend TestLifecycleManager for test lifecycle management and logging.
 */
@Slf4j
public abstract class BaseTest extends TestLifecycleManager {
    
    protected static TestConfiguration config;
    protected static DataAggregatorClient dataAggregatorClient;
    
    @BeforeAll
    public static void initializeTestResources() {
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
    
    // ==================== Test Step Factory Methods ====================
    
    /**
     * Create a new test step for checking Data Aggregator health
     * 
     * @return DataAggregatorHealthTestStep instance
     */
    protected DataAggregatorHealthTestStep checkDataAggregatorHealth() {
        DataAggregatorHealthTestStep step = new DataAggregatorHealthTestStep(dataAggregatorClient);
        step.setStepLogger(this::logStep);
        return step;
    }
    
    /**
     * Create a new test step for getting customer banking information
     * 
     * @return GetCustomerBankingInfoTestStep instance
     */
    protected GetCustomerBankingInfoTestStep getCustomerBankingInfo() {
        GetCustomerBankingInfoTestStep step = new GetCustomerBankingInfoTestStep(dataAggregatorClient);
        step.setStepLogger(this::logStep);
        return step;
    }
}

