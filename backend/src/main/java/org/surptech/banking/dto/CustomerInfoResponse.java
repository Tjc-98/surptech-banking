package org.surptech.banking.dto;

import java.util.List;

// Everything the frontend needs about a customer in one object -
// their personal details, credit account, and transaction history.
public class CustomerInfoResponse {

    private String socialSecurityNumber;
    private String firstName;
    private String lastName;
    private String address;

    private double fullCreditBalance;  // total credit limit
    private double spendBalance;       // how much has been used
    private double availableBalance;   // limit minus spend
    private double interestRate;

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
