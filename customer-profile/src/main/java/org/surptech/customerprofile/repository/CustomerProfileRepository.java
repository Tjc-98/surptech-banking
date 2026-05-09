package org.surptech.customerprofile.repository;

import org.surptech.customerprofile.domain.entity.CustomerProfileEntity;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing customer profile persistence operations.
 *
 * This interface defines the contract for database operations on customer profiles.
 * Implementations of this interface provide database-specific implementations using
 * their respective database drivers and query languages.
 *
 * All implementations should follow transactional semantics and handle errors appropriately.
 */
public interface CustomerProfileRepository {

    /**
     * Saves a customer profile entity to the database.
     *
     * If a customer profile with the same social security number already exists,
     * the existing profile will be updated with the new values.
     * Otherwise, a new profile will be inserted.
     *
     * @param customerProfile the customer profile entity to save or update
     * @return the saved customer profile entity
     * @throws IllegalArgumentException if the customer profile is null
     * @throws java.sql.SQLException if a database error occurs
     */
    CustomerProfileEntity save(CustomerProfileEntity customerProfile);

    /**
     * Finds and retrieves a customer profile by social security number.
     *
     * @param socialSecurityNumber the social security number to search for
     * @return an Optional containing the customer profile entity if found,
     *         empty Optional if not found
     * @throws IllegalArgumentException if social security number is null or empty
     * @throws java.sql.SQLException if a database error occurs
     */
    Optional<CustomerProfileEntity> findBySocialSecurityNumber(String socialSecurityNumber);

    /**
     * Retrieves all customer profiles from the database.
     *
     * @return a list of all customer profile entities; returns an empty list if no profiles exist
     * @throws java.sql.SQLException if a database error occurs
     */
    List<CustomerProfileEntity> findAll();

    /**
     * Deletes a customer profile by social security number.
     * 
     * @param socialSecurityNumber the social security number of the profile to delete
     * @throws IllegalArgumentException if social security number is null or empty
     * @throws java.sql.SQLException if a database error occurs
     */
    void deleteBySocialSecurityNumber(String socialSecurityNumber);
}

