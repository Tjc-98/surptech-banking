package org.surptech.customerprofile.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.surptech.customerprofile.domain.CustomerProfile;

import java.util.List;
import java.util.Optional;

@Repository
public class CustomerProfileRepository {

    private final JdbcTemplate jdbcTemplate;

    public CustomerProfileRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<CustomerProfile> rowMapper = (rs, rowNum) -> 
        CustomerProfile.builder()
            .socialSecurityNumber(rs.getString("social_security_number"))
            .firstName(rs.getString("first_name"))
            .lastName(rs.getString("last_name"))
            .address(rs.getString("address"))
            .build();

    public CustomerProfile save(CustomerProfile customerProfile) {
        // Check if customer already exists by SSN
        Optional<CustomerProfile> existing = findBySocialSecurityNumber(customerProfile.getSocialSecurityNumber());
        
        if (existing.isPresent()) {
            return update(customerProfile);
        } else {
            return insert(customerProfile);
        }
    }

    private CustomerProfile insert(CustomerProfile customerProfile) {
        String sql = "INSERT INTO customer_profile (social_security_number, first_name, last_name, address) VALUES (?, ?, ?, ?)";
        
        jdbcTemplate.update(sql,
            customerProfile.getSocialSecurityNumber(),
            customerProfile.getFirstName(),
            customerProfile.getLastName(),
            customerProfile.getAddress()
        );

        return customerProfile;
    }

    private CustomerProfile update(CustomerProfile customerProfile) {
        String sql = "UPDATE customer_profile SET first_name = ?, last_name = ?, address = ? WHERE social_security_number = ?";
        
        jdbcTemplate.update(sql,
            customerProfile.getFirstName(),
            customerProfile.getLastName(),
            customerProfile.getAddress(),
            customerProfile.getSocialSecurityNumber()
        );
        
        return customerProfile;
    }

    public Optional<CustomerProfile> findBySocialSecurityNumber(String socialSecurityNumber) {
        String sql = "SELECT * FROM customer_profile WHERE social_security_number = ?";
        
        List<CustomerProfile> results = jdbcTemplate.query(sql, rowMapper, socialSecurityNumber);
        
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    public List<CustomerProfile> findAll() {
        String sql = "SELECT * FROM customer_profile";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public void deleteBySocialSecurityNumber(String socialSecurityNumber) {
        String sql = "DELETE FROM customer_profile WHERE social_security_number = ?";
        jdbcTemplate.update(sql, socialSecurityNumber);
    }
}

