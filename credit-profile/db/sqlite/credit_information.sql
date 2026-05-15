-- Credit Information Table DDL for SQLite
-- This table stores customer credit profile information

CREATE TABLE IF NOT EXISTS credit_profile (
    social_security_number TEXT PRIMARY KEY NOT NULL,
    full_credit_balance REAL NOT NULL,
    spend_balance REAL NOT NULL,
    interest_rate REAL NOT NULL
);

-- Create index on full_credit_balance for faster searches
CREATE INDEX IF NOT EXISTS idx_credit_full_credit_balance 
ON credit_profile(full_credit_balance);

-- Insert dummy data for testing (matching SSN with customer_profile)
INSERT OR IGNORE INTO credit_profile (social_security_number, full_credit_balance, spend_balance, interest_rate) 
VALUES ('123-45-6789', 15000.00, 5000.00, 3.5);
