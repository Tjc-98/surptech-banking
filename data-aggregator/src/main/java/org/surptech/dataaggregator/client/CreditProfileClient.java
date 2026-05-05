package org.surptech.dataaggregator.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.surptech.dataaggregator.domain.CreditProfile;
import org.surptech.dataaggregator.service.ApplicationServices;

import java.util.Optional;

@Slf4j
@Component
public class CreditProfileClient {

    private final RestClient creditProfileRestClient;

    public CreditProfileClient(ApplicationServices applicationServices) {
        this.creditProfileRestClient = applicationServices.getCreditProfileRestClient();
    }

    public Optional<CreditProfile> getCreditProfile(String socialSecurityNumber) {
        try {
            log.info("Fetching credit profile for SSN: {}", socialSecurityNumber);
            
            CreditProfile creditProfile = creditProfileRestClient.get()
                    .uri("/credit/get/{socialSecurityNumber}", socialSecurityNumber)
                    .retrieve()
                    .body(CreditProfile.class);

            if (creditProfile != null) {
                log.info("Successfully retrieved credit profile for SSN: {}", socialSecurityNumber);
                return Optional.of(creditProfile);
            }
            
            log.warn("Credit profile not found for SSN: {}", socialSecurityNumber);
            return Optional.empty();
            
        } catch (Exception e) {
            log.error("Error fetching credit profile for SSN: {} - Service may not be available yet", socialSecurityNumber, e);
            return Optional.empty();
        }
    }
}
