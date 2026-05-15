package org.surptech.creditprofile.repository;

import org.surptech.creditprofile.domain.entity.CreditProfileEntity;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing credit profile persistence operations.
 *
 * This interface defines the contract for database operations on credit profiles.
 * Implementations of this interface provide database-specific implementations using
 * their respective database drivers and query languages.
 *
 * All implementations should follow transactional semantics and handle errors appropriately.
 */
public interface CreditProfileRepository {

    /**
     * Saves a credit profile entity to the database.
     *
     * If a credit profile with the same social security number already exists,
     * the existing profile will be updated with the new values.
     * Otherwise, a new profile will be inserted.
     *
     * @param creditProfile the credit profile entity to save or update
     * @return the saved credit profile entity
     * @throws IllegalArgumentException if the credit profile is null
     * @throws java.sql.SQLException if a database error occurs
     */
    CreditProfileEntity save(CreditProfileEntity creditProfile);

    /**
     * Finds and retrieves a credit profile by social security number.
     *
     * @param socialSecurityNumber the social security number to search for
     * @return an Optional containing the credit profile entity if found,
     *         empty Optional if not found
     * @throws IllegalArgumentException if social security number is null or empty
     * @throws java.sql.SQLException if a database error occurs
     */
    Optional<CreditProfileEntity> findBySocialSecurityNumber(String socialSecurityNumber);

    /**
     * Retrieves all credit profiles from the database.
     *
     * @return a list of all credit profile entities; returns an empty list if no profiles exist
     * @throws java.sql.SQLException if a database error occurs
     */
    List<CreditProfileEntity> findAll();

    /**
     * Deletes a credit profile by social security number.
     * 
     * @param socialSecurityNumber the social security number of the profile to delete
     * @throws IllegalArgumentException if social security number is null or empty
     * @throws java.sql.SQLException if a database error occurs
     */
    void deleteBySocialSecurityNumber(String socialSecurityNumber);
}
