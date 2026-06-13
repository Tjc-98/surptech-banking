package org.surptech.banking.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "transaction")
public class Transaction {

    public enum Type { DEPOSIT, WITHDRAWAL }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "social_security_number", nullable = false)
    private CustomerProfile customer;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Type type;

    @Column(nullable = false)
    private double amount;

    @Column
    private String description;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public Transaction() {}

    public Transaction(CustomerProfile customer, Type type, double amount, String description) {
        this.customer = customer;
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }

    public CustomerProfile getCustomer() { return customer; }
    public void setCustomer(CustomerProfile customer) { this.customer = customer; }

    public Type getType() { return type; }
    public void setType(Type type) { this.type = type; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
