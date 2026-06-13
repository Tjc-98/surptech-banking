package org.surptech.banking.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

// Holds the basic personal details for a customer.
@Entity
@Table(name = "customer_profile")
public class CustomerProfile {

    @Id
    @Column(name = "social_security_number")
    private String socialSecurityNumber;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "address", nullable = false)
    private String address;

    public CustomerProfile() {}

    public CustomerProfile(String socialSecurityNumber, String firstName, String lastName, String address) {
        this.socialSecurityNumber = socialSecurityNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
    }

    public String getSocialSecurityNumber() { return socialSecurityNumber; }
    public void setSocialSecurityNumber(String socialSecurityNumber) { this.socialSecurityNumber = socialSecurityNumber; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
}
