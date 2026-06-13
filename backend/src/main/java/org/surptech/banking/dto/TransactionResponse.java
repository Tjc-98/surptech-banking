package org.surptech.banking.dto;

import org.surptech.banking.entity.Transaction;

import java.time.LocalDateTime;

/**
 * Response object for a single transaction.
 */
public class TransactionResponse {

    private Long id;
    private String type;
    private double amount;
    private String description;
    private LocalDateTime createdAt;

    public static TransactionResponse from(Transaction t) {
        TransactionResponse r = new TransactionResponse();
        r.id = t.getId();
        r.type = t.getType().name();
        r.amount = t.getAmount();
        r.description = t.getDescription();
        r.createdAt = t.getCreatedAt();
        return r;
    }

    public Long getId() { return id; }
    public String getType() { return type; }
    public double getAmount() { return amount; }
    public String getDescription() { return description; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
