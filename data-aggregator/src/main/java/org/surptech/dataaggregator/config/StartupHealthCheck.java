package org.surptech.dataaggregator.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.surptech.dataaggregator.health.AuthServerHealthIndicator;
import org.surptech.dataaggregator.health.CreditProfileHealthIndicator;
import org.surptech.dataaggregator.health.CustomerProfileHealthIndicator;

@Slf4j
@Component
public class StartupHealthCheck {

    private final CustomerProfileHealthIndicator customerProfileHealthIndicator;
    private final CreditProfileHealthIndicator creditProfileHealthIndicator;
    private final AuthServerHealthIndicator authServerHealthIndicator;

    public StartupHealthCheck(
            CustomerProfileHealthIndicator customerProfileHealthIndicator,
            CreditProfileHealthIndicator creditProfileHealthIndicator,
            AuthServerHealthIndicator authServerHealthIndicator) {
        this.customerProfileHealthIndicator = customerProfileHealthIndicator;
        this.creditProfileHealthIndicator = creditProfileHealthIndicator;
        this.authServerHealthIndicator = authServerHealthIndicator;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void checkExternalServicesHealth() {
        log.info("=".repeat(80));
        log.info("Performing startup health checks for external services...");
        log.info("=".repeat(80));

        // Check customer-profile
        String customerProfileStatus = customerProfileHealthIndicator.getStatus();
        logHealthStatus("Customer Profile", customerProfileStatus);

        // Check credit-profile
        String creditProfileStatus = creditProfileHealthIndicator.getStatus();
        logHealthStatus("Credit Profile", creditProfileStatus);

        // Check auth-server
        String authServerStatus = authServerHealthIndicator.getStatus();
        logHealthStatus("Auth Server", authServerStatus);

        log.info("=".repeat(80));
        log.info("Startup health checks completed");
        log.info("=".repeat(80));
    }

    private void logHealthStatus(String serviceName, String status) {
        if ("UP".equals(status)) {
            log.info("✓ {} service: {}", serviceName, status);
        } else {
            log.warn("✗ {} service: {}", serviceName, status);
        }
    }
}
