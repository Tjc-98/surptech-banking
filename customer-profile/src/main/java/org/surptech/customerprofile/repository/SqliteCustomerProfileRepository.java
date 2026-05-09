package org.surptech.customerprofile.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.surptech.customerprofile.domain.entity.CustomerProfileEntity;

import java.util.List;
import java.util.Optional;

/**
 * SQLite implementation of the CustomerProfileRepository interface.
 * This class handles all SQLite-specific database operations for customer profiles.
 */
@Slf4j
public class SqliteCustomerProfileRepository implements CustomerProfileRepository {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<CustomerProfileEntity> customerProfileRowMapper;

    /**
     * Constructs a SqliteCustomerProfileRepository with the provided JdbcTemplate.
     *
     * @param jdbcTemplate the JdbcTemplate for database operations
     */
    public SqliteCustomerProfileRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.customerProfileRowMapper = this::mapRowToCustomerProfileEntity;
        log.info("SqliteCustomerProfileRepository initialized successfully");
    }

    /**
     * Maps a result set row to a CustomerProfileEntity.
     *
     * @param resultSet the result set to map
     * @param rowNumber the row number
     * @return the mapped CustomerProfileEntity
     */
    private CustomerProfileEntity mapRowToCustomerProfileEntity(java.sql.ResultSet resultSet, int rowNumber)
            throws java.sql.SQLException {
        return CustomerProfileEntity.builder()
                .socialSecurityNumber(resultSet.getString("social_security_number"))
                .firstName(resultSet.getString("first_name"))
                .lastName(resultSet.getString("last_name"))
                .address(resultSet.getString("address"))
                .build();
    }

    @Override
    public CustomerProfileEntity save(CustomerProfileEntity customerProfile) {
        if (customerProfile == null) {
            log.warn("Attempted to save null customer profile");
            throw new IllegalArgumentException("Customer profile cannot be null");
        }

        log.debug("Saving customer profile with social security number: {}",
                  customerProfile.getSocialSecurityNumber());

        // Check if customer already exists by social security number
        Optional<CustomerProfileEntity> existingProfile =
                findBySocialSecurityNumber(customerProfile.getSocialSecurityNumber());

        if (existingProfile.isPresent()) {
            log.debug("Customer profile exists, updating");
            return updateCustomerProfile(customerProfile);
        } else {
            log.debug("Customer profile does not exist, inserting new record");
            return insertCustomerProfile(customerProfile);
        }
    }

    /**
     * Inserts a new customer profile into the database.
     *
     * @param customerProfile the customer profile entity to insert
     * @return the inserted customer profile entity
     */
    private CustomerProfileEntity insertCustomerProfile(CustomerProfileEntity customerProfile) {
        log.info("Inserting new customer profile with social security number: {}",
                 customerProfile.getSocialSecurityNumber());

        int rowsAffected = jdbcTemplate.update(
            "INSERT INTO customer_profile (social_security_number, first_name, last_name, address) VALUES (?, ?, ?, ?)",
            customerProfile.getSocialSecurityNumber(),
            customerProfile.getFirstName(),
            customerProfile.getLastName(),
            customerProfile.getAddress()
        );

        if (rowsAffected > 0) {
            log.info("Customer profile inserted successfully");
        } else {
            log.warn("Customer profile insertion returned 0 rows affected");
        }

        return customerProfile;
    }

    /**
     * Updates an existing customer profile in the database.
     *
     * @param customerProfile the customer profile entity to update
     * @return the updated customer profile entity
     */
    private CustomerProfileEntity updateCustomerProfile(CustomerProfileEntity customerProfile) {
        log.info("Updating customer profile with social security number: {}",
                 customerProfile.getSocialSecurityNumber());

        int rowsAffected = jdbcTemplate.update(
            "UPDATE customer_profile SET first_name = ?, last_name = ?, address = ? WHERE social_security_number = ?",
            customerProfile.getFirstName(),
            customerProfile.getLastName(),
            customerProfile.getAddress(),
            customerProfile.getSocialSecurityNumber()
        );
        
        if (rowsAffected > 0) {
            log.info("Customer profile updated successfully");
        } else {
            log.warn("Customer profile update returned 0 rows affected");
        }

        return customerProfile;
    }

    @Override
    public Optional<CustomerProfileEntity> findBySocialSecurityNumber(String socialSecurityNumber) {
        if (socialSecurityNumber == null || socialSecurityNumber.trim().isEmpty()) {
            log.warn("Attempted to find customer profile with null or empty social security number");
            return Optional.empty();
        }

        log.debug("Finding customer profile by social security number: {}", socialSecurityNumber);

        List<CustomerProfileEntity> results = jdbcTemplate.query(
            "SELECT * FROM customer_profile WHERE social_security_number = ?",
            customerProfileRowMapper,
            socialSecurityNumber
        );
        
        if (results.isEmpty()) {
            log.debug("No customer profile found for social security number: {}", socialSecurityNumber);
        } else if (results.size() == 1) {
            log.debug("Customer profile found for social security number: {}", socialSecurityNumber);
        } else {
            log.warn("Multiple customer profiles found for social security number: {} (found: {})",
                     socialSecurityNumber, results.size());
        }
        
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    @Override
    public List<CustomerProfileEntity> findAll() {
        log.info("Retrieving all customer profiles");
        List<CustomerProfileEntity> profiles = jdbcTemplate.query(
            "SELECT * FROM customer_profile",
            customerProfileRowMapper
        );
        log.info("Retrieved {} customer profile(s)", profiles.size());
        return profiles;
    }

    @Override
    public void deleteBySocialSecurityNumber(String socialSecurityNumber) {
        if (socialSecurityNumber == null || socialSecurityNumber.trim().isEmpty()) {
            log.warn("Attempted to delete customer profile with null or empty social security number");
            throw new IllegalArgumentException("Social security number cannot be null or empty");
        }

        log.info("Deleting customer profile with social security number: {}", socialSecurityNumber);
        int rowsAffected = jdbcTemplate.update(
            "DELETE FROM customer_profile WHERE social_security_number = ?",
            socialSecurityNumber
        );

        if (rowsAffected > 0) {
            log.info("Successfully deleted {} customer profile(s)", rowsAffected);
        } else {
            log.warn("No customer profile found to delete for social security number: {}", socialSecurityNumber);
        }
    }
}
