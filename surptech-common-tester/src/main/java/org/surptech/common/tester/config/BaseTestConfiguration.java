package org.surptech.common.tester.config;

import lombok.Getter;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Base test configuration loader for reading application.properties.
 * Subclasses should extend this to provide service-specific configuration.
 */
@Getter
public abstract class BaseTestConfiguration {
    
    protected final Properties properties;
    
    protected BaseTestConfiguration() {
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
    
    /**
     * Get a property value by key
     * 
     * @param key The property key
     * @return The property value or null if not found
     */
    public String getProperty(String key) {
        return properties.getProperty(key);
    }
    
    /**
     * Get a property value by key with a default value
     * 
     * @param key The property key
     * @param defaultValue The default value if property is not found
     * @return The property value or default value
     */
    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
    
    /**
     * Get an integer property value
     * 
     * @param key The property key
     * @param defaultValue The default value if property is not found
     * @return The integer property value
     */
    public int getIntProperty(String key, int defaultValue) {
        String value = properties.getProperty(key);
        if (value == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
