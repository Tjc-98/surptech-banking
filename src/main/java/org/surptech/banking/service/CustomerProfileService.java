package org.surptech.banking.service;

import org.springframework.stereotype.Service;
import org.surptech.banking.entity.CustomerProfile;
import org.surptech.banking.repository.CustomerProfileRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerProfileService {

    private final CustomerProfileRepository customerProfileRepository;

    public CustomerProfileService(CustomerProfileRepository customerProfileRepository) {
        this.customerProfileRepository = customerProfileRepository;
    }

    public Optional<CustomerProfile> getCustomerProfile(String socialSecurityNumber) {
        return customerProfileRepository.findById(socialSecurityNumber);
    }

    public CustomerProfile saveCustomerProfile(CustomerProfile customerProfile) {
        return customerProfileRepository.save(customerProfile);
    }

    public List<CustomerProfile> getAllCustomerProfiles() {
        return customerProfileRepository.findAll();
    }
}
