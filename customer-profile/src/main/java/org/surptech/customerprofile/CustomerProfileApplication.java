package org.surptech.customerprofile;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the Customer Profile Service application.
 *
 * This Spring Boot application provides REST APIs for managing customer profile information
 * including retrieval and creation/update of customer profiles.
 *
 * Configuration can be found in:
 * - application.properties: Default configuration
 * - RepositoryConfiguration: Repository bean configuration
 * - ApplicationContextProvider: Spring context configuration
 *
 * Main endpoints:
 * - GET /customer-profile/customer/get: Retrieve customer profile by SSN
 * - POST /customer-profile/customer/create: Create or update customer profile
 */
@SpringBootApplication
public class CustomerProfileApplication {

    /**
     * Main method to start the Spring Boot application.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(CustomerProfileApplication.class, args);
    }

}
