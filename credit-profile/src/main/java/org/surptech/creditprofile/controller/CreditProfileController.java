package org.surptech.creditprofile.controller;

import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.surptech.creditprofile.domain.response.CreditProfileResponse;
import org.surptech.creditprofile.domain.request.CreditProfileRequest;
import org.surptech.creditprofile.procedure.CreateCreditProfileProcedure;
import org.surptech.creditprofile.procedure.GetCreditProfileProcedure;

/**
 * REST Controller for managing credit profiles.
 * This controller handles HTTP requests for creating and retrieving credit profile information.
 */
@RestController
@RequestMapping("/credit")
public class CreditProfileController extends BaseController {

    /**
     * Retrieves a credit profile by social security number.
     *
     * @param socialSecurityNumber the social security number of the customer to retrieve
     * @return a ResponseEntity containing the credit profile if found, or 404 Not Found
     */
    @GetMapping(value = "/get", produces = "application/json")
    public ResponseEntity<CreditProfileResponse> getCreditProfile(
            @NonNull @RequestParam("socialSecurityNumber") String socialSecurityNumber) {

        CreditProfileResponse creditProfile = executeProcedure(
                new GetCreditProfileProcedure(socialSecurityNumber));

        return creditProfile != null
            ? ResponseEntity.ok(creditProfile)
            : ResponseEntity.notFound().build();
    }

    /**
     * Creates a new credit profile or updates an existing one.
     *
     * @param creditProfileRequest the credit profile data to create or update
     * @return a ResponseEntity containing the created/updated credit profile with HTTP 201 Created status,
     *         or 400 Bad Request if the operation failed
     */
    @PostMapping(value = "/create", consumes = "application/json", produces = "application/json")
    public ResponseEntity<CreditProfileResponse> createCreditProfile(
            @NonNull @RequestBody CreditProfileRequest creditProfileRequest) {

        // Validate request
        creditProfileRequest.validate();

        CreditProfileResponse creditProfile = executeProcedure(
                new CreateCreditProfileProcedure(creditProfileRequest));

        return creditProfile != null
            ? ResponseEntity.status(HttpStatus.CREATED).body(creditProfile)
            : ResponseEntity.badRequest().build();
    }
}
