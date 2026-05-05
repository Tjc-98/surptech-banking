package org.surptech.dataaggregator.health;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Slf4j
@Component
public class AuthServerHealthIndicator {

    private final RestClient authServerRestClient;

    public AuthServerHealthIndicator(@Qualifier("authServerRestClient") RestClient authServerRestClient) {
        this.authServerRestClient = authServerRestClient;
    }

    public boolean isHealthy() {
        try {
            log.debug("Checking auth-server service health...");
            
            String response = authServerRestClient.get()
                    .uri("/management/health")
                    .retrieve()
                    .body(String.class);

            if (response != null && response.contains("UP")) {
                log.info("Auth-server service is UP");
                return true;
            } else {
                log.warn("Auth-server service returned unexpected response");
                return false;
            }
        } catch (Exception exception) {
            log.warn("Auth-server service is DOWN (not yet implemented): {}", exception.getMessage());
            return false;
        }
    }

    public String getStatus() {
        return isHealthy() ? "UP" : "DOWN";
    }
}
