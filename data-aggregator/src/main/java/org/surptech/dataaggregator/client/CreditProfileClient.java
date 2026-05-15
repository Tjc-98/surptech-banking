package org.surptech.dataaggregator.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.surptech.dataaggregator.domain.response.CreditProfileResponse;
import org.surptech.dataaggregator.domain.entity.CreditProfileEntity;
import org.surptech.dataaggregator.mapper.CreditProfileMapper;
import org.surptech.dataaggregator.service.ApplicationServices;

import java.util.Optional;

@Slf4j
@Component
public class CreditProfileClient {

    private final RestClient creditProfileRestClient;

    public CreditProfileClient(ApplicationServices applicationServices) {
        this.creditProfileRestClient = applicationServices.getCreditProfileRestClient();
    }

    public Optional<CreditProfileEntity> getCreditProfile(String socialSecurityNumber) {
        try {
            log.info("Fetching credit profile for SSN: {}", socialSecurityNumber);
            
            // Call external service with GET and query parameter
            CreditProfileResponse response = creditProfileRestClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/credit-profile/credit/get")
                            .queryParam("socialSecurityNumber", socialSecurityNumber)
                            .build())
                    .retrieve()
                    .body(CreditProfileResponse.class);

            if (response != null) {
                log.info("Successfully retrieved credit profile for SSN: {}", socialSecurityNumber);
                // Convert DTO to internal entity
                return Optional.of(CreditProfileMapper.toEntity(response));
            }
            
            log.warn("Credit profile not found for SSN: {}", socialSecurityNumber);
            return Optional.empty();
            
        } catch (Exception exception) {
            log.error("Error fetching credit profile for SSN: {} - Service may not be available yet", socialSecurityNumber, exception);
            return Optional.empty();
        }
    }
}
