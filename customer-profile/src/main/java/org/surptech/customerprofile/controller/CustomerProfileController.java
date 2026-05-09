package org.surptech.customerprofile.controller;

import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.surptech.customerprofile.domain.response.CustomerProfileResponse;
import org.surptech.customerprofile.domain.request.CustomerProfileRequest;
import org.surptech.customerprofile.procedure.CreateCustomerProfileProcedure;
import org.surptech.customerprofile.procedure.GetCustomerProfileProcedure;

/**
 * REST Controller for managing customer profiles.
 * This controller handles HTTP requests for creating and retrieving customer profile information.
 */
@RestController
@RequestMapping("/customer")
public class CustomerProfileController extends BaseController {

    /**
     * Retrieves a customer profile by social security number.
     *
     * @param socialSecurityNumber the social security number of the customer to retrieve
     * @return a ResponseEntity containing the customer profile if found, or 404 Not Found
     */
    @GetMapping(value = "/get", produces = "application/json")
    public ResponseEntity<CustomerProfileResponse> getCustomerProfile(
            @NonNull @RequestParam("socialSecurityNumber") String socialSecurityNumber) {

        CustomerProfileResponse customerProfile = executeProcedure(
                new GetCustomerProfileProcedure(socialSecurityNumber));

        return customerProfile != null
            ? ResponseEntity.ok(customerProfile)
            : ResponseEntity.notFound().build();
    }

    /**
     * Creates a new customer profile or updates an existing one.
     *
     * @param customerProfileRequest the customer profile data to create or update
     * @return a ResponseEntity containing the created/updated customer profile with HTTP 201 Created status,
     *         or 400 Bad Request if the operation failed
     */
    @PostMapping(value = "/create", consumes = "application/json", produces = "application/json")
    public ResponseEntity<CustomerProfileResponse> createCustomerProfile(
            @NonNull @RequestBody CustomerProfileRequest customerProfileRequest) {

        // Validate request
        customerProfileRequest.validate();

        CustomerProfileResponse customerProfile = executeProcedure(
                new CreateCustomerProfileProcedure(customerProfileRequest));

        return customerProfile != null
            ? ResponseEntity.status(HttpStatus.CREATED).body(customerProfile)
            : ResponseEntity.badRequest().build();
    }
}
