package org.surptech.customerprofile.procedure;

import lombok.extern.slf4j.Slf4j;
import org.surptech.customerprofile.domain.CustomerProfileResponse;
import org.surptech.customerprofile.domain.EmptyRequest;
import org.surptech.customerprofile.entity.CustomerProfileEntity;
import org.surptech.customerprofile.mapper.CustomerProfileMapper;
import org.surptech.customerprofile.repository.CustomerProfileRepository;

import java.util.Optional;

@Slf4j
public class GetCustomerProfileProcedure extends BaseProcedure<EmptyRequest, CustomerProfileResponse> {

    private final String socialSecurityNumber;
    private final CustomerProfileRepository customerProfileRepository;

    public GetCustomerProfileProcedure(String socialSecurityNumber) {
        super(EmptyRequest.builder().build());
        this.socialSecurityNumber = socialSecurityNumber;
        this.customerProfileRepository = applicationServices.getCustomerProfileRepository();
    }

    @Override
    public CustomerProfileResponse executeProcedure() {
        Optional<CustomerProfileEntity> customerProfileEntity = 
                customerProfileRepository.findBySocialSecurityNumber(socialSecurityNumber);

        if (customerProfileEntity.isPresent()) {
            response = CustomerProfileMapper.toResponse(customerProfileEntity.get());
            log.info("Found customer profile for SSN: {}", socialSecurityNumber);
        } else {
            log.info("No customer profile found for SSN: {}", socialSecurityNumber);
        }

        return response;
    }
}
