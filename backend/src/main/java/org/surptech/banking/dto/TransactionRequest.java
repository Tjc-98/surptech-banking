package org.surptech.banking.dto;

import org.surptech.banking.entity.Transaction;

// What the client sends when recording a new deposit or withdrawal.
public class TransactionRequest {

    private String socialSecurityNumber;
    private Transaction.Type type;   // DEPOSIT or WITHDRAWAL
    private double amount;
    private String description;      // optional note

    public String getSocialSecurityNumber() { return socialSecurityNumber; }
    public void setSocialSecurityNumber(String socialSecurityNumber) { this.socialSecurityNumber = socialSecurityNumber; }

    public Transaction.Type getType() { return type; }
    public void setType(Transaction.Type type) { this.type = type; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
