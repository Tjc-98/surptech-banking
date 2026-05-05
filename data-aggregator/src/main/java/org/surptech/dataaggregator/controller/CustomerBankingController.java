package org.surptech.dataaggregator.controller;

import lombok.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.surptech.dataaggregator.client.AuthServerClient;
import org.surptech.dataaggregator.domain.CustomerCreditInfoResponse;
import org.surptech.dataaggregator.domain.CustomerInfoRequest;
import org.surptech.dataaggregator.procedure.GetCustomerCreditInfoProcedure;

@RestController
@RequestMapping("/customer")
public class CustomerBankingController extends BaseController {

    private final AuthServerClient authServerClient;

    public CustomerBankingController(AuthServerClient authServerClient) {
        this.authServerClient = authServerClient;
    }

    @ManagedOperation(description = "Retrieve aggregated customer and credit information by social security number")
    @PostMapping(value = "/info", consumes = "application/json", produces = "application/json")
    public ResponseEntity<CustomerCreditInfoResponse> getCustomerCreditInfo(
            @NonNull @RequestBody CustomerInfoRequest request,
            @RequestHeader(value = "Authorization", required = false) String authToken) {

        // Validate token with auth server (placeholder for now)
        if (authToken != null && !authToken.isEmpty()) {
            boolean isValid = authServerClient.validateToken(authToken);
            if (!isValid) {
                return ResponseEntity.status(401).build(); // Unauthorized
            }
        }

        // Execute aggregation procedure
        CustomerCreditInfoResponse customerCreditInfo = runProcedure(
                new GetCustomerCreditInfoProcedure(request.getSocialSecurityNumber()));

        if (customerCreditInfo != null) {
            return ResponseEntity.ok(customerCreditInfo);
        }

        return ResponseEntity.notFound().build();
    }
}
