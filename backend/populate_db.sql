USE employee_payroll_system;
DROP TABLE IF EXISTS employee_payroll;
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
INSERT INTO employee_payroll (name, designation, basic_salary, hra, tax, net_salary) VALUES
('Rajesh Kumar', 'Senior Engineer', 50000.00, 15000.00, 8000.00, 57000.00),
('Priya Sharma', 'HR Manager', 45000.00, 13500.00, 7000.00, 51500.00),
('Amit Singh', 'Production Supervisor', 38000.00, 11400.00, 5500.00, 43900.00),
('Sneha Reddy', 'Safety Officer', 35000.00, 10500.00, 5000.00, 40500.00),
('Vikram Patel', 'Maintenance Technician', 28000.00, 8400.00, 3500.00, 32900.00);