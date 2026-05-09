package org.surptech.customerprofile.procedure;

import lombok.extern.slf4j.Slf4j;
import org.surptech.common.procedure.BaseProcedure;
import org.surptech.common.validation.ValidationUtils;
import org.surptech.customerprofile.config.ApplicationContextProvider;
import org.surptech.customerprofile.domain.response.CustomerProfileResponse;
import org.surptech.customerprofile.domain.entity.CustomerProfileEntity;
import org.surptech.customerprofile.mapper.CustomerProfileMapper;
import org.surptech.customerprofile.repository.CustomerProfileRepository;

import java.util.Optional;

/**
 * Procedure to retrieve an existing customer profile by social security number.
 */
@Slf4j
public class GetCustomerProfileProcedure extends BaseProcedure<String, CustomerProfileResponse> {

    private final CustomerProfileRepository customerProfileRepository;

    /**
     * Constructs a GetCustomerProfileProcedure with the provided social security number.
     *
     * @param socialSecurityNumber the social security number of the customer to retrieve
     */
    public GetCustomerProfileProcedure(String socialSecurityNumber) {
        super(socialSecurityNumber);
        this.customerProfileRepository = ApplicationContextProvider.getBean(CustomerProfileRepository.class);
    }

    @Override
    protected CustomerProfileResponse executeProcedure() {
        // Validate SSN format
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
