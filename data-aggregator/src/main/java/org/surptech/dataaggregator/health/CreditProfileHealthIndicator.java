package org.surptech.dataaggregator.health;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Slf4j
@Component
public class CreditProfileHealthIndicator {

    private final RestClient creditProfileRestClient;

    public CreditProfileHealthIndicator(@Qualifier("creditProfileRestClient") RestClient creditProfileRestClient) {
        this.creditProfileRestClient = creditProfileRestClient;
    }

    public boolean isHealthy() {
        try {
            log.debug("Checking credit-profile service health...");
            
            String response = creditProfileRestClient.get()
                    .uri("/management/health")
                    .retrieve()
                    .body(String.class);

            if (response != null && response.contains("UP")) {
                log.info("Credit-profile service is UP");
                return true;
            } else {
                log.warn("Credit-profile service returned unexpected response");
                return false;
            }
        } catch (Exception exception) {
            log.warn("Credit-profile service is DOWN (not yet implemented): {}", exception.getMessage());
            return false;
        }
    }

    public String getStatus() {
        return isHealthy() ? "UP" : "DOWN";
    }
}
