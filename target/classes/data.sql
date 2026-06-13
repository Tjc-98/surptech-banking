-- Seed customer profiles
INSERT INTO customer_profile (social_security_number, first_name, last_name, address)
VALUES ('123-45-6789', 'James', 'Smith', '456 Tailor Street, California, LA 56001');

INSERT INTO customer_profile (social_security_number, first_name, last_name, address)
VALUES ('987-65-4321', 'John', 'Travolta', '123 West Street, New York, NY 875423');

-- Seed credit profiles
INSERT INTO credit_profile (social_security_number, full_credit_balance, spend_balance, interest_rate)
VALUES ('123-45-6789', 15000.00, 5000.00, 3.5);

INSERT INTO credit_profile (social_security_number, full_credit_balance, spend_balance, interest_rate)
VALUES ('987-65-4321', 28000.00, 12000.00, 8.5);
