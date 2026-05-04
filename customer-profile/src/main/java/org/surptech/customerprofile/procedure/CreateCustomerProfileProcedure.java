package org.surptech.customerprofile.procedure;

import org.surptech.customerprofile.domain.CustomerProfile;
import org.surptech.customerprofile.domain.CustomerProfileRequest;
import org.surptech.customerprofile.repository.CustomerProfileRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CreateCustomerProfileProcedure extends BaseProcedure<CustomerProfileRequest, CustomerProfile> {

    private final CustomerProfileRepository customerProfileRepository;

    public CreateCustomerProfileProcedure(CustomerProfileRequest customerProfileRequest) {
        super(customerProfileRequest);
        this.customerProfileRepository = applicationServices.getCustomerProfileRepository();
    }

    @Override
    public CustomerProfile executeProcedure() {
        CustomerProfile customerProfile = CustomerProfile.builder()
                .socialSecurityNumber(request.getSocialSecurityNumber())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .address(request.getAddress())
                .build();

        response = customerProfileRepository.save(customerProfile);
        log.info("Created customer profile for SSN: {}", response.getSocialSecurityNumber());

        return response;
    }
}
