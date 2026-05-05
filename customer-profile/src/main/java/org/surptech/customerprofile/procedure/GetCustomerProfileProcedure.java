package org.surptech.customerprofile.procedure;

import lombok.extern.slf4j.Slf4j;
import org.surptech.customerprofile.domain.CustomerProfile;
import org.surptech.customerprofile.domain.EmptyRequest;
import org.surptech.customerprofile.repository.CustomerProfileRepository;

import java.util.Optional;

@Slf4j
public class GetCustomerProfileProcedure extends BaseProcedure<EmptyRequest, CustomerProfile> {

    private final String socialSecurityNumber;
    private final CustomerProfileRepository customerProfileRepository;

    public GetCustomerProfileProcedure(String socialSecurityNumber) {
        super(EmptyRequest.builder().build());
        this.socialSecurityNumber = socialSecurityNumber;
        this.customerProfileRepository = applicationServices.getCustomerProfileRepository();
    }

    @Override
    public CustomerProfile executeProcedure() {
        Optional<CustomerProfile> customerProfile = customerProfileRepository.findBySocialSecurityNumber(socialSecurityNumber);

        if (customerProfile.isPresent()) {
            response = customerProfile.get();
            log.info("Found customer profile for SSN: {}", socialSecurityNumber);
        } else {
            log.info("No customer profile found for SSN: {}", socialSecurityNumber);
        }

        return response;
    }
}
