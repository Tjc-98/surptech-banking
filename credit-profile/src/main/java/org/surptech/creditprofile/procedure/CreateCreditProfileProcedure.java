package org.surptech.creditprofile.procedure;

import lombok.extern.slf4j.Slf4j;
import org.surptech.common.config.ApplicationContextProvider;
import org.surptech.common.procedure.BaseProcedure;
import org.surptech.creditprofile.domain.response.CreditProfileResponse;
import org.surptech.creditprofile.domain.request.CreditProfileRequest;
import org.surptech.creditprofile.domain.entity.CreditProfileEntity;
import org.surptech.creditprofile.mapper.CreditProfileMapper;
import org.surptech.creditprofile.repository.CreditProfileRepository;

/**
 * Procedure to create a new credit profile or update an existing one.
 */
@Slf4j
public class CreateCreditProfileProcedure extends BaseProcedure<CreditProfileRequest, CreditProfileResponse> {

    private final CreditProfileRepository creditProfileRepository;

    /**
     * Constructs a CreateCreditProfileProcedure with the provided credit profile request.
     *
     * @param creditProfileRequest the credit profile data to create or update
     */
    public CreateCreditProfileProcedure(CreditProfileRequest creditProfileRequest) {
        super(creditProfileRequest);
        this.creditProfileRepository = ApplicationContextProvider.getBean(CreditProfileRepository.class);
    }

    @Override
    protected CreditProfileResponse executeProcedure() {
        log.debug("Converting credit profile request to entity");
        // Convert request DTO to entity
        CreditProfileEntity entity = CreditProfileMapper.toEntity(request);

        log.debug("Saving credit profile to repository");
        // Save entity
        CreditProfileEntity savedEntity = creditProfileRepository.save(entity);
        
        log.debug("Converting saved entity back to response DTO");
        // Convert entity back to response DTO
        CreditProfileResponse response = CreditProfileMapper.toResponse(savedEntity);
        
        log.info("Successfully created/updated credit profile for social security number: {}",
                 response.getSocialSecurityNumber());

        return response;
    }
}
