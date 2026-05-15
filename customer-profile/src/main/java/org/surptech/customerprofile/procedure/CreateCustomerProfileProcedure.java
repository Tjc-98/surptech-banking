package org.surptech.customerprofile.procedure;

import lombok.extern.slf4j.Slf4j;
import org.surptech.common.config.ApplicationContextProvider;
import org.surptech.common.procedure.BaseProcedure;
import org.surptech.customerprofile.domain.response.CustomerProfileResponse;
import org.surptech.customerprofile.domain.request.CustomerProfileRequest;
import org.surptech.customerprofile.domain.entity.CustomerProfileEntity;
import org.surptech.customerprofile.mapper.CustomerProfileMapper;
import org.surptech.customerprofile.repository.CustomerProfileRepository;

/**
 * Procedure for creating a new customer profile or updating an existing one.
 *
 * Orchestrates the flow:
 * 1. Converts the incoming request DTO to an internal entity
 * 2. Persists the entity using the repository
 * 3. Converts the persisted entity back to a response DTO
 *
 * If a customer with the same SSN exists, the existing profile will be updated.
 */
@Slf4j
public class CreateCustomerProfileProcedure extends BaseProcedure<CustomerProfileRequest, CustomerProfileResponse> {

    private final CustomerProfileRepository customerProfileRepository;

    /**
     * Constructs the procedure with the customer profile request data.
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
        // Convert request DTO to internal entity
        CustomerProfileEntity entity = CustomerProfileMapper.toEntity(request);

        log.debug("Saving customer profile to repository");
        // Save or update the entity in the database
        CustomerProfileEntity savedEntity = customerProfileRepository.save(entity);
        
        log.debug("Converting saved entity back to response DTO");
        // Convert the saved entity back to response DTO
        CustomerProfileResponse response = CustomerProfileMapper.toResponse(savedEntity);
        
        log.info("Successfully created/updated customer profile for social security number: {}",
                 response.getSocialSecurityNumber());

        return response;
    }
}
