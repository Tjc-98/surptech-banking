package org.surptech.bankingtester.teststep;

import io.restassured.response.Response;
import org.surptech.bankingtester.client.DataAggregatorClient;
import org.surptech.bankingtester.model.CustomerBankingInfo;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test step for retrieving and verifying customer banking information
 */
public class GetCustomerBankingInfoTestStep extends TestStep<GetCustomerBankingInfoTestStep> {
    
    private final DataAggregatorClient client;
    private String ssn;
    private Integer expectedStatusCode = 200;
    private String expectedSsn;
    private String expectedFirstName;
    private String expectedLastName;
    private String expectedAddress;
    private Boolean verifyCustomerProfile = false;
    private Boolean verifyCreditProfile = false;
    private Boolean verifyNotNull = false;
    private Long maxResponseTimeMs;
    
    public GetCustomerBankingInfoTestStep(DataAggregatorClient client) {
        this.client = client;
    }
    
    /**
     * Set the SSN to query
     * 
     * @param ssn Social Security Number
     * @return This test step for chaining
     */
    public GetCustomerBankingInfoTestStep withSsn(String ssn) {
        this.ssn = ssn;
        return this;
    }
    
    /**
     * Set the expected HTTP status code
     * 
     * @param statusCode Expected status code
     * @return This test step for chaining
     */
    public GetCustomerBankingInfoTestStep expectStatusCode(int statusCode) {
        this.expectedStatusCode = statusCode;
        return this;
    }
    
    /**
     * Verify customer profile data matches expected values
     * 
     * @param ssn Expected SSN
     * @param firstName Expected first name
     * @param lastName Expected last name
     * @param address Expected address
     * @return This test step for chaining
     */
    public GetCustomerBankingInfoTestStep verifyCustomerProfile(String ssn, String firstName, String lastName, String address) {
        this.expectedSsn = ssn;
        this.expectedFirstName = firstName;
        this.expectedLastName = lastName;
        this.expectedAddress = address;
        this.verifyCustomerProfile = true;
        return this;
    }
    
    /**
     * Verify credit profile data exists and is not null
     * 
     * @return This test step for chaining
     */
    public GetCustomerBankingInfoTestStep verifyCreditProfileExists() {
        this.verifyCreditProfile = true;
        return this;
    }
    
    /**
     * Verify response body can be parsed and is not null
     * 
     * @return This test step for chaining
     */
    public GetCustomerBankingInfoTestStep verifyResponseNotNull() {
        this.verifyNotNull = true;
        return this;
    }
    
    /**
     * Set the maximum acceptable response time
     * 
     * @param maxResponseTimeMs Maximum response time in milliseconds
     * @return This test step for chaining
     */
    public GetCustomerBankingInfoTestStep expectResponseTimeUnder(long maxResponseTimeMs) {
        this.maxResponseTimeMs = maxResponseTimeMs;
        return this;
    }
    
    @Override
    protected Response execute() {
        assertNotNull(ssn, "SSN must be set before executing");
        return client.getCustomerBankingInfo(ssn);
    }
    
    @Override
    protected void verify() {
        assertNotNull(response, "Response should not be null");
        
        // Verify status code
        if (expectedStatusCode != null) {
            assertEquals(expectedStatusCode, response.getStatusCode(),
                    String.format("Expected status code %d but got %d", expectedStatusCode, response.getStatusCode()));
        }
        
        // Verify response time if specified
        if (maxResponseTimeMs != null) {
            long actualResponseTime = response.getTime();
            assertTrue(actualResponseTime < maxResponseTimeMs,
                    String.format("Response time (%d ms) should be less than %d ms", 
                            actualResponseTime, maxResponseTimeMs));
        }
        
        // Only parse body if status code is 200
        if (response.getStatusCode() == 200) {
            CustomerBankingInfo bankingInfo = response.as(CustomerBankingInfo.class);
            
            // Verify not null if requested
            if (verifyNotNull) {
                assertNotNull(bankingInfo, "Banking info should not be null");
            }
            
            // Verify customer profile if requested
            if (verifyCustomerProfile) {
                assertNotNull(bankingInfo, "Banking info should not be null for profile verification");
                assertEquals(expectedSsn, bankingInfo.getSocialSecurityNumber(), "SSN should match");
                assertEquals(expectedFirstName, bankingInfo.getFirstName(), "First name should match");
                assertEquals(expectedLastName, bankingInfo.getLastName(), "Last name should match");
                assertEquals(expectedAddress, bankingInfo.getAddress(), "Address should match");
            }
            
            // Verify credit profile if requested
            if (verifyCreditProfile) {
                assertNotNull(bankingInfo, "Banking info should not be null for credit verification");
                assertNotNull(bankingInfo.getCurrentBalance(), "Current balance should not be null");
                assertNotNull(bankingInfo.getSpendBalance(), "Spend balance should not be null");
                assertNotNull(bankingInfo.getInterestRate(), "Interest rate should not be null");
            }
        }
    }
    
    @Override
    protected String getDescription() {
        if (description != null) {
            return description;
        }
        return String.format("Get customer banking info for SSN: %s", ssn != null ? ssn : "N/A");
    }
    
    @Override
    protected String getRequestDetails() {
        return String.format("POST /customer/info - Body: {\"social_security_number\": \"%s\"}", 
                ssn != null ? ssn : "null");
    }
    
    /**
     * Get the parsed CustomerBankingInfo from the response
     * 
     * @return CustomerBankingInfo object or null if response was not 200
     */
    public CustomerBankingInfo getBankingInfo() {
        if (response != null && response.getStatusCode() == 200) {
            return response.as(CustomerBankingInfo.class);
        }
        return null;
    }
}
