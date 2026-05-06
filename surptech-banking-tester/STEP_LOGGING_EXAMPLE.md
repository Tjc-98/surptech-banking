# Dynamic Step Logging - Usage Example

## How to Use

The `TestLifecycleManager` provides a `logStep()` method that automatically numbers each step within a test case.

### Example Test Case:

```java
@Test
@TestId("TC-001")
@DisplayName("TC-001: Verify successful retrieval of customer banking information")
public void testGetCustomerBankingInfo_ValidSSN_Success() {
    // Step 1
    logStep("Arrange - Prepare test data");
    String ssn = config.getValidSsn();
    log.info("  Using SSN: {}", ssn);
    
    // Step 2
    logStep("Act - Send POST request to /customer/info");
    Response response = dataAggregatorClient.getCustomerBankingInfo(ssn);
    
    // Step 3
    logStep("Assert - Validate response status code");
    assertEquals(200, response.getStatusCode());
    
    // Step 4
    logStep("Assert - Parse and validate response body");
    CustomerBankingInfo bankingInfo = response.as(CustomerBankingInfo.class);
    assertNotNull(bankingInfo);
    
    // Step 5
    logStep("Assert - Validate customer profile data", 
            "Expected SSN: " + config.getExpectedSsn(),
            "Actual SSN: " + bankingInfo.getSocialSecurityNumber());
    assertEquals(config.getExpectedSsn(), bankingInfo.getSocialSecurityNumber());
    
    // Step 6
    logStep("Assert - Validate credit profile data");
    assertNotNull(bankingInfo.getCurrentBalance());
    assertNotNull(bankingInfo.getSpendBalance());
}
```

### Output Example:

```
--------------------------------------------------------------------------------
Starting Test: [TC-001] CustomerBankingInfoTest.testGetCustomerBankingInfo_ValidSSN_Success
--------------------------------------------------------------------------------
Step 1: Arrange - Prepare test data
  Using SSN: 123-45-6789
Step 2: Act - Send POST request to /customer/info
Step 3: Assert - Validate response status code
Step 4: Assert - Parse and validate response body
Step 5: Assert - Validate customer profile data
  Expected SSN: 123-45-6789
  Actual SSN: 123-45-6789
Step 6: Assert - Validate credit profile data
--------------------------------------------------------------------------------
Completed Test: [TC-001] CustomerBankingInfoTest.testGetCustomerBankingInfo_ValidSSN_Success
--------------------------------------------------------------------------------
```

## Key Features:

1. **Automatic Numbering**: Each call to `logStep()` increments the counter automatically
2. **Per-Test Reset**: Counter resets to 0 at the start of each test
3. **Thread-Safe**: Uses ThreadLocal to support parallel test execution
4. **Two Variants**:
   - `logStep(String message)` - Simple step logging
   - `logStep(String message, Object... details)` - Step with additional details

## Benefits:

- Clear test flow tracking
- Easy to identify which step failed
- Consistent logging format across all tests
- No manual step number management
