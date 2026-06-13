package org.surptech.banking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.surptech.banking.dto.CustomerInfoResponse;
import org.surptech.banking.entity.CreditProfile;
import org.surptech.banking.entity.CustomerProfile;
import org.surptech.banking.repository.CreditProfileRepository;
import org.surptech.banking.repository.CustomerProfileRepository;
import org.surptech.banking.repository.TransactionRepository;
import org.surptech.banking.service.BankingInfoService;
import org.surptech.banking.service.CreditProfileService;
import org.surptech.banking.service.CustomerProfileService;
import org.surptech.banking.service.TransactionService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("BankingInfoService Tests")
class BankingInfoServiceTest {

    private CustomerProfileRepository customerProfileRepository;
    private CreditProfileRepository creditProfileRepository;
    private TransactionRepository transactionRepository;
    private BankingInfoService bankingInfoService;

    @BeforeEach
    void setUp() {
        customerProfileRepository = mock(CustomerProfileRepository.class);
        creditProfileRepository = mock(CreditProfileRepository.class);
        transactionRepository = mock(TransactionRepository.class);

        CustomerProfileService customerProfileService = new CustomerProfileService(customerProfileRepository);
        CreditProfileService creditProfileService = new CreditProfileService(creditProfileRepository);
        TransactionService transactionService = new TransactionService(transactionRepository, customerProfileService);

        bankingInfoService = new BankingInfoService(customerProfileService, creditProfileService, transactionService);

        // Default: no transactions
        when(transactionRepository
                .findByCustomerSocialSecurityNumberOrderByCreatedAtDesc(anyString()))
                .thenReturn(List.of());
    }

    @Test
    @DisplayName("Returns combined info when both profiles exist")
    void getCustomerInfo_BothProfilesExist_ReturnsCombinedInfo() {
        String ssn = "123-45-6789";
        CustomerProfile customer = new CustomerProfile(ssn, "James", "Smith", "456 Tailor Street");
        CreditProfile credit = new CreditProfile(ssn, 15000.0, 5000.0, 3.5);

        when(customerProfileRepository.findById(ssn)).thenReturn(Optional.of(customer));
        when(creditProfileRepository.findById(ssn)).thenReturn(Optional.of(credit));

        CustomerInfoResponse response = bankingInfoService.getCustomerInfo(ssn);

        assertNotNull(response);
        assertEquals("James", response.getFirstName());
        assertEquals("Smith", response.getLastName());
        assertEquals(15000.0, response.getFullCreditBalance());
        assertEquals(10000.0, response.getAvailableBalance());
        assertEquals(3.5, response.getInterestRate());
    }

    @Test
    @DisplayName("Available balance is correctly calculated as fullCreditBalance minus spendBalance")
    void getCustomerInfo_CalculatesAvailableBalance() {
        String ssn = "123-45-6789";
        CustomerProfile customer = new CustomerProfile(ssn, "James", "Smith", "456 Tailor Street");
        CreditProfile credit = new CreditProfile(ssn, 20000.0, 8000.0, 4.0);

        when(customerProfileRepository.findById(ssn)).thenReturn(Optional.of(customer));
        when(creditProfileRepository.findById(ssn)).thenReturn(Optional.of(credit));

        CustomerInfoResponse response = bankingInfoService.getCustomerInfo(ssn);

        assertEquals(12000.0, response.getAvailableBalance());
    }

    @Test
    @DisplayName("Returns null when no profiles exist for SSN")
    void getCustomerInfo_NoProfilesExist_ReturnsNull() {
        String ssn = "000-00-0000";
        when(customerProfileRepository.findById(ssn)).thenReturn(Optional.empty());
        when(creditProfileRepository.findById(ssn)).thenReturn(Optional.empty());

        CustomerInfoResponse response = bankingInfoService.getCustomerInfo(ssn);

        assertNull(response);
    }

    @Test
    @DisplayName("Returns partial info when only customer profile exists")
    void getCustomerInfo_OnlyCustomerProfileExists_ReturnsPartialInfo() {
        String ssn = "123-45-6789";
        CustomerProfile customer = new CustomerProfile(ssn, "James", "Smith", "456 Tailor Street");

        when(customerProfileRepository.findById(ssn)).thenReturn(Optional.of(customer));
        when(creditProfileRepository.findById(ssn)).thenReturn(Optional.empty());

        CustomerInfoResponse response = bankingInfoService.getCustomerInfo(ssn);

        assertNotNull(response);
        assertEquals("James", response.getFirstName());
        assertEquals(0.0, response.getFullCreditBalance());
        assertEquals(0.0, response.getAvailableBalance());
    }
}
