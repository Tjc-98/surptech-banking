package org.surptech.common.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * Spring Boot auto-configuration for the surptech-common library.
 *
 * This configuration class automatically enables component scanning for all components in the
 * org.surptech.common package, allowing them to be registered as Spring beans in applications
 * that depend on the surptech-common library.
 *
 * This auto-configuration is processed automatically when surptech-common is included as a dependency,
 * eliminating the need for manual configuration by consuming modules.
 */
@AutoConfiguration
@ComponentScan(basePackages = "org.surptech.common")
public class CommonAutoConfiguration {
    // Auto-configuration class - components are auto-discovered via component scanning
}
