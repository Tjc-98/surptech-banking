package org.surptech.customerprofile.procedure;

import lombok.extern.slf4j.Slf4j;
import org.surptech.customerprofile.domain.response.CustomerProfileResponse;
import org.surptech.customerprofile.domain.entity.CustomerProfileEntity;
import org.surptech.customerprofile.mapper.CustomerProfileMapper;
import org.surptech.customerprofile.repository.CustomerProfileRepository;

import java.util.Optional;

@Slf4j
public class GetCustomerProfileProcedure extends BaseProcedure<String, CustomerProfileResponse> {

    private final CustomerProfileRepository customerProfileRepository;

    public GetCustomerProfileProcedure(String socialSecurityNumber) {
        super(socialSecurityNumber);
        this.customerProfileRepository = applicationServices.getCustomerProfileRepository();
    }

    @Override
    public CustomerProfileResponse executeProcedure() {
        Optional<CustomerProfileEntity> customerProfileEntity = 
                customerProfileRepository.findBySocialSecurityNumber(request);

        if (customerProfileEntity.isPresent()) {
            response = CustomerProfileMapper.toResponse(customerProfileEntity.get());
            log.info("Found customer profile for SocialSecurityNumber: {}", request);
        } else {
            log.info("No customer profile found for SocialSecurityNumber: {}", request);
        }

        return response;
    }
}
