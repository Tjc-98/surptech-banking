package org.surptech.banking;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("BankingApiController Integration Tests")
class BankingApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // --- Customer info ---

    @Test
    @DisplayName("GET /api/customer/info with valid SSN returns 200 and customer data")
    void getCustomerInfo_ValidSsn_Returns200() throws Exception {
        mockMvc.perform(get("/api/customer/info")
                        .param("socialSecurityNumber", "123-45-6789"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("James"))
                .andExpect(jsonPath("$.lastName").value("Smith"))
                .andExpect(jsonPath("$.fullCreditBalance").value(15000.0))
                .andExpect(jsonPath("$.availableBalance").value(10000.0));
    }

    @Test
    @DisplayName("GET /api/customer/info with unknown SSN returns 404")
    void getCustomerInfo_UnknownSsn_Returns404() throws Exception {
        mockMvc.perform(get("/api/customer/info")
                        .param("socialSecurityNumber", "000-00-0000"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /api/customer/info with blank SSN returns 400 with error message")
    void getCustomerInfo_BlankSsn_Returns400() throws Exception {
        mockMvc.perform(get("/api/customer/info")
                        .param("socialSecurityNumber", ""))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    @DisplayName("GET /api/customer/info with malformed SSN returns 400 with error message")
    void getCustomerInfo_InvalidSsnFormat_Returns400() throws Exception {
        mockMvc.perform(get("/api/customer/info")
                        .param("socialSecurityNumber", "INVALID"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Invalid SSN format. Expected format: XXX-XX-XXXX."));
    }

    // --- Create customer ---

    @Test
    @DisplayName("POST /api/customer/create with valid data returns 201")
    void createCustomerProfile_ValidRequest_Returns201() throws Exception {
        String json = """
                {
                    "socialSecurityNumber": "111-22-3333",
                    "firstName": "Alice",
                    "lastName": "Johnson",
                    "address": "789 Oak Avenue, Chicago, IL 60601"
                }
                """;

        mockMvc.perform(post("/api/customer/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("Alice"));
    }

    @Test
    @DisplayName("POST /api/customer/create with missing first name returns 400")
    void createCustomerProfile_MissingFirstName_Returns400() throws Exception {
        String json = """
                {
                    "socialSecurityNumber": "111-22-4444",
                    "lastName": "Johnson",
                    "address": "789 Oak Avenue"
                }
                """;

        mockMvc.perform(post("/api/customer/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("First name is required."));
    }

    // --- Create credit ---

    @Test
    @DisplayName("POST /api/credit/create with valid data returns 201")
    void createCreditProfile_ValidRequest_Returns201() throws Exception {
        String json = """
                {
                    "socialSecurityNumber": "111-22-3333",
                    "fullCreditBalance": 10000.0,
                    "spendBalance": 2500.0,
                    "interestRate": 5.0
                }
                """;

        mockMvc.perform(post("/api/credit/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.fullCreditBalance").value(10000.0));
    }

    // --- Transactions ---

    @Test
    @DisplayName("POST /api/transaction/add with valid deposit returns 201")
    void addTransaction_ValidDeposit_Returns201() throws Exception {
        String json = """
                {
                    "socialSecurityNumber": "123-45-6789",
                    "type": "DEPOSIT",
                    "amount": 500.0,
                    "description": "Test deposit"
                }
                """;

        mockMvc.perform(post("/api/transaction/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.type").value("DEPOSIT"))
                .andExpect(jsonPath("$.amount").value(500.0));
    }

    @Test
    @DisplayName("POST /api/transaction/add with amount zero returns 400")
    void addTransaction_ZeroAmount_Returns400() throws Exception {
        String json = """
                {
                    "socialSecurityNumber": "123-45-6789",
                    "type": "DEPOSIT",
                    "amount": 0
                }
                """;

        mockMvc.perform(post("/api/transaction/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Amount must be greater than zero."));
    }

    @Test
    @DisplayName("POST /api/transaction/add for unknown customer returns 404")
    void addTransaction_UnknownCustomer_Returns404() throws Exception {
        String json = """
                {
                    "socialSecurityNumber": "000-00-0000",
                    "type": "DEPOSIT",
                    "amount": 100.0
                }
                """;

        mockMvc.perform(post("/api/transaction/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /api/transaction/history returns list for known customer")
    void getTransactionHistory_KnownCustomer_ReturnsList() throws Exception {
        mockMvc.perform(get("/api/transaction/history")
                        .param("socialSecurityNumber", "123-45-6789"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}
