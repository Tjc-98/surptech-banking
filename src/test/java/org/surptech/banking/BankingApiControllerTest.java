package org.surptech.banking;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("BankingApiController Integration Tests")
class BankingApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("GET /api/customer/info with valid SSN returns 200 and customer data")
    void getCustomerInfo_ValidSsn_Returns200() throws Exception {
        mockMvc.perform(get("/api/customer/info")
                        .param("socialSecurityNumber", "123-45-6789"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("James"))
                .andExpect(jsonPath("$.lastName").value("Smith"))
                .andExpect(jsonPath("$.fullCreditBalance").value(15000.0));
    }

    @Test
    @DisplayName("GET /api/customer/info with unknown SSN returns 404")
    void getCustomerInfo_UnknownSsn_Returns404() throws Exception {
        mockMvc.perform(get("/api/customer/info")
                        .param("socialSecurityNumber", "000-00-0000"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /api/customer/info with blank SSN returns 400")
    void getCustomerInfo_BlankSsn_Returns400() throws Exception {
        mockMvc.perform(get("/api/customer/info")
                        .param("socialSecurityNumber", ""))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/customer/create saves a new customer and returns 201")
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
    @DisplayName("POST /api/credit/create saves a new credit profile and returns 201")
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
}
