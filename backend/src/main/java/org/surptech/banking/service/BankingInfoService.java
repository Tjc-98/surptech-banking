package org.surptech.banking.service;

import org.springframework.stereotype.Service;
import org.surptech.banking.dto.CustomerInfoResponse;
import org.surptech.banking.entity.CreditProfile;
import org.surptech.banking.entity.CustomerProfile;

import java.util.Optional;

// Pulls together everything we know about a customer into one response object.
// Rather than making the frontend call three separate endpoints, this does it all in one go.
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

        // Nothing found at all - let the controller return a 404
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
            // Available balance = how much credit is left to use
            response.setAvailableBalance(credit.getFullCreditBalance() - credit.getSpendBalance());
            response.setInterestRate(credit.getInterestRate());
        }

        // Tack on the transaction history so the frontend has everything it needs
        response.setTransactions(transactionService.getTransactionHistory(socialSecurityNumber));

        return response;
    }
}
