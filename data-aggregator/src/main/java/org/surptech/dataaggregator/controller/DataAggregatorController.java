package org.surptech.dataaggregator.controller;

import lombok.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.surptech.dataaggregator.client.AuthServerClient;
import org.surptech.dataaggregator.domain.CustomerCreditInfo;
import org.surptech.dataaggregator.procedure.GetCustomerCreditInfoProcedure;

@RestController
@RequestMapping("/aggregate")
public class DataAggregatorController extends BaseController {

    private final AuthServerClient authServerClient;

    public DataAggregatorController(AuthServerClient authServerClient) {
        this.authServerClient = authServerClient;
    }

    @ManagedOperation(description = "Retrieve aggregated customer and credit information by social security number")
    @GetMapping(value = "/customer-credit/{socialSecurityNumber}", produces = "application/json")
    public ResponseEntity<CustomerCreditInfo> getCustomerCreditInfo(
            @NonNull @PathVariable String socialSecurityNumber,
            @RequestHeader(value = "Authorization", required = false) String authToken) {

        // Validate token with auth server (placeholder for now)
        if (authToken != null && !authToken.isEmpty()) {
            boolean isValid = authServerClient.validateToken(authToken);
            if (!isValid) {
                return ResponseEntity.status(401).build(); // Unauthorized
            }
        }

        // Execute aggregation procedure
        CustomerCreditInfo customerCreditInfo = runProcedure(
                new GetCustomerCreditInfoProcedure(socialSecurityNumber));

        if (customerCreditInfo != null) {
            return ResponseEntity.ok(customerCreditInfo);
        }

        return ResponseEntity.notFound().build();
    }
}
