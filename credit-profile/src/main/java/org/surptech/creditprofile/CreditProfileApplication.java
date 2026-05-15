package org.surptech.creditprofile;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the Credit Profile Service application.
 *
 * This Spring Boot application provides REST APIs for managing credit profile information
 * including retrieval and creation/update of credit profiles.
 *
 * Configuration can be found in:
 * - application.properties: Default configuration
 * - RepositoryConfiguration: Repository bean configuration
 * - ApplicationContextProvider: Spring context configuration
 *
 * Main endpoints:
 * - GET /credit-profile/credit/get: Retrieve credit profile by SSN
 * - POST /credit-profile/credit/create: Create or update credit profile
 */
@SpringBootApplication
public class CreditProfileApplication {

    /**
     * Main method to start the Spring Boot application.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(CreditProfileApplication.class, args);
    }

}
