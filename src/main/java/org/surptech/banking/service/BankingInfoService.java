package org.surptech.banking.service;

import org.springframework.stereotype.Service;
import org.surptech.banking.dto.CustomerInfoResponse;
import org.surptech.banking.entity.CreditProfile;
import org.surptech.banking.entity.CustomerProfile;

import java.util.Optional;

/**
 * Service that aggregates customer personal and credit information
 * into a single response object.
 */
@Service
public class BankingInfoService {

    private final CustomerProfileService customerProfileService;
    private final CreditProfileService creditProfileService;

    public BankingInfoService(CustomerProfileService customerProfileService,
                              CreditProfileService creditProfileService) {
        this.customerProfileService = customerProfileService;
        this.creditProfileService = creditProfileService;
    }

    /**
     * Looks up a customer by SSN and combines their personal and credit information.
     *
     * @param socialSecurityNumber the SSN to look up
     * @return the combined info, or null if neither profile is found
     */
    public CustomerInfoResponse getCustomerInfo(String socialSecurityNumber) {
        Optional<CustomerProfile> customerProfile = customerProfileService.getCustomerProfile(socialSecurityNumber);
        Optional<CreditProfile> creditProfile = creditProfileService.getCreditProfile(socialSecurityNumber);

        // If nothing found, return null so the controller can send a 404
        if (customerProfile.isEmpty() && creditProfile.isEmpty()) {
            return null;
        }

        CustomerInfoResponse response = new CustomerInfoResponse();
        response.setSocialSecurityNumber(socialSecurityNumber);

        if (customerProfile.isPresent()) {
            CustomerProfile customer = customerProfile.get();
            response.setFirstName(customer.getFirstName());
            response.setLastName(customer.getLastName());
            response.setAddress(customer.getAddress());
        }

        if (creditProfile.isPresent()) {
            CreditProfile credit = creditProfile.get();
            response.setFullCreditBalance(credit.getFullCreditBalance());
            response.setSpendBalance(credit.getSpendBalance());
            response.setInterestRate(credit.getInterestRate());
        }

        return response;
    }
}
