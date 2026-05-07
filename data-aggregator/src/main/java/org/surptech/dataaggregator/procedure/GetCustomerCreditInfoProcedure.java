package org.surptech.dataaggregator.procedure;

import lombok.extern.slf4j.Slf4j;
import org.surptech.dataaggregator.client.CreditProfileClient;
import org.surptech.dataaggregator.client.CustomerProfileClient;
import org.surptech.dataaggregator.config.ApplicationContextProvider;
import org.surptech.dataaggregator.domain.response.CustomerCreditInfoResponse;
import org.surptech.dataaggregator.domain.entity.CreditProfileEntity;
import org.surptech.dataaggregator.domain.entity.CustomerCreditInfoEntity;
import org.surptech.dataaggregator.domain.entity.CustomerProfileEntity;
import org.surptech.dataaggregator.mapper.CustomerCreditInfoMapper;

import java.util.Optional;

@Slf4j
public class GetCustomerCreditInfoProcedure extends BaseProcedure<String, CustomerCreditInfoResponse> {

    private final CustomerProfileClient customerProfileClient;
    private final CreditProfileClient creditProfileClient;

    public GetCustomerCreditInfoProcedure(String socialSecurityNumber) {
        super(socialSecurityNumber);
        this.customerProfileClient = ApplicationContextProvider.getApplicationContext()
                .getBean(CustomerProfileClient.class);
        this.creditProfileClient = ApplicationContextProvider.getApplicationContext()
                .getBean(CreditProfileClient.class);
    }

    @Override
    public CustomerCreditInfoResponse executeProcedure() {
        log.info("Aggregating customer and credit information for SSN: {}", request);

        // Fetch customer profile entity
        Optional<CustomerProfileEntity> customerProfile = customerProfileClient.getCustomerProfile(request);
        
        // Fetch credit profile entity
        Optional<CreditProfileEntity> creditProfile = creditProfileClient.getCreditProfile(request);

        // If neither profile exists, return null
        if (customerProfile.isEmpty() && creditProfile.isEmpty()) {
            log.warn("No customer or credit profile found for SSN: {}", request);
            return null;
        }

        // Build aggregated entity
        var customerCreditInfoEntityBuilder = CustomerCreditInfoEntity.builder()
                .socialSecurityNumber(request);

        // Add customer profile data if available
        if (customerProfile.isPresent()) {
            CustomerProfileEntity customerProfileEntity = customerProfile.get();
            customerCreditInfoEntityBuilder = customerCreditInfoEntityBuilder
                   .firstName(customerProfileEntity.getFirstName())
                   .lastName(customerProfileEntity.getLastName())
                   .address(customerProfileEntity.getAddress());
        }

        // Add credit profile data if available
        if (creditProfile.isPresent()) {
            CreditProfileEntity creditProfileEntity = creditProfile.get();
            customerCreditInfoEntityBuilder = customerCreditInfoEntityBuilder
                   .currentBalance(creditProfileEntity.getCurrentBalance())
                   .spendBalance(creditProfileEntity.getSpendBalance())
                   .interestRate(creditProfileEntity.getInterestRate());
        }

        CustomerCreditInfoEntity entity = customerCreditInfoEntityBuilder.build();

        // Convert entity to response DTO
        CustomerCreditInfoResponse response = CustomerCreditInfoMapper.toResponse(entity);
        
        log.info("Successfully aggregated customer and credit information for SSN: {}", request);

        return response;
    }
}
