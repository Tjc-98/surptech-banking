package org.surptech.customerprofile.controller;

import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.surptech.customerprofile.domain.response.CustomerProfileResponse;
import org.surptech.customerprofile.domain.request.CustomerProfileRequest;
import org.surptech.customerprofile.procedure.CreateCustomerProfileProcedure;
import org.surptech.customerprofile.procedure.GetCustomerProfileProcedure;

@RestController
@RequestMapping("/customer")
public class CustomerProfileController extends BaseController {

    @GetMapping(value = "/get", produces = "application/json")
    public ResponseEntity<CustomerProfileResponse> getCustomerProfile(
            @NonNull @RequestParam("socialSecurityNumber") String socialSecurityNumber) {

        CustomerProfileResponse customerProfile = executeProcedure(
                new GetCustomerProfileProcedure(socialSecurityNumber));

        if (customerProfile != null) {
            return ResponseEntity.ok(customerProfile);
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping(value = "/create", consumes = "application/json", produces = "application/json")
    public ResponseEntity<CustomerProfileResponse> createCustomerProfile(
            @NonNull @RequestBody CustomerProfileRequest customerProfileRequest) {

        CustomerProfileResponse customerProfile = executeProcedure(
                new CreateCustomerProfileProcedure(customerProfileRequest));

        if (customerProfile != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(customerProfile);
        }

        return ResponseEntity.badRequest().build();
    }
}
