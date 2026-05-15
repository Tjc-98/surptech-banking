package org.surptech.customerprofile.controller;

import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.surptech.common.controller.BaseController;
import org.surptech.customerprofile.domain.response.CustomerProfileResponse;
import org.surptech.customerprofile.domain.request.CustomerProfileRequest;
import org.surptech.customerprofile.procedure.CreateCustomerProfileProcedure;
import org.surptech.customerprofile.procedure.GetCustomerProfileProcedure;

/**
 * REST Controller for managing customer profile resources.
 *
 * Handles HTTP requests for creating and retrieving customer profile information.
 * All endpoints produce and consume JSON with snake_case field names.
 *
 * Endpoint mappings:
 * - GET /customer-profile/customer/get - Retrieve customer profile by SSN
 * - POST /customer-profile/customer/create - Create or update customer profile
 */
@RestController
@RequestMapping("/customer")
public class CustomerProfileController extends BaseController {

    /**
     * Retrieves a customer profile by social security number.
     *
     * @param socialSecurityNumber the USA format social security number (XXX-XX-XXXX)
     * @return ResponseEntity with customer profile if found (200 OK), or 404 Not Found
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
     * If a customer with the same social security number exists, updates that profile.
     * Otherwise, creates a new customer profile.
     *
     * @param customerProfileRequest the customer profile data to create or update
     * @return ResponseEntity with created/updated customer profile and 201 Created status,
     *         or 400 Bad Request if the operation failed
     */
    @PostMapping(value = "/create", consumes = "application/json", produces = "application/json")
    public ResponseEntity<CustomerProfileResponse> createCustomerProfile(
            @NonNull @RequestBody CustomerProfileRequest customerProfileRequest) {

        // Validate request before processing
        customerProfileRequest.validate();

        CustomerProfileResponse customerProfile = executeProcedure(
                new CreateCustomerProfileProcedure(customerProfileRequest));

        return customerProfile != null
            ? ResponseEntity.status(HttpStatus.CREATED).body(customerProfile)
            : ResponseEntity.badRequest().build();
    }
}
