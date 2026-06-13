package org.surptech.banking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.surptech.banking.entity.CustomerProfile;

public interface CustomerProfileRepository extends JpaRepository<CustomerProfile, String> {
}
