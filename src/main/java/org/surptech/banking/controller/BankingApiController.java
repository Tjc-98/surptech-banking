package org.surptech.banking.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.surptech.banking.dto.CustomerInfoResponse;
import org.surptech.banking.entity.CreditProfile;
import org.surptech.banking.entity.CustomerProfile;
import org.surptech.banking.service.BankingInfoService;
import org.surptech.banking.service.CreditProfileService;
import org.surptech.banking.service.CustomerProfileService;

/**
 * REST API controller for the banking system.
 * All endpoints return JSON and are prefixed with /api.
 */
@RestController
@RequestMapping("/api")
public class BankingApiController {

    private final BankingInfoService bankingInfoService;
    private final CustomerProfileService customerProfileService;
    private final CreditProfileService creditProfileService;

    public BankingApiController(BankingInfoService bankingInfoService,
                                CustomerProfileService customerProfileService,
                                CreditProfileService creditProfileService) {
        this.bankingInfoService = bankingInfoService;
        this.customerProfileService = customerProfileService;
        this.creditProfileService = creditProfileService;
    }

    /**
     * GET /api/customer/info?socialSecurityNumber=XXX-XX-XXXX
     * Returns combined customer and credit information for the given SSN.
     */
    @GetMapping("/customer/info")
    public ResponseEntity<CustomerInfoResponse> getCustomerInfo(
            @RequestParam String socialSecurityNumber) {

        if (socialSecurityNumber == null || socialSecurityNumber.isBlank()) {
            return ResponseEntity.badRequest().build();
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
    public ResponseEntity<CustomerProfile> createCustomerProfile(
            @RequestBody CustomerProfile customerProfile) {

        if (customerProfile.getSocialSecurityNumber() == null
                || customerProfile.getSocialSecurityNumber().isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        CustomerProfile saved = customerProfileService.saveCustomerProfile(customerProfile);
        return ResponseEntity.status(201).body(saved);
    }

    /**
     * POST /api/credit/create
     * Creates or updates a credit profile.
     */
    @PostMapping("/credit/create")
    public ResponseEntity<CreditProfile> createCreditProfile(
            @RequestBody CreditProfile creditProfile) {

        if (creditProfile.getSocialSecurityNumber() == null
                || creditProfile.getSocialSecurityNumber().isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        CreditProfile saved = creditProfileService.saveCreditProfile(creditProfile);
        return ResponseEntity.status(201).body(saved);
    }
}
