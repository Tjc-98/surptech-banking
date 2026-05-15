package org.surptech.customerprofile.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.surptech.customerprofile.repository.CustomerProfileRepository;
import org.surptech.customerprofile.repository.SqliteCustomerProfileRepository;

/**
 * Configuration class for repository beans.
 *
 * This configuration class is responsible for instantiating the appropriate repository implementation
 * based on the database type specified in the application properties (database.type configuration).
 *
 * Currently supported database types:
 * - sqlite: SQLite database implementation
 *
 * Future database implementations can be added here by adding new case statements
 * and creating corresponding repository implementation classes.
 */
@Slf4j
@Configuration
public class RepositoryConfiguration {

    @Value("${database.type:sqlite}")
    private String databaseType;

    /**
     * Creates and configures the CustomerProfileRepository bean based on the configured database type.
     *
     * The repository implementation is selected based on the 'database.type' property.
     * This allows for easy switching between different database implementations without code changes.
     *
     * @param jdbcTemplate the JdbcTemplate instance to be injected into the repository
     * @return the appropriate CustomerProfileRepository implementation for the configured database type
     * @throws IllegalArgumentException if the configured database type is not supported
     */
    @Bean
    public CustomerProfileRepository customerProfileRepository(JdbcTemplate jdbcTemplate) {
        log.info("Configuring CustomerProfileRepository with database type: {}", databaseType);

        return switch (databaseType.toLowerCase()) {
            case "sqlite" -> {
                log.debug("Initializing SqliteCustomerProfileRepository");
                yield new SqliteCustomerProfileRepository(jdbcTemplate);
            }
            // Future database implementations can be added here:
            // case "postgresql" -> new PostgresqlCustomerProfileRepository(jdbcTemplate);
            // case "mysql" -> new MysqlCustomerProfileRepository(jdbcTemplate);
            default -> throw new IllegalArgumentException(
                String.format("Unsupported database type: '%s'. Supported types: sqlite", databaseType)
            );
        };
    }
}
