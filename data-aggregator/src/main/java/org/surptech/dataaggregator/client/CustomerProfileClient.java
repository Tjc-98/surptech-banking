package org.surptech.dataaggregator.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.surptech.dataaggregator.domain.response.CustomerProfileResponse;
import org.surptech.dataaggregator.domain.entity.CustomerProfileEntity;
import org.surptech.dataaggregator.mapper.CustomerProfileMapper;
import org.surptech.dataaggregator.service.ApplicationServices;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
public class CustomerProfileClient {

    private final RestClient customerProfileRestClient;

    public CustomerProfileClient(ApplicationServices applicationServices) {
        this.customerProfileRestClient = applicationServices.getCustomerProfileRestClient();
    }

    public Optional<CustomerProfileEntity> getCustomerProfile(String socialSecurityNumber) {
        try {
            log.info("Fetching customer profile for SSN: {}", socialSecurityNumber);
            
            // Create request body
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("social_security_number", socialSecurityNumber);
            
            // Call external service and get DTO response
            CustomerProfileResponse response = customerProfileRestClient.post()
                    .uri("/customer-profile/customer/get")
                    .body(requestBody)
                    .retrieve()
                    .body(CustomerProfileResponse.class);

            if (response != null) {
                log.info("Successfully retrieved customer profile for SSN: {}", socialSecurityNumber);
                // Convert DTO to internal entity
                return Optional.of(CustomerProfileMapper.toEntity(response));
            }
            
            log.warn("Customer profile not found for SSN: {}", socialSecurityNumber);
            return Optional.empty();
            
        } catch (Exception e) {
            log.error("Error fetching customer profile for SSN: {}", socialSecurityNumber, e);
            return Optional.empty();
        }
    }
}
