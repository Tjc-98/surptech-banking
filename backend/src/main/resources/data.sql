-- Seed customer profiles
INSERT INTO customer_profile (social_security_number, first_name, last_name, address)
VALUES ('123-45-6789', 'James', 'Smith', '456 Tailor Street, California, LA 56001')
ON CONFLICT (social_security_number) DO NOTHING;

INSERT INTO customer_profile (social_security_number, first_name, last_name, address)
VALUES ('987-65-4321', 'John', 'Travolta', '123 West Street, New York, NY 875423')
ON CONFLICT (social_security_number) DO NOTHING;

-- Seed credit profiles
INSERT INTO credit_profile (social_security_number, full_credit_balance, spend_balance, interest_rate)
VALUES ('123-45-6789', 15000.00, 5000.00, 3.5)
ON CONFLICT (social_security_number) DO NOTHING;

INSERT INTO credit_profile (social_security_number, full_credit_balance, spend_balance, interest_rate)
VALUES ('987-65-4321', 28000.00, 12000.00, 8.5)
ON CONFLICT (social_security_number) DO NOTHING;

-- Seed transactions
INSERT INTO transaction (social_security_number, type, amount, description, created_at)
VALUES ('123-45-6789', 'DEPOSIT', 2000.00, 'Salary payment', '2026-05-01 09:00:00')
ON CONFLICT DO NOTHING;

INSERT INTO transaction (social_security_number, type, amount, description, created_at)
VALUES ('123-45-6789', 'WITHDRAWAL', 500.00, 'Grocery shopping', '2026-05-05 14:30:00')
ON CONFLICT DO NOTHING;

INSERT INTO transaction (social_security_number, type, amount, description, created_at)
VALUES ('123-45-6789', 'WITHDRAWAL', 200.00, 'Utility bill', '2026-05-10 11:00:00')
ON CONFLICT DO NOTHING;

INSERT INTO transaction (social_security_number, type, amount, description, created_at)
VALUES ('987-65-4321', 'DEPOSIT', 5000.00, 'Freelance project', '2026-05-03 10:00:00')
ON CONFLICT DO NOTHING;

INSERT INTO transaction (social_security_number, type, amount, description, created_at)
VALUES ('987-65-4321', 'WITHDRAWAL', 1200.00, 'Rent payment', '2026-05-08 09:00:00')
ON CONFLICT DO NOTHING;
