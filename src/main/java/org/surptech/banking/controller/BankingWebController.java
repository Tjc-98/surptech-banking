package org.surptech.banking.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.surptech.banking.dto.CustomerInfoResponse;
import org.surptech.banking.service.BankingInfoService;

/**
 * Web controller that serves Thymeleaf HTML pages.
 */
@Controller
public class BankingWebController {

    private final BankingInfoService bankingInfoService;

    public BankingWebController(BankingInfoService bankingInfoService) {
        this.bankingInfoService = bankingInfoService;
    }

    /**
     * GET /
     * Serves the main search page.
     */
    @GetMapping("/")
    public String index() {
        return "index";
    }

    /**
     * GET /search?socialSecurityNumber=XXX-XX-XXXX
     * Looks up a customer by SSN and renders the result page.
     */
    @GetMapping("/search")
    public String search(@RequestParam(required = false) String socialSecurityNumber, Model model) {

        if (socialSecurityNumber == null || socialSecurityNumber.isBlank()) {
            model.addAttribute("error", "Please enter a Social Security Number.");
            return "index";
        }

        CustomerInfoResponse info = bankingInfoService.getCustomerInfo(socialSecurityNumber);

        if (info == null) {
            model.addAttribute("error", "No customer found for SSN: " + socialSecurityNumber);
            return "index";
        }

        model.addAttribute("customer", info);
        return "result";
    }
}
