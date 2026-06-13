package org.surptech.banking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.surptech.banking.entity.CreditProfile;

public interface CreditProfileRepository extends JpaRepository<CreditProfile, String> {
}
