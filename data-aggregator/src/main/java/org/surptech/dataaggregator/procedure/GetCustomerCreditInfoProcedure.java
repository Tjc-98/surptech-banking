package org.surptech.dataaggregator.procedure;

import lombok.extern.slf4j.Slf4j;
import org.surptech.dataaggregator.client.CreditProfileClient;
import org.surptech.dataaggregator.client.CustomerProfileClient;
import org.surptech.dataaggregator.config.ApplicationContextProvider;
import org.surptech.dataaggregator.domain.CustomerCreditInfoResponse;
import org.surptech.dataaggregator.domain.EmptyRequest;
import org.surptech.dataaggregator.entity.CreditProfileEntity;
import org.surptech.dataaggregator.entity.CustomerCreditInfoEntity;
import org.surptech.dataaggregator.entity.CustomerProfileEntity;
import org.surptech.dataaggregator.mapper.CustomerCreditInfoMapper;

import java.util.Optional;

@Slf4j
public class GetCustomerCreditInfoProcedure extends BaseProcedure<EmptyRequest, CustomerCreditInfoResponse> {

    private final String socialSecurityNumber;
    private final CustomerProfileClient customerProfileClient;
    private final CreditProfileClient creditProfileClient;

    public GetCustomerCreditInfoProcedure(String socialSecurityNumber) {
        super(EmptyRequest.builder().build());
        this.socialSecurityNumber = socialSecurityNumber;
        this.customerProfileClient = ApplicationContextProvider.getApplicationContext()
                .getBean(CustomerProfileClient.class);
        this.creditProfileClient = ApplicationContextProvider.getApplicationContext()
                .getBean(CreditProfileClient.class);
    }

    @Override
    public CustomerCreditInfoResponse executeProcedure() {
        log.info("Aggregating customer and credit information for SSN: {}", socialSecurityNumber);

        // Fetch customer profile entity
        Optional<CustomerProfileEntity> customerProfile = customerProfileClient.getCustomerProfile(socialSecurityNumber);
        
        // Fetch credit profile entity
        Optional<CreditProfileEntity> creditProfile = creditProfileClient.getCreditProfile(socialSecurityNumber);

        // If neither profile exists, return null
        if (customerProfile.isEmpty() && creditProfile.isEmpty()) {
            log.warn("No customer or credit profile found for SSN: {}", socialSecurityNumber);
            return null;
        }

        // Build aggregated entity
        CustomerCreditInfoEntity.Builder builder = CustomerCreditInfoEntity.builder()
                .socialSecurityNumber(socialSecurityNumber);

        // Add customer profile data if available
        if (customerProfile.isPresent()) {
            CustomerProfileEntity cp = customerProfile.get();
            builder.firstName(cp.getFirstName())
                   .lastName(cp.getLastName())
                   .address(cp.getAddress());
        }

        // Add credit profile data if available
        if (creditProfile.isPresent()) {
            CreditProfileEntity crp = creditProfile.get();
            builder.currentBalance(crp.getCurrentBalance())
                   .spendBalance(crp.getSpendBalance())
                   .interestRate(crp.getInterestRate());
        }

        CustomerCreditInfoEntity entity = builder.build();
        
        // Convert entity to response DTO
        response = CustomerCreditInfoMapper.toResponse(entity);
        
        log.info("Successfully aggregated customer and credit information for SSN: {}", socialSecurityNumber);

        return response;
    }
}
