package org.surptech.customerprofiletester.config;

import lombok.Getter;
import org.surptech.common.tester.config.BaseTestConfiguration;

/**
 * Test configuration loader for Customer Profile service tests
 */
@Getter
public class TestConfiguration extends BaseTestConfiguration {
    
    private static TestConfiguration instance;
    
    private TestConfiguration() {
        super();
    }
    
    public static TestConfiguration getInstance() {
        if (instance == null) {
            instance = new TestConfiguration();
        }
        return instance;
    }
    
    public String getCustomerProfileBaseUrl() {
        return getProperty("customer.profile.base.url");
    }
    
    public int getCustomerProfileTimeout() {
        return getIntProperty("customer.profile.timeout.seconds", 30);
    }
    
    public String getValidSsn() {
        return getProperty("test.data.valid.ssn");
    }
    
    public String getInvalidSsn() {
        return getProperty("test.data.invalid.ssn");
    }
    
    public String getNewSsn() {
        return getProperty("test.data.new.ssn");
    }
    
    public String getExpectedSsn() {
        return getProperty("test.expected.ssn");
    }
    
    public String getExpectedFirstName() {
        return getProperty("test.expected.firstName");
    }
    
    public String getExpectedLastName() {
        return getProperty("test.expected.lastName");
    }
    
    public String getExpectedAddress() {
        return getProperty("test.expected.address");
    }
    
    public String getCreateSsn() {
        return getProperty("test.create.ssn");
    }
    
    public String getCreateFirstName() {
        return getProperty("test.create.firstName");
    }
    
    public String getCreateLastName() {
        return getProperty("test.create.lastName");
    }
    
    public String getCreateAddress() {
        return getProperty("test.create.address");
    }
}
