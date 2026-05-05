package org.surptech.dataaggregator.procedure;

import lombok.extern.slf4j.Slf4j;
import org.surptech.dataaggregator.client.CreditProfileClient;
import org.surptech.dataaggregator.client.CustomerProfileClient;
import org.surptech.dataaggregator.config.ApplicationContextProvider;
import org.surptech.dataaggregator.domain.CreditProfile;
import org.surptech.dataaggregator.domain.CustomerCreditInfo;
import org.surptech.dataaggregator.domain.CustomerProfile;
import org.surptech.dataaggregator.domain.EmptyRequest;

import java.util.Optional;

@Slf4j
public class GetCustomerCreditInfoProcedure extends BaseProcedure<EmptyRequest, CustomerCreditInfo> {

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
    public CustomerCreditInfo executeProcedure() {
        log.info("Aggregating customer and credit information for SSN: {}", socialSecurityNumber);

        // Fetch customer profile
        Optional<CustomerProfile> customerProfile = customerProfileClient.getCustomerProfile(socialSecurityNumber);
        
        // Fetch credit profile
        Optional<CreditProfile> creditProfile = creditProfileClient.getCreditProfile(socialSecurityNumber);

        // If neither profile exists, return null
        if (customerProfile.isEmpty() && creditProfile.isEmpty()) {
            log.warn("No customer or credit profile found for SSN: {}", socialSecurityNumber);
            return null;
        }

        // Build aggregated response
        CustomerCreditInfo.Builder builder = CustomerCreditInfo.builder()
                .socialSecurityNumber(socialSecurityNumber);

        // Add customer profile data if available
        if (customerProfile.isPresent()) {
            CustomerProfile cp = customerProfile.get();
            builder.firstName(cp.getFirstName())
                   .lastName(cp.getLastName())
                   .address(cp.getAddress());
        }

        // Add credit profile data if available
        if (creditProfile.isPresent()) {
            CreditProfile crp = creditProfile.get();
            builder.currentBalance(crp.getCurrentBalance())
                   .spendBalance(crp.getSpendBalance())
                   .interestRate(crp.getInterestRate());
        }

        response = builder.build();
        log.info("Successfully aggregated customer and credit information for SSN: {}", socialSecurityNumber);

        return response;
    }
}
