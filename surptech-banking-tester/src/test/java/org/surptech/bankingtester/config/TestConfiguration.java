package org.surptech.bankingtester.config;

import lombok.Getter;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Test configuration loader for reading application.properties
 */
@Getter
public class TestConfiguration {
    
    private static TestConfiguration instance;
    private final Properties properties;
    
    private TestConfiguration() {
        properties = new Properties();
        try (InputStream input = getClass().getClassLoader()
                .getResourceAsStream("application.properties")) {
            if (input == null) {
                throw new RuntimeException("Unable to find application.properties");
            }
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load test configuration", e);
        }
    }
    
    public static TestConfiguration getInstance() {
        if (instance == null) {
            instance = new TestConfiguration();
        }
        return instance;
    }
    
    public String getDataAggregatorBaseUrl() {
        return properties.getProperty("data.aggregator.base.url");
    }
    
    public int getDataAggregatorTimeout() {
        return Integer.parseInt(properties.getProperty("data.aggregator.timeout.seconds", "30"));
    }
    
    public String getValidSsn() {
        return properties.getProperty("test.data.valid.ssn");
    }
    
    public String getInvalidSsn() {
        return properties.getProperty("test.data.invalid.ssn");
    }
    
    public String getExpectedSsn() {
        return properties.getProperty("test.expected.ssn");
    }
    
    public String getExpectedFirstName() {
        return properties.getProperty("test.expected.firstName");
    }
    
    public String getExpectedLastName() {
        return properties.getProperty("test.expected.lastName");
    }
    
    public String getExpectedAddress() {
        return properties.getProperty("test.expected.address");
    }
}
