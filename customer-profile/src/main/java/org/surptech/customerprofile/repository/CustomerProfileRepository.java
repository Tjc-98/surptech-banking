package org.surptech.customerprofile.repository;

import org.surptech.customerprofile.domain.CustomerProfile;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing customer profile data.
 * Implementations of this interface provide database-specific operations.
 */
public interface CustomerProfileRepository {

    /**
     * Saves a customer profile. If the profile already exists, it will be updated.
     * 
     * @param customerProfile the customer profile to save
     * @return the saved customer profile
     */
    CustomerProfile save(CustomerProfile customerProfile);

    /**
     * Finds a customer profile by social security number.
     * 
     * @param socialSecurityNumber the social security number to search for
     * @return an Optional containing the customer profile if found, empty otherwise
     */
    Optional<CustomerProfile> findBySocialSecurityNumber(String socialSecurityNumber);

    /**
     * Retrieves all customer profiles.
     * 
     * @return a list of all customer profiles
     */
    List<CustomerProfile> findAll();

    /**
     * Deletes a customer profile by social security number.
     * 
     * @param socialSecurityNumber the social security number of the profile to delete
     */
    void deleteBySocialSecurityNumber(String socialSecurityNumber);
}

