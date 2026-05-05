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
 * This class instantiates the appropriate repository implementation based on the database type
 * specified in application properties.
 */
@Slf4j
@Configuration
public class RepositoryConfiguration {

    @Value("${database.type:sqlite}")
    private String databaseType;

    /**
     * Creates and configures the CustomerProfileRepository bean based on the database type.
     *
     * @param jdbcTemplate the JdbcTemplate to use for database operations
     * @return the appropriate CustomerProfileRepository implementation
     * @throws IllegalArgumentException if the database type is not supported
     */
    @Bean
    public CustomerProfileRepository customerProfileRepository(JdbcTemplate jdbcTemplate) {
        log.info("Configuring CustomerProfileRepository with database type: {}", databaseType);

        CustomerProfileRepository repository = switch (databaseType.toLowerCase()) {
            case "sqlite" -> {
                log.info("Initializing SqliteCustomerProfileRepository");
                yield new SqliteCustomerProfileRepository(jdbcTemplate);
            }
            // Future database implementations can be added here:
            // case "postgresql" -> new PostgresCustomerProfileRepository(jdbcTemplate);
            // case "mysql" -> new MysqlCustomerProfileRepository(jdbcTemplate);
            default -> {
                log.error("Unsupported database type: {}. Supported types: sqlite", databaseType);
                throw new IllegalArgumentException(
                        "Unsupported database type: " + databaseType + ". Supported types: sqlite"
                );
            }
        };

        log.info("CustomerProfileRepository configured successfully");
        return repository;
    }
}
