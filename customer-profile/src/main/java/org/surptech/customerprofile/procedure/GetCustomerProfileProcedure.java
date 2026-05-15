package org.surptech.customerprofile.procedure;

import lombok.extern.slf4j.Slf4j;
import org.surptech.common.config.ApplicationContextProvider;
import org.surptech.common.procedure.BaseProcedure;
import org.surptech.common.validation.ValidationUtils;
import org.surptech.customerprofile.domain.response.CustomerProfileResponse;
import org.surptech.customerprofile.domain.entity.CustomerProfileEntity;
import org.surptech.customerprofile.mapper.CustomerProfileMapper;
import org.surptech.customerprofile.repository.CustomerProfileRepository;

import java.util.Optional;

/**
 * Procedure for retrieving an existing customer profile by social security number.
 *
 * Orchestrates the flow:
 * 1. Validates the SSN format
 * 2. Queries the repository for the customer
 * 3. Converts the entity to a response DTO if found
 *
 * Returns null if the customer profile is not found (handled by controller).
 */
@Slf4j
public class GetCustomerProfileProcedure extends BaseProcedure<String, CustomerProfileResponse> {

    private final CustomerProfileRepository customerProfileRepository;

    /**
     * Constructs the procedure with the customer's social security number.
     *
     * @param socialSecurityNumber the social security number of the customer to retrieve (USA format: XXX-XX-XXXX)
     */
    public GetCustomerProfileProcedure(String socialSecurityNumber) {
        super(socialSecurityNumber);
        this.customerProfileRepository = ApplicationContextProvider.getBean(CustomerProfileRepository.class);
    }

    @Override
    protected CustomerProfileResponse executeProcedure() {
        // Validate SSN format before database lookup
        log.debug("Validating social security number format");
        ValidationUtils.validateSocialSecurityNumber(request);

        log.debug("Searching for customer profile with social security number: {}", request);
        Optional<CustomerProfileEntity> customerProfileEntity =
                customerProfileRepository.findBySocialSecurityNumber(request);

        if (customerProfileEntity.isPresent()) {
            CustomerProfileResponse response = CustomerProfileMapper.toResponse(customerProfileEntity.get());
            log.info("Successfully retrieved customer profile for social security number: {}", request);
            return response;
        } else {
            log.info("Customer profile not found for social security number: {}", request);
            return null;
        }
    }
}
