package org.surptech.customerprofile.procedure;

import lombok.extern.slf4j.Slf4j;
import org.surptech.common.procedure.BaseProcedure;
import org.surptech.customerprofile.config.ApplicationContextProvider;
import org.surptech.customerprofile.domain.response.CustomerProfileResponse;
import org.surptech.customerprofile.domain.request.CustomerProfileRequest;
import org.surptech.customerprofile.domain.entity.CustomerProfileEntity;
import org.surptech.customerprofile.mapper.CustomerProfileMapper;
import org.surptech.customerprofile.repository.CustomerProfileRepository;

/**
 * Procedure to create a new customer profile or update an existing one.
 */
@Slf4j
public class CreateCustomerProfileProcedure extends BaseProcedure<CustomerProfileRequest, CustomerProfileResponse> {

    private final CustomerProfileRepository customerProfileRepository;

    /**
     * Constructs a CreateCustomerProfileProcedure with the provided customer profile request.
     *
     * @param customerProfileRequest the customer profile data to create or update
     */
    public CreateCustomerProfileProcedure(CustomerProfileRequest customerProfileRequest) {
        super(customerProfileRequest);
        this.customerProfileRepository = ApplicationContextProvider.getBean(CustomerProfileRepository.class);
    }

    @Override
    protected CustomerProfileResponse executeProcedure() {
        log.debug("Converting customer profile request to entity");
        // Convert request DTO to entity
        CustomerProfileEntity entity = CustomerProfileMapper.toEntity(request);

        log.debug("Saving customer profile to repository");
        // Save entity
        CustomerProfileEntity savedEntity = customerProfileRepository.save(entity);
        
        log.debug("Converting saved entity back to response DTO");
        // Convert entity back to response DTO
        CustomerProfileResponse response = CustomerProfileMapper.toResponse(savedEntity);
        
        log.info("Successfully created/updated customer profile for social security number: {}",
                 response.getSocialSecurityNumber());

        return response;
    }
}
