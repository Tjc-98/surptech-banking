package org.surptech.customerprofiletester.client;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.surptech.customerprofiletester.config.TestConfiguration;
import org.surptech.customerprofiletester.model.CustomerProfileRequest;

import static io.restassured.RestAssured.given;

/**
 * Client for interacting with Customer Profile service
 */
@Slf4j
public class CustomerProfileClient {
    
    private final String baseUrl;
    private final int timeout;
    
    public CustomerProfileClient() {
        TestConfiguration config = TestConfiguration.getInstance();
        this.baseUrl = config.getCustomerProfileBaseUrl();
        this.timeout = config.getCustomerProfileTimeout();
        
        RestAssured.baseURI = baseUrl;
        log.info("CustomerProfileClient initialized with base URL: {}", baseUrl);
    }
    
    /**
     * Get customer profile by SSN
     */
    public Response getCustomerProfile(String socialSecurityNumber) {
        log.info("Requesting customer profile for SSN: {}", socialSecurityNumber);
        
        return given()
                .queryParam("socialSecurityNumber", socialSecurityNumber)
            .when()
                .get("/customer/get")
            .then()
                .extract()
                .response();
    }
    
    /**
     * Create a new customer profile
     */
    public Response createCustomerProfile(CustomerProfileRequest request) {
        log.info("Creating customer profile for SSN: {}", request.getSocialSecurityNumber());
        
        return given()
                .contentType(ContentType.JSON)
                .body(request)
            .when()
                .post("/customer/create")
            .then()
                .extract()
                .response();
    }
    
    /**
     * Health check for customer profile service
     */
    public Response healthCheck() {
        log.info("Performing health check on customer profile service");
        
        return given()
            .when()
                .get("/management/health")
            .then()
                .extract()
                .response();
    }
}
