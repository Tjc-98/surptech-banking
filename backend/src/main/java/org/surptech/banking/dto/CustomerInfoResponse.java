package org.surptech.banking.dto;

import java.util.List;

/**
 * Response object combining customer personal, credit, and transaction information.
 */
public class CustomerInfoResponse {

    // Customer profile fields
    private String socialSecurityNumber;
    private String firstName;
    private String lastName;
    private String address;

    // Credit profile fields
    private double fullCreditBalance;
    private double spendBalance;
    private double availableBalance;
    private double interestRate;

    // Transaction history
    private List<TransactionResponse> transactions;

    public CustomerInfoResponse() {}

    public String getSocialSecurityNumber() { return socialSecurityNumber; }
    public void setSocialSecurityNumber(String socialSecurityNumber) { this.socialSecurityNumber = socialSecurityNumber; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public double getFullCreditBalance() { return fullCreditBalance; }
    public void setFullCreditBalance(double fullCreditBalance) { this.fullCreditBalance = fullCreditBalance; }

    public double getSpendBalance() { return spendBalance; }
    public void setSpendBalance(double spendBalance) { this.spendBalance = spendBalance; }

    public double getAvailableBalance() { return availableBalance; }
    public void setAvailableBalance(double availableBalance) { this.availableBalance = availableBalance; }

    public double getInterestRate() { return interestRate; }
    public void setInterestRate(double interestRate) { this.interestRate = interestRate; }

    public List<TransactionResponse> getTransactions() { return transactions; }
    public void setTransactions(List<TransactionResponse> transactions) { this.transactions = transactions; }
}
