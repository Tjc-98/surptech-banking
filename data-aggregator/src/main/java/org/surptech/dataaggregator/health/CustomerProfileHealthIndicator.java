package org.surptech.dataaggregator.health;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Slf4j
@Component
public class CustomerProfileHealthIndicator {

    private final RestClient customerProfileRestClient;

    public CustomerProfileHealthIndicator(@Qualifier("customerProfileRestClient") RestClient customerProfileRestClient) {
        this.customerProfileRestClient = customerProfileRestClient;
    }

    public boolean isHealthy() {
        try {
            log.debug("Checking customer-profile service health...");
            
            String response = customerProfileRestClient.get()
                    .uri("/customer-profile/management/health")
                    .retrieve()
                    .body(String.class);

            if (response != null && response.contains("UP")) {
                log.info("Customer-profile service is UP");
                return true;
            } else {
                log.warn("Customer-profile service returned unexpected response");
                return false;
            }
        } catch (Exception e) {
            log.error("Customer-profile service is DOWN: {}", e.getMessage());
            return false;
        }
    }

    public String getStatus() {
        return isHealthy() ? "UP" : "DOWN";
    }
}
