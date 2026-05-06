package org.surptech.bankingtester.client;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.surptech.bankingtester.config.TestConfiguration;

import static io.restassured.RestAssured.given;

/**
 * Client for interacting with Data Aggregator service
 */
@Slf4j
public class DataAggregatorClient {
    
    private final String baseUrl;
    private final int timeout;
    
    public DataAggregatorClient() {
        TestConfiguration config = TestConfiguration.getInstance();
        this.baseUrl = config.getDataAggregatorBaseUrl();
        this.timeout = config.getDataAggregatorTimeout();
        
        RestAssured.baseURI = baseUrl;
        log.info("DataAggregatorClient initialized with base URL: {}", baseUrl);
    }
    
    /**
     * Get customer banking information (combined customer + credit info)
     */
    public Response getCustomerBankingInfo(String socialSecurityNumber) {
        log.info("Requesting customer banking info for SSN: {}", socialSecurityNumber);
        
        return given()
                .queryParam("socialSecurityNumber", socialSecurityNumber)
            .when()
                .get("/customer/info")
            .then()
                .extract()
                .response();
    }
    
    /**
     * Health check for data aggregator service
     */
    public Response healthCheck() {
        log.info("Performing health check on data aggregator");
        
        return given()
            .when()
                .get("/management/health")
            .then()
                .extract()
                .response();
    }
}
