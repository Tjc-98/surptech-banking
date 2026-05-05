package org.surptech.dataaggregator.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.surptech.dataaggregator.domain.response.HealthResponse;
import org.surptech.dataaggregator.domain.response.ServiceHealthResponse;
import org.surptech.dataaggregator.health.AuthServerHealthIndicator;
import org.surptech.dataaggregator.health.CreditProfileHealthIndicator;
import org.surptech.dataaggregator.health.CustomerProfileHealthIndicator;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/services/management")
public class HealthController {

    private final CustomerProfileHealthIndicator customerProfileHealthIndicator;
    private final CreditProfileHealthIndicator creditProfileHealthIndicator;
    private final AuthServerHealthIndicator authServerHealthIndicator;

    public HealthController(
            CustomerProfileHealthIndicator customerProfileHealthIndicator,
            CreditProfileHealthIndicator creditProfileHealthIndicator,
            AuthServerHealthIndicator authServerHealthIndicator) {
        this.customerProfileHealthIndicator = customerProfileHealthIndicator;
        this.creditProfileHealthIndicator = creditProfileHealthIndicator;
        this.authServerHealthIndicator = authServerHealthIndicator;
    }

    @GetMapping("/health")
    public ResponseEntity<HealthResponse> servicesHealth() {
        Map<String, ServiceHealthResponse> components = new HashMap<>();

        // Check customer-profile
        boolean isCustomerProfileUp = customerProfileHealthIndicator.isHealthy();
        components.put("customerProfile", ServiceHealthResponse.builder()
                .status(customerProfileHealthIndicator.getStatus())
                .service("customer-profile")
                .url("http://localhost:5551")
                .available(isCustomerProfileUp)
                .build());

        // Check credit-profile
        boolean isCreditProfileUp = creditProfileHealthIndicator.isHealthy();
        components.put("creditProfile", ServiceHealthResponse.builder()
                .status(creditProfileHealthIndicator.getStatus())
                .service("credit-profile")
                .url("http://localhost:5552")
                .available(isCreditProfileUp)
                .build());

        // Check auth-server
        boolean isAuthServerUp = authServerHealthIndicator.isHealthy();
        components.put("authServer", ServiceHealthResponse.builder()
                .status(authServerHealthIndicator.getStatus())
                .service("auth-server")
                .url("http://localhost:5553")
                .available(isAuthServerUp)
                .build());

        // Overall status - UP if at least customer-profile is up (critical service)
        String overallStatus = isCustomerProfileUp ? "UP" : "DOWN";

        HealthResponse response = HealthResponse.builder()
                .status(overallStatus)
                .components(components)
                .build();

        return ResponseEntity.ok(response);
    }
}
