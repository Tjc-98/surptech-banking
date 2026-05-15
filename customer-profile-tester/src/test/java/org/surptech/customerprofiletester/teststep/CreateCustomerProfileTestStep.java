package org.surptech.customerprofiletester.teststep;

import io.restassured.response.Response;
import org.surptech.common.tester.teststep.TestStep;
import org.surptech.customerprofiletester.client.CustomerProfileClient;
import org.surptech.customerprofiletester.model.CustomerProfile;
import org.surptech.customerprofiletester.model.CustomerProfileRequest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test step for creating customer profile
 */
public class CreateCustomerProfileTestStep extends TestStep<CreateCustomerProfileTestStep> {
    
    private final CustomerProfileClient client;
    private CustomerProfileRequest request;
    private Integer expectedStatusCode;
    private boolean shouldVerifyCreated = false;
    
    public CreateCustomerProfileTestStep(CustomerProfileClient client) {
        this.client = client;
    }
    
    public CreateCustomerProfileTestStep withRequest(CustomerProfileRequest request) {
        this.request = request;
        return this;
    }
    
    public CreateCustomerProfileTestStep withCustomerData(String ssn, String firstName, String lastName, String address) {
        this.request = CustomerProfileRequest.builder()
                .socialSecurityNumber(ssn)
                .firstName(firstName)
                .lastName(lastName)
                .address(address)
                .build();
        return this;
    }
    
    public CreateCustomerProfileTestStep expectStatusCode(int statusCode) {
        this.expectedStatusCode = statusCode;
        return this;
    }
    
    public CreateCustomerProfileTestStep verifyCreated() {
        this.shouldVerifyCreated = true;
        return this;
    }
    
    @Override
    protected Response execute() {
        return client.createCustomerProfile(request);
    }
    
    @Override
    protected void verify() {
        assertNotNull(response, "Response should not be null");
        
        if (expectedStatusCode != null) {
            assertEquals(expectedStatusCode, response.getStatusCode(),
                    "Status code should be " + expectedStatusCode);
        }
        
        if (shouldVerifyCreated && expectedStatusCode == 201) {
            CustomerProfile profile = response.as(CustomerProfile.class);
            assertNotNull(profile, "Created customer profile should not be null");
            assertEquals(request.getSocialSecurityNumber(), profile.getSocialSecurityNumber(), "SSN should match");
            assertEquals(request.getFirstName(), profile.getFirstName(), "First name should match");
            assertEquals(request.getLastName(), profile.getLastName(), "Last name should match");
            assertEquals(request.getAddress(), profile.getAddress(), "Address should match");
        }
    }
    
    @Override
    protected String getDescription() {
        if (description != null && !description.isEmpty()) {
            return description;
        }
        return "Create customer profile for SSN: " + (request != null ? request.getSocialSecurityNumber() : "N/A");
    }
    
    @Override
    protected String getRequestDetails() {
        return "POST /customer/create with data: " + (request != null ? request.toString() : "N/A");
    }
}
