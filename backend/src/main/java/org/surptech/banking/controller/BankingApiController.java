package org.surptech.banking.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.surptech.banking.dto.CustomerInfoResponse;
import org.surptech.banking.dto.TransactionRequest;
import org.surptech.banking.dto.TransactionResponse;
import org.surptech.banking.entity.CreditProfile;
import org.surptech.banking.entity.CustomerProfile;
import org.surptech.banking.service.BankingInfoService;
import org.surptech.banking.service.CreditProfileService;
import org.surptech.banking.service.CustomerProfileService;
import org.surptech.banking.service.TransactionService;

import java.util.List;
import java.util.Map;

// All our API routes live here. Everything is under /api.
@RestController
@RequestMapping("/api")
public class BankingApiController {

    // SSNs should look like 123-45-6789
    private static final String SSN_PATTERN = "^\\d{3}-\\d{2}-\\d{4}$";

    private final BankingInfoService bankingInfoService;
    private final CustomerProfileService customerProfileService;
    private final CreditProfileService creditProfileService;
    private final TransactionService transactionService;

    public BankingApiController(BankingInfoService bankingInfoService,
                                CustomerProfileService customerProfileService,
                                CreditProfileService creditProfileService,
                                TransactionService transactionService) {
        this.bankingInfoService = bankingInfoService;
        this.customerProfileService = customerProfileService;
        this.creditProfileService = creditProfileService;
        this.transactionService = transactionService;
    }

    // GET /api/customer/info?socialSecurityNumber=123-45-6789
    // Returns everything we know about a customer - their profile, credit account, and transaction history.
    @GetMapping("/customer/info")
    public ResponseEntity<?> getCustomerInfo(@RequestParam String socialSecurityNumber) {

        if (socialSecurityNumber == null || socialSecurityNumber.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "We need an SSN to search. Please provide one."));
        }

        if (!socialSecurityNumber.matches(SSN_PATTERN)) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "That SSN doesn't look right. It should be in the format XXX-XX-XXXX."));
        }

        CustomerInfoResponse info = bankingInfoService.getCustomerInfo(socialSecurityNumber);

        if (info == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(info);
    }

    // POST /api/customer/create
    // Adds a new customer, or updates an existing one if the SSN already exists.
    @PostMapping("/customer/create")
    public ResponseEntity<?> createCustomerProfile(@RequestBody CustomerProfile customerProfile) {

        if (customerProfile.getSocialSecurityNumber() == null
                || customerProfile.getSocialSecurityNumber().isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "We need an SSN to create a customer profile."));
        }

        if (!customerProfile.getSocialSecurityNumber().matches(SSN_PATTERN)) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "That SSN doesn't look right. It should be in the format XXX-XX-XXXX."));
        }

        if (customerProfile.getFirstName() == null || customerProfile.getFirstName().isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "First name is required."));
        }

        if (customerProfile.getLastName() == null || customerProfile.getLastName().isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Last name is required."));
        }

        if (customerProfile.getAddress() == null || customerProfile.getAddress().isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Please include a home address."));
        }

        CustomerProfile saved = customerProfileService.saveCustomerProfile(customerProfile);
        return ResponseEntity.status(201).body(saved);
    }

    // POST /api/credit/create
    // Sets up a credit account for a customer.
    @PostMapping("/credit/create")
    public ResponseEntity<?> createCreditProfile(@RequestBody CreditProfile creditProfile) {

        if (creditProfile.getSocialSecurityNumber() == null
                || creditProfile.getSocialSecurityNumber().isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "We need an SSN to create a credit profile."));
        }

        if (creditProfile.getFullCreditBalance() <= 0) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "The credit limit needs to be a positive amount."));
        }

        CreditProfile saved = creditProfileService.saveCreditProfile(creditProfile);
        return ResponseEntity.status(201).body(saved);
    }

    // POST /api/transaction/add
    // Records a deposit or withdrawal against a customer's account.
    @PostMapping("/transaction/add")
    public ResponseEntity<?> addTransaction(@RequestBody TransactionRequest request) {

        if (request.getSocialSecurityNumber() == null
                || request.getSocialSecurityNumber().isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "We need an SSN to record a transaction."));
        }

        if (request.getType() == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Please specify the transaction type - either DEPOSIT or WITHDRAWAL."));
        }

        if (request.getAmount() <= 0) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "The transaction amount needs to be more than $0.00."));
        }

        TransactionResponse saved = transactionService.addTransaction(request);

        if (saved == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.status(201).body(saved);
    }

    // GET /api/transaction/history?socialSecurityNumber=123-45-6789
    // Returns a customer's full transaction history, most recent first.
    @GetMapping("/transaction/history")
    public ResponseEntity<?> getTransactionHistory(@RequestParam String socialSecurityNumber) {

        if (socialSecurityNumber == null || socialSecurityNumber.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "We need an SSN to fetch transaction history."));
        }

        if (!socialSecurityNumber.matches(SSN_PATTERN)) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "That SSN doesn't look right. It should be in the format XXX-XX-XXXX."));
        }

        List<TransactionResponse> history =
                transactionService.getTransactionHistory(socialSecurityNumber);

        return ResponseEntity.ok(history);
    }
}
