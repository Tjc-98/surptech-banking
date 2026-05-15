package org.surptech.customerprofiletester.base;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.surptech.common.tester.base.BaseTest;
import org.surptech.customerprofiletester.client.CustomerProfileClient;
import org.surptech.customerprofiletester.config.TestConfiguration;
import org.surptech.customerprofiletester.teststep.CreateCustomerProfileTestStep;
import org.surptech.customerprofiletester.teststep.CustomerProfileHealthTestStep;
import org.surptech.customerprofiletester.teststep.GetCustomerProfileTestStep;

/**
 * Base test class for Customer Profile tests providing common clients and test step factories
 */
@Slf4j
public abstract class BaseCustomerProfileTest extends BaseTest {
    
    protected static TestConfiguration config;
    protected static CustomerProfileClient customerProfileClient;
    
    @BeforeAll
    public static void initializeTestResources() {
        log.info("=".repeat(80));
        log.info("CUSTOMER PROFILE SERVICE - TEST SUITE INITIALIZATION");
        log.info("=".repeat(80));
        
        config = TestConfiguration.getInstance();
        customerProfileClient = new CustomerProfileClient();
        
        log.info("Test Configuration Loaded:");
        log.info("  - Customer Profile URL: {}", config.getCustomerProfileBaseUrl());
        log.info("  - Valid Test SSN: {}", config.getValidSsn());
        log.info("=".repeat(80));
    }
    
    // ==================== Test Step Factory Methods ====================
    
    /**
     * Create a new test step for checking customer profile service health
     * 
     * @return CustomerProfileHealthTestStep instance
     */
    protected CustomerProfileHealthTestStep checkCustomerProfileHealth() {
        CustomerProfileHealthTestStep step = new CustomerProfileHealthTestStep(customerProfileClient);
        step.setStepLogger(this::logStep);
        return step;
    }
    
    /**
     * Create a new test step for getting customer profile
     * 
     * @return GetCustomerProfileTestStep instance
     */
    protected GetCustomerProfileTestStep getCustomerProfile() {
        GetCustomerProfileTestStep step = new GetCustomerProfileTestStep(customerProfileClient);
        step.setStepLogger(this::logStep);
        return step;
    }
    
    /**
     * Create a new test step for creating customer profile
     * 
     * @return CreateCustomerProfileTestStep instance
     */
    protected CreateCustomerProfileTestStep createCustomerProfile() {
        CreateCustomerProfileTestStep step = new CreateCustomerProfileTestStep(customerProfileClient);
        step.setStepLogger(this::logStep);
        return step;
    }
}
