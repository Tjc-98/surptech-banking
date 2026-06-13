package org.surptech.banking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.surptech.banking.entity.Transaction;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // Spring Data turns this method name into a SQL query automatically.
    // It fetches all transactions for a given SSN, sorted newest first.
    List<Transaction> findByCustomerSocialSecurityNumberOrderByCreatedAtDesc(String socialSecurityNumber);
}
