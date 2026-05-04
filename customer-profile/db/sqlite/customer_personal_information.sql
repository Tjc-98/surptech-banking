-- Customer Personal Information Table DDL for SQLite
-- This table stores customer profile and personal information

CREATE TABLE IF NOT EXISTS customer_profile (
    social_security_number TEXT PRIMARY KEY NOT NULL,
    first_name TEXT NOT NULL,
    last_name TEXT NOT NULL,
    address TEXT NOT NULL
);

-- Create index on last_name for faster searches
CREATE INDEX IF NOT EXISTS idx_customer_last_name 
ON customer_profile(last_name);

