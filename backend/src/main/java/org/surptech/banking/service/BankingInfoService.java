package org.surptech.banking.service;

import org.springframework.stereotype.Service;
import org.surptech.banking.dto.CustomerInfoResponse;
import org.surptech.banking.entity.CreditProfile;
import org.surptech.banking.entity.CustomerProfile;

import java.util.Optional;

/**
 * Aggregates customer personal, credit, and transaction information into one response.
 */
@Service
public class BankingInfoService {

    private final CustomerProfileService customerProfileService;
    private final CreditProfileService creditProfileService;
    private final TransactionService transactionService;

    public BankingInfoService(CustomerProfileService customerProfileService,
                              CreditProfileService creditProfileService,
                              TransactionService transactionService) {
        this.customerProfileService = customerProfileService;
        this.creditProfileService = creditProfileService;
        this.transactionService = transactionService;
    }

    public CustomerInfoResponse getCustomerInfo(String socialSecurityNumber) {
        Optional<CustomerProfile> customerProfile =
                customerProfileService.getCustomerProfile(socialSecurityNumber);
        Optional<CreditProfile> creditProfile =
                creditProfileService.getCreditProfile(socialSecurityNumber);

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
            response.setAvailableBalance(credit.getFullCreditBalance() - credit.getSpendBalance());
            response.setInterestRate(credit.getInterestRate());
        }

        // Include transaction history
        response.setTransactions(transactionService.getTransactionHistory(socialSecurityNumber));

        return response;
    }
}
