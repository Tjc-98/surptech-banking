package org.surptech.banking.service;

import org.springframework.stereotype.Service;
import org.surptech.banking.entity.CreditProfile;
import org.surptech.banking.repository.CreditProfileRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CreditProfileService {

    private final CreditProfileRepository creditProfileRepository;

    public CreditProfileService(CreditProfileRepository creditProfileRepository) {
        this.creditProfileRepository = creditProfileRepository;
    }

    public Optional<CreditProfile> getCreditProfile(String socialSecurityNumber) {
        return creditProfileRepository.findById(socialSecurityNumber);
    }

    public CreditProfile saveCreditProfile(CreditProfile creditProfile) {
        return creditProfileRepository.save(creditProfile);
    }

    public List<CreditProfile> getAllCreditProfiles() {
        return creditProfileRepository.findAll();
    }
}
