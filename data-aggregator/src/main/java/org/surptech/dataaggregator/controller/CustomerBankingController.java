package org.surptech.dataaggregator.controller;

import lombok.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.surptech.dataaggregator.client.AuthServerClient;
import org.surptech.dataaggregator.domain.response.CustomerCreditInfoResponse;
import org.surptech.dataaggregator.procedure.GetCustomerCreditInfoProcedure;

@RestController
@RequestMapping("/customer")
public class CustomerBankingController extends BaseController {

    private final AuthServerClient authServerClient;

    public CustomerBankingController(AuthServerClient authServerClient) {
        this.authServerClient = authServerClient;
    }

    @GetMapping(value = "/info", produces = "application/json")
    public ResponseEntity<CustomerCreditInfoResponse> getCustomerCreditInfo(
            @NonNull @RequestParam("socialSecurityNumber") String socialSecurityNumber,
            @RequestHeader(value = "Authorization", required = false) String authToken) {

        // Validate token with auth server (placeholder for now)
        if (authToken != null && !authToken.isEmpty()) {
            boolean isValid = authServerClient.validateToken(authToken);
            if (!isValid) {
                return ResponseEntity.status(401).build();
            }
        }

        // Execute aggregation procedure
        CustomerCreditInfoResponse customerCreditInfo = runProcedure(
                new GetCustomerCreditInfoProcedure(socialSecurityNumber));

        if (customerCreditInfo != null) {
            return ResponseEntity.ok(customerCreditInfo);
        }

        return ResponseEntity.notFound().build();
    }
}
