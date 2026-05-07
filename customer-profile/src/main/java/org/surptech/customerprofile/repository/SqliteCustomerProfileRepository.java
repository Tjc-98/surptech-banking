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

    public SqliteCustomerProfileRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        log.info("SqliteCustomerProfileRepository initialized");
    }

    private final RowMapper<CustomerProfileEntity> rowMapper = (resultSet, rowNumber) -> 
        CustomerProfileEntity.builder()
            .socialSecurityNumber(resultSet.getString("social_security_number"))
            .firstName(resultSet.getString("first_name"))
            .lastName(resultSet.getString("last_name"))
            .address(resultSet.getString("address"))
            .build();

    @Override
    public CustomerProfileEntity save(CustomerProfileEntity customerProfile) {
        log.debug("Saving customer profile with SocialSecurityNumber: {}", customerProfile.getSocialSecurityNumber());

        // Check if customer already exists by SocialSecurityNumber
        Optional<CustomerProfileEntity> existing = findBySocialSecurityNumber(customerProfile.getSocialSecurityNumber());
        
        if (existing.isPresent()) {
            log.debug("Customer profile exists, updating");
            return update(customerProfile);
        } else {
            log.debug("Customer profile does not exist, inserting");
            return insert(customerProfile);
        }
    }

    private CustomerProfileEntity insert(CustomerProfileEntity customerProfile) {
        log.info("Inserting new customer profile: {}", customerProfile.getSocialSecurityNumber());
        
        jdbcTemplate.update(
            "INSERT INTO customer_profile (social_security_number, first_name, last_name, address) VALUES (?, ?, ?, ?)",
            customerProfile.getSocialSecurityNumber(),
            customerProfile.getFirstName(),
            customerProfile.getLastName(),
            customerProfile.getAddress()
        );

        log.info("Customer profile inserted successfully");
        return customerProfile;
    }

    private CustomerProfileEntity update(CustomerProfileEntity customerProfile) {
        log.info("Updating customer profile: {}", customerProfile.getSocialSecurityNumber());
        
        jdbcTemplate.update(
            "UPDATE customer_profile SET first_name = ?, last_name = ?, address = ? WHERE social_security_number = ?",
            customerProfile.getFirstName(),
            customerProfile.getLastName(),
            customerProfile.getAddress(),
            customerProfile.getSocialSecurityNumber()
        );
        
        log.info("Customer profile updated successfully");
        return customerProfile;
    }

    @Override
    public Optional<CustomerProfileEntity> findBySocialSecurityNumber(String socialSecurityNumber) {
        log.debug("Finding customer profile by SocialSecurityNumber: {}", socialSecurityNumber);
        
        List<CustomerProfileEntity> results = jdbcTemplate.query(
            "SELECT * FROM customer_profile WHERE social_security_number = ?",
            rowMapper,
            socialSecurityNumber
        );
        
        if (results.isEmpty()) {
            log.debug("Customer profile not found for SocialSecurityNumber: {}", socialSecurityNumber);
        } else {
            log.debug("Customer profile found for SocialSecurityNumber: {}", socialSecurityNumber);
        }
        
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    @Override
    public List<CustomerProfileEntity> findAll() {
        log.info("Retrieving all customer profiles");
        List<CustomerProfileEntity> profiles = jdbcTemplate.query(
            "SELECT * FROM customer_profile",
            rowMapper
        );
        log.info("Retrieved {} customer profiles", profiles.size());
        return profiles;
    }

    @Override
    public void deleteBySocialSecurityNumber(String socialSecurityNumber) {
        log.info("Deleting customer profile with SocialSecurityNumber: {}", socialSecurityNumber);
        int rowsAffected = jdbcTemplate.update(
            "DELETE FROM customer_profile WHERE social_security_number = ?",
            socialSecurityNumber
        );
        log.info("Deleted {} customer profile(s)", rowsAffected);
    }
}
