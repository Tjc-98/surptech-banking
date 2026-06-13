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

/**
 * REST API controller for the banking system.
 * All endpoints return JSON and are prefixed with /api.
 */
@RestController
@RequestMapping("/api")
public class BankingApiController {

    // SSN format: XXX-XX-XXXX
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

    /**
     * GET /api/customer/info?socialSecurityNumber=XXX-XX-XXXX
     * Returns combined customer, credit, and transaction info for the given SSN.
     */
    @GetMapping("/customer/info")
    public ResponseEntity<?> getCustomerInfo(@RequestParam String socialSecurityNumber) {

        if (socialSecurityNumber == null || socialSecurityNumber.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Social Security Number is required."));
        }

        if (!socialSecurityNumber.matches(SSN_PATTERN)) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Invalid SSN format. Expected format: XXX-XX-XXXX."));
        }

        CustomerInfoResponse info = bankingInfoService.getCustomerInfo(socialSecurityNumber);

        if (info == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(info);
    }

    /**
     * POST /api/customer/create
     * Creates or updates a customer profile.
     */
    @PostMapping("/customer/create")
    public ResponseEntity<?> createCustomerProfile(@RequestBody CustomerProfile customerProfile) {

        if (customerProfile.getSocialSecurityNumber() == null
                || customerProfile.getSocialSecurityNumber().isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Social Security Number is required."));
        }

        if (!customerProfile.getSocialSecurityNumber().matches(SSN_PATTERN)) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Invalid SSN format. Expected format: XXX-XX-XXXX."));
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
                    .body(Map.of("error", "Address is required."));
        }

        CustomerProfile saved = customerProfileService.saveCustomerProfile(customerProfile);
        return ResponseEntity.status(201).body(saved);
    }

    /**
     * POST /api/credit/create
     * Creates or updates a credit profile.
     */
    @PostMapping("/credit/create")
    public ResponseEntity<?> createCreditProfile(@RequestBody CreditProfile creditProfile) {

        if (creditProfile.getSocialSecurityNumber() == null
                || creditProfile.getSocialSecurityNumber().isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Social Security Number is required."));
        }

        if (creditProfile.getFullCreditBalance() <= 0) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Full credit balance must be greater than zero."));
        }

        CreditProfile saved = creditProfileService.saveCreditProfile(creditProfile);
        return ResponseEntity.status(201).body(saved);
    }

    /**
     * POST /api/transaction/add
     * Records a deposit or withdrawal for a customer.
     */
    @PostMapping("/transaction/add")
    public ResponseEntity<?> addTransaction(@RequestBody TransactionRequest request) {

        if (request.getSocialSecurityNumber() == null
                || request.getSocialSecurityNumber().isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Social Security Number is required."));
        }

        if (request.getType() == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Transaction type is required (DEPOSIT or WITHDRAWAL)."));
        }

        if (request.getAmount() <= 0) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Amount must be greater than zero."));
        }

        TransactionResponse saved = transactionService.addTransaction(request);

        if (saved == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.status(201).body(saved);
    }

    /**
     * GET /api/transaction/history?socialSecurityNumber=XXX-XX-XXXX
     * Returns transaction history for a customer, newest first.
     */
    @GetMapping("/transaction/history")
    public ResponseEntity<?> getTransactionHistory(@RequestParam String socialSecurityNumber) {

        if (socialSecurityNumber == null || socialSecurityNumber.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Social Security Number is required."));
        }

        if (!socialSecurityNumber.matches(SSN_PATTERN)) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Invalid SSN format. Expected format: XXX-XX-XXXX."));
        }

        List<TransactionResponse> history =
                transactionService.getTransactionHistory(socialSecurityNumber);

        return ResponseEntity.ok(history);
    }
}
