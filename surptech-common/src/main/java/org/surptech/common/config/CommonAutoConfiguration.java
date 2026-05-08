package org.surptech.common.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * Auto-configuration for surptech-common library.
 * Automatically enables component scanning for common components.
 */
@AutoConfiguration
@ComponentScan(basePackages = "org.surptech.common")
public class CommonAutoConfiguration {
    // Auto-configuration class - components will be auto-discovered
}
