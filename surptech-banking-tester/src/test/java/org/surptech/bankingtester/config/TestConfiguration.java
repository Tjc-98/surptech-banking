package org.surptech.bankingtester.config;

import lombok.Getter;
import org.surptech.common.tester.config.BaseTestConfiguration;

/**
 * Test configuration loader for reading application.properties
 * Extends common tester BaseTestConfiguration
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
    
    public String getDataAggregatorBaseUrl() {
        return getProperty("data.aggregator.base.url");
    }
    
    public int getDataAggregatorTimeout() {
        return getIntProperty("data.aggregator.timeout.seconds", 30);
    }
    
    public String getValidSsn() {
        return getProperty("test.data.valid.ssn");
    }
    
    public String getInvalidSsn() {
        return getProperty("test.data.invalid.ssn");
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
}
