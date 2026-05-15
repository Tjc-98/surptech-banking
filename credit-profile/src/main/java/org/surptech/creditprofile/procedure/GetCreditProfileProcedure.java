package org.surptech.creditprofile.procedure;

import lombok.extern.slf4j.Slf4j;
import org.surptech.common.procedure.BaseProcedure;
import org.surptech.common.validation.ValidationUtils;
import org.surptech.creditprofile.config.ApplicationContextProvider;
import org.surptech.creditprofile.domain.response.CreditProfileResponse;
import org.surptech.creditprofile.domain.entity.CreditProfileEntity;
import org.surptech.creditprofile.mapper.CreditProfileMapper;
import org.surptech.creditprofile.repository.CreditProfileRepository;

import java.util.Optional;

/**
 * Procedure to retrieve an existing credit profile by social security number.
 */
@Slf4j
public class GetCreditProfileProcedure extends BaseProcedure<String, CreditProfileResponse> {

    private final CreditProfileRepository creditProfileRepository;

    /**
     * Constructs a GetCreditProfileProcedure with the provided social security number.
     *
     * @param socialSecurityNumber the social security number of the customer to retrieve
     */
    public GetCreditProfileProcedure(String socialSecurityNumber) {
        super(socialSecurityNumber);
        this.creditProfileRepository = ApplicationContextProvider.getBean(CreditProfileRepository.class);
    }

    @Override
    protected CreditProfileResponse executeProcedure() {
        // Validate SSN format
        log.debug("Validating social security number format");
        ValidationUtils.validateSocialSecurityNumber(request);

        log.debug("Searching for credit profile with social security number: {}", request);
        Optional<CreditProfileEntity> creditProfileEntity =
                creditProfileRepository.findBySocialSecurityNumber(request);

        if (creditProfileEntity.isPresent()) {
            CreditProfileResponse response = CreditProfileMapper.toResponse(creditProfileEntity.get());
            log.info("Successfully retrieved credit profile for social security number: {}", request);
            return response;
        } else {
            log.info("Credit profile not found for social security number: {}", request);
            return null;
        }
    }
}
