package org.surptech.customerprofile.controller;

import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.surptech.customerprofile.domain.CustomerProfile;
import org.surptech.customerprofile.domain.CustomerProfileRequest;
import org.surptech.customerprofile.procedure.CreateCustomerProfileProcedure;
import org.surptech.customerprofile.procedure.GetCustomerProfileProcedure;

@RestController
@RequestMapping("/customer")
public class CustomerProfileController extends BaseController {

    @ManagedOperation(description = "Retrieve customer profile by social security number")
    @GetMapping(value = "/get/{socialSecurityNumber}", produces = "application/json")
    public ResponseEntity<CustomerProfile> getCustomerProfile(
            @NonNull @PathVariable String socialSecurityNumber) {

        CustomerProfile customerProfile = runProcedure(new GetCustomerProfileProcedure(socialSecurityNumber));

        if (customerProfile != null) {
            return ResponseEntity.ok(customerProfile);
        }

        return ResponseEntity.notFound().build();
    }

    @ManagedOperation(description = "Create a new customer profile")
    @PostMapping(value = "/create", consumes = "application/json", produces = "application/json")
    public ResponseEntity<CustomerProfile> createCustomerProfile(
            @NonNull @RequestBody CustomerProfileRequest customerProfileRequest) {

        CustomerProfile customerProfile = runProcedure(new CreateCustomerProfileProcedure(customerProfileRequest));

        if (customerProfile != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(customerProfile);
        }

        return ResponseEntity.badRequest().build();
    }
}
