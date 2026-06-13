package org.surptech.banking.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "credit_profile")
public class CreditProfile {

    @Id
    @Column(name = "social_security_number")
    private String socialSecurityNumber;

    @Column(name = "full_credit_balance", nullable = false)
    private double fullCreditBalance;

    @Column(name = "spend_balance", nullable = false)
    private double spendBalance;

    @Column(name = "interest_rate", nullable = false)
    private double interestRate;

    public CreditProfile() {}

    public CreditProfile(String socialSecurityNumber, double fullCreditBalance, double spendBalance, double interestRate) {
        this.socialSecurityNumber = socialSecurityNumber;
        this.fullCreditBalance = fullCreditBalance;
        this.spendBalance = spendBalance;
        this.interestRate = interestRate;
    }

    public String getSocialSecurityNumber() { return socialSecurityNumber; }
    public void setSocialSecurityNumber(String socialSecurityNumber) { this.socialSecurityNumber = socialSecurityNumber; }

    public double getFullCreditBalance() { return fullCreditBalance; }
    public void setFullCreditBalance(double fullCreditBalance) { this.fullCreditBalance = fullCreditBalance; }

    public double getSpendBalance() { return spendBalance; }
    public void setSpendBalance(double spendBalance) { this.spendBalance = spendBalance; }

    public double getInterestRate() { return interestRate; }
    public void setInterestRate(double interestRate) { this.interestRate = interestRate; }
}
