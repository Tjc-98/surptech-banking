package org.surptech.dataaggregator.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.surptech.dataaggregator.domain.CustomerProfile;
import org.surptech.dataaggregator.service.ApplicationServices;

import java.util.Optional;

@Slf4j
@Component
public class CustomerProfileClient {

    private final RestClient customerProfileRestClient;

    public CustomerProfileClient(ApplicationServices applicationServices) {
        this.customerProfileRestClient = applicationServices.getCustomerProfileRestClient();
    }

    public Optional<CustomerProfile> getCustomerProfile(String socialSecurityNumber) {
        try {
            log.info("Fetching customer profile for SSN: {}", socialSecurityNumber);
            
            CustomerProfile customerProfile = customerProfileRestClient.get()
                    .uri("/customer/get/{socialSecurityNumber}", socialSecurityNumber)
                    .retrieve()
                    .body(CustomerProfile.class);

            if (customerProfile != null) {
                log.info("Successfully retrieved customer profile for SSN: {}", socialSecurityNumber);
                return Optional.of(customerProfile);
            }
            
            log.warn("Customer profile not found for SSN: {}", socialSecurityNumber);
            return Optional.empty();
            
        } catch (Exception e) {
            log.error("Error fetching customer profile for SSN: {}", socialSecurityNumber, e);
            return Optional.empty();
        }
    }
}
