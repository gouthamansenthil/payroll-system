-- Employee Payroll Management System - Database Setup Script
-- Inspired by SAIL Salem Steel Plant Industrial Payroll System

-- Create database
CREATE DATABASE IF NOT EXISTS employee_payroll_system;
USE employee_payroll_system;

-- Drop table if exists (for fresh setup)
DROP TABLE IF EXISTS employee_payroll;

-- Create employee_payroll table
CREATE TABLE employee_payroll (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    designation VARCHAR(50) NOT NULL,
    basic_salary DECIMAL(10, 2) NOT NULL,
    hra DECIMAL(10, 2) NOT NULL,
    tax DECIMAL(10, 2) NOT NULL,
    net_salary DECIMAL(10, 2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Insert sample data (inspired by typical SAIL employee structure)
INSERT INTO employee_payroll (name, designation, basic_salary, hra, tax, net_salary) VALUES
('Rajesh Kumar', 'Senior Engineer', 50000.00, 15000.00, 8000.00, 57000.00),
('Priya Sharma', 'HR Manager', 45000.00, 13500.00, 7000.00, 51500.00),
('Amit Singh', 'Production Supervisor', 38000.00, 11400.00, 5500.00, 43900.00),
('Sneha Reddy', 'Safety Officer', 35000.00, 10500.00, 5000.00, 40500.00),
('Vikram Patel', 'Maintenance Technician', 28000.00, 8400.00, 3500.00, 32900.00);

-- Display all records
SELECT * FROM employee_payroll;

-- Useful queries for payroll management
-- View employees with net salary above a threshold
-- SELECT * FROM employee_payroll WHERE net_salary > 40000;

-- View employees by designation
-- SELECT * FROM employee_payroll WHERE designation LIKE '%Engineer%';