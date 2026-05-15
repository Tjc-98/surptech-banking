package org.surptech.customerprofiletester.teststep;

import io.restassured.response.Response;
import org.surptech.common.tester.teststep.TestStep;
import org.surptech.customerprofiletester.client.CustomerProfileClient;
import org.surptech.customerprofiletester.model.CustomerProfile;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test step for getting customer profile
 */
public class GetCustomerProfileTestStep extends TestStep<GetCustomerProfileTestStep> {
    
    private final CustomerProfileClient client;
    private String ssn;
    private Integer expectedStatusCode;
    private Long maxResponseTime;
    private String expectedFirstName;
    private String expectedLastName;
    private String expectedAddress;
    private boolean shouldVerifyNotNull = false;
    
    public GetCustomerProfileTestStep(CustomerProfileClient client) {
        this.client = client;
    }
    
    public GetCustomerProfileTestStep withSsn(String ssn) {
        this.ssn = ssn;
        return this;
    }
    
    public GetCustomerProfileTestStep expectStatusCode(int statusCode) {
        this.expectedStatusCode = statusCode;
        return this;
    }
    
    public GetCustomerProfileTestStep expectResponseTimeUnder(long milliseconds) {
        this.maxResponseTime = milliseconds;
        return this;
    }
    
    public GetCustomerProfileTestStep verifyResponseNotNull() {
        this.shouldVerifyNotNull = true;
        return this;
    }
    
    public GetCustomerProfileTestStep verifyCustomerProfile(String ssn, String firstName, String lastName, String address) {
        this.expectedFirstName = firstName;
        this.expectedLastName = lastName;
        this.expectedAddress = address;
        return this;
    }
    
    @Override
    protected Response execute() {
        return client.getCustomerProfile(ssn);
    }
    
    @Override
    protected void verify() {
        assertNotNull(response, "Response should not be null");
        
        if (expectedStatusCode != null) {
            assertEquals(expectedStatusCode, response.getStatusCode(),
                    "Status code should be " + expectedStatusCode);
        }
        
        if (maxResponseTime != null) {
            assertTrue(response.getTime() < maxResponseTime,
                    "Response time should be under " + maxResponseTime + "ms, but was " + response.getTime() + "ms");
        }
        
        if (shouldVerifyNotNull && expectedStatusCode == 200) {
            CustomerProfile profile = response.as(CustomerProfile.class);
            assertNotNull(profile, "Customer profile should not be null");
            assertNotNull(profile.getSocialSecurityNumber(), "SSN should not be null");
        }
        
        if (expectedFirstName != null && expectedStatusCode == 200) {
            CustomerProfile profile = response.as(CustomerProfile.class);
            assertEquals(expectedFirstName, profile.getFirstName(), "First name should match");
            assertEquals(expectedLastName, profile.getLastName(), "Last name should match");
            assertEquals(expectedAddress, profile.getAddress(), "Address should match");
            assertEquals(ssn, profile.getSocialSecurityNumber(), "SSN should match");
        }
    }
    
    @Override
    protected String getDescription() {
        if (description != null && !description.isEmpty()) {
            return description;
        }
        return "Get customer profile for SSN: " + ssn;
    }
    
    @Override
    protected String getRequestDetails() {
        return "GET /customer/get?socialSecurityNumber=" + ssn;
    }
}
