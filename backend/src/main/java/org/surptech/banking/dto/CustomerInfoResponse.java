package org.surptech.banking.dto;

/**
 * Response object combining customer personal and credit information.
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
    private double interestRate;

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

    public double getInterestRate() { return interestRate; }
    public void setInterestRate(double interestRate) { this.interestRate = interestRate; }
}
