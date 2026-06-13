package org.surptech.banking.service;

import org.springframework.stereotype.Service;
import org.surptech.banking.dto.TransactionRequest;
import org.surptech.banking.dto.TransactionResponse;
import org.surptech.banking.entity.CustomerProfile;
import org.surptech.banking.entity.Transaction;
import org.surptech.banking.repository.TransactionRepository;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CustomerProfileService customerProfileService;

    public TransactionService(TransactionRepository transactionRepository,
                              CustomerProfileService customerProfileService) {
        this.transactionRepository = transactionRepository;
        this.customerProfileService = customerProfileService;
    }

    /**
     * Records a new transaction for a customer.
     * Returns null if the customer does not exist.
     * Returns null if the amount is not positive.
     */
    public TransactionResponse addTransaction(TransactionRequest request) {
        if (request.getAmount() <= 0) {
            return null;
        }

        Optional<CustomerProfile> customer =
                customerProfileService.getCustomerProfile(request.getSocialSecurityNumber());

        if (customer.isEmpty()) {
            return null;
        }

        Transaction transaction = new Transaction(
                customer.get(),
                request.getType(),
                request.getAmount(),
                request.getDescription()
        );

        Transaction saved = transactionRepository.save(transaction);
        return TransactionResponse.from(saved);
    }

    /**
     * Returns all transactions for a customer, newest first.
     */
    public List<TransactionResponse> getTransactionHistory(String socialSecurityNumber) {
        return transactionRepository
                .findByCustomerSocialSecurityNumberOrderByCreatedAtDesc(socialSecurityNumber)
                .stream()
                .map(TransactionResponse::from)
                .toList();
    }
}
