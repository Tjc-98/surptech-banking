package org.surptech.customerprofile.procedure;

import lombok.extern.slf4j.Slf4j;
import org.surptech.common.procedure.BaseProcedure;
import org.surptech.customerprofile.config.ApplicationContextProvider;
import org.surptech.customerprofile.domain.response.CustomerProfileResponse;
import org.surptech.customerprofile.domain.request.CustomerProfileRequest;
import org.surptech.customerprofile.domain.entity.CustomerProfileEntity;
import org.surptech.customerprofile.mapper.CustomerProfileMapper;
import org.surptech.customerprofile.repository.CustomerProfileRepository;

@Slf4j
public class CreateCustomerProfileProcedure extends BaseProcedure<CustomerProfileRequest, CustomerProfileResponse> {

    private final CustomerProfileRepository customerProfileRepository;

    public CreateCustomerProfileProcedure(CustomerProfileRequest customerProfileRequest) {
        super(customerProfileRequest);
        this.customerProfileRepository = ApplicationContextProvider.getBean(CustomerProfileRepository.class);
    }

    @Override
    protected CustomerProfileResponse executeProcedure() {
        // Convert request DTO to entity
        CustomerProfileEntity entity = CustomerProfileMapper.toEntity(request);

        // Save entity
        CustomerProfileEntity savedEntity = customerProfileRepository.save(entity);
        
        // Convert entity back to response DTO
        CustomerProfileResponse response = CustomerProfileMapper.toResponse(savedEntity);
        
        log.info("Created customer profile for SocialSecurityNumber: {}", response.getSocialSecurityNumber());

        return response;
    }
}
