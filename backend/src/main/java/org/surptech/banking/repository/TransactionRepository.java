package org.surptech.banking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.surptech.banking.entity.Transaction;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // Spring Data generates the query from the method name
    List<Transaction> findByCustomerSocialSecurityNumberOrderByCreatedAtDesc(String socialSecurityNumber);
}
