package org.surptech.creditprofile.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.surptech.creditprofile.domain.entity.CreditProfileEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * SQLite implementation of the CreditProfileRepository interface.
 * This class handles all SQLite-specific database operations for credit profiles.
 */
@Slf4j
public class SqliteCreditProfileRepository implements CreditProfileRepository {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<CreditProfileEntity> creditProfileRowMapper;

    /**
     * Constructs a SqliteCreditProfileRepository with the provided JdbcTemplate.
     *
     * @param jdbcTemplate the JdbcTemplate for database operations
     */
    public SqliteCreditProfileRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.creditProfileRowMapper = this::mapRowToCreditProfileEntity;
        log.info("SqliteCreditProfileRepository initialized successfully");
    }

    /**
     * Maps a result set row to a CreditProfileEntity.
     *
     * @param resultSet the result set to map
     * @param rowNumber the row number
     * @return the mapped CreditProfileEntity
     */
    private CreditProfileEntity mapRowToCreditProfileEntity(java.sql.ResultSet resultSet, int rowNumber)
            throws java.sql.SQLException {
        return CreditProfileEntity.builder()
                .socialSecurityNumber(resultSet.getString("social_security_number"))
                .fullCreditBalance(BigDecimal.valueOf(resultSet.getDouble("full_credit_balance")))
                .spendBalance(BigDecimal.valueOf(resultSet.getDouble("spend_balance")))
                .interestRate(BigDecimal.valueOf(resultSet.getDouble("interest_rate")))
                .build();
    }

    @Override
    public CreditProfileEntity save(CreditProfileEntity creditProfile) {
        if (creditProfile == null) {
            log.warn("Attempted to save null credit profile");
            throw new IllegalArgumentException("Credit profile cannot be null");
        }

        log.debug("Saving credit profile with social security number: {}",
                  creditProfile.getSocialSecurityNumber());

        // Check if credit profile already exists by social security number
        Optional<CreditProfileEntity> existingProfile =
                findBySocialSecurityNumber(creditProfile.getSocialSecurityNumber());

        if (existingProfile.isPresent()) {
            log.debug("Credit profile exists, updating");
            return updateCreditProfile(creditProfile);
        } else {
            log.debug("Credit profile does not exist, inserting new record");
            return insertCreditProfile(creditProfile);
        }
    }

    /**
     * Inserts a new credit profile into the database.
     *
     * @param creditProfile the credit profile entity to insert
     * @return the inserted credit profile entity
     */
    private CreditProfileEntity insertCreditProfile(CreditProfileEntity creditProfile) {
        log.info("Inserting new credit profile with social security number: {}",
                 creditProfile.getSocialSecurityNumber());

        int rowsAffected = jdbcTemplate.update(
            "INSERT INTO credit_profile (social_security_number, full_credit_balance, spend_balance, interest_rate) VALUES (?, ?, ?, ?)",
            creditProfile.getSocialSecurityNumber(),
            creditProfile.getFullCreditBalance(),
            creditProfile.getSpendBalance(),
            creditProfile.getInterestRate()
        );

        if (rowsAffected > 0) {
            log.info("Credit profile inserted successfully");
        } else {
            log.warn("Credit profile insertion returned 0 rows affected");
        }

        return creditProfile;
    }

    /**
     * Updates an existing credit profile in the database.
     *
     * @param creditProfile the credit profile entity to update
     * @return the updated credit profile entity
     */
    private CreditProfileEntity updateCreditProfile(CreditProfileEntity creditProfile) {
        log.info("Updating credit profile with social security number: {}",
                 creditProfile.getSocialSecurityNumber());

        int rowsAffected = jdbcTemplate.update(
            "UPDATE credit_profile SET full_credit_balance = ?, spend_balance = ?, interest_rate = ? WHERE social_security_number = ?",
            creditProfile.getFullCreditBalance(),
            creditProfile.getSpendBalance(),
            creditProfile.getInterestRate(),
            creditProfile.getSocialSecurityNumber()
        );
        
        if (rowsAffected > 0) {
            log.info("Credit profile updated successfully");
        } else {
            log.warn("Credit profile update returned 0 rows affected");
        }

        return creditProfile;
    }

    @Override
    public Optional<CreditProfileEntity> findBySocialSecurityNumber(String socialSecurityNumber) {
        if (socialSecurityNumber == null || socialSecurityNumber.trim().isEmpty()) {
            log.warn("Attempted to find credit profile with null or empty social security number");
            return Optional.empty();
        }

        log.debug("Finding credit profile by social security number: {}", socialSecurityNumber);

        List<CreditProfileEntity> results = jdbcTemplate.query(
            "SELECT * FROM credit_profile WHERE social_security_number = ?",
            creditProfileRowMapper,
            socialSecurityNumber
        );
        
        if (results.isEmpty()) {
            log.debug("No credit profile found for social security number: {}", socialSecurityNumber);
        } else if (results.size() == 1) {
            log.debug("Credit profile found for social security number: {}", socialSecurityNumber);
        } else {
            log.warn("Multiple credit profiles found for social security number: {} (found: {})",
                     socialSecurityNumber, results.size());
        }
        
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    @Override
    public List<CreditProfileEntity> findAll() {
        log.info("Retrieving all credit profiles");
        List<CreditProfileEntity> profiles = jdbcTemplate.query(
            "SELECT * FROM credit_profile",
            creditProfileRowMapper
        );
        log.info("Retrieved {} credit profile(s)", profiles.size());
        return profiles;
    }

    @Override
    public void deleteBySocialSecurityNumber(String socialSecurityNumber) {
        if (socialSecurityNumber == null || socialSecurityNumber.trim().isEmpty()) {
            log.warn("Attempted to delete credit profile with null or empty social security number");
            throw new IllegalArgumentException("Social security number cannot be null or empty");
        }

        log.info("Deleting credit profile with social security number: {}", socialSecurityNumber);
        int rowsAffected = jdbcTemplate.update(
            "DELETE FROM credit_profile WHERE social_security_number = ?",
            socialSecurityNumber
        );

        if (rowsAffected > 0) {
            log.info("Successfully deleted {} credit profile(s)", rowsAffected);
        } else {
            log.warn("No credit profile found to delete for social security number: {}", socialSecurityNumber);
        }
    }
}
