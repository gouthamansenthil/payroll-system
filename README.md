================================================================================
                    EMPLOYEE PAYROLL MANAGEMENT SYSTEM
                    Inspired by SAIL Salem Steel Plant
================================================================================

🌟 OVERVIEW
A comprehensive payroll management system built with Java backend and modern 
web frontend. Handles employee management, salary calculations, payslip 
generation, and payroll analytics.

📋 FEATURES

💼 EMPLOYEE MANAGEMENT
• Add New Employees - Complete employee registration
• Edit Employee Details - Update salary, designation, and personal information
• Delete Employees - Remove employees from payroll
• View All Employees - Complete employee directory
• Search by Designation - Filter employees by job role

💰 PAYROLL PROCESSING
• Automatic Salary Calculation - Net salary = Basic + HRA - Tax
• Payslip Generation - Professional printable payslips
• Tax Deduction Management - Flexible tax calculations
• HRA Calculations - House Rent Allowance management

📊 ANALYTICS & REPORTS
• Dashboard Overview - Key payroll metrics
• Total Payroll Expense - Monthly payroll summary
• Designation Distribution - Employee role analytics
• Salary Statistics - Average and total salary reports

🛠️ TECHNOLOGY STACK

BACKEND
• Java - Core application logic
• MySQL - Database management
• JDBC - Database connectivity
• HTTP Server - REST API endpoints

FRONTEND
• HTML5 - Structure and semantics
• CSS3 - Styling and responsive design
• JavaScript - Dynamic functionality
• Font Awesome - Icons and UI elements

📁 PROJECT STRUCTURE
payroll-system/
├── backend/
│   ├── Employee.java              # Employee model class
│   ├── PayrollService.java        # Business logic layer
│   ├── DatabaseConnection.java    # Database configuration
│   ├── HttpServer.java           # REST API server
│   ├── Main.java                 # Console application
│   └── mysql-connector-j-9.4.0.jar
├── frontend/
│   ├── index.html                # Main application
│   ├── styles.css                # Styling
│   ├── script.js                 # Frontend logic
│   └── assets/                   # Images and resources
└── database/
    └── payroll_db_setup.sql      # Database schema

🚀 QUICK START

PREREQUISITES
• Java JDK 8 or higher
• MySQL Server 5.7+
• Modern web browser

STEP 1: DATABASE SETUP
1. Open MySQL command line:
   mysql -u root -p

2. Run these SQL commands:
   CREATE DATABASE employee_payroll_system;
   USE employee_payroll_system;
   
   -- Then run the entire payroll_db_setup.sql file

STEP 2: CONFIGURE DATABASE
Edit backend/DatabaseConnection.java with your MySQL credentials:
private static final String URL = "jdbc:mysql://localhost:3306/employee_payroll_system";
private static final String USERNAME = "your_username";
private static final String PASSWORD = "your_password";

STEP 3: COMPILE AND RUN
# Navigate to backend folder
cd backend

javac -cp ".;mysql-connector-j-9.4.0.jar" *.java

java -cp ".;mysql-connector-j-9.4.0.jar" PayrollHttpServer

5. **Access Application**
Open frontend/index.html in your browser.

## 📁 Project Structure
payroll-system/

├── backend/          # Java server & database

├── frontend/         # Web interface

└── database/         # SQL scripts

## 🔌 API Endpoints
- GET    /employees - Get all employees
- POST   /employees - Add new employee
- GET    /employees/{id} - Get specific employee
- PUT    /employees/{id} - Update employee
- DELETE /employees/{id} - Delete employee
- GET    /employees/search - Search by designation
4. **Access Application**
Open frontend/index.html in your browser.

## 📁 Project Structure
payroll-system/
├── backend/          # Java server & database
├── frontend/         # Web interface
└── database/         # SQL scripts

## 🔌 API Endpoints
- GET    /employees - Get all employees
- POST   /employees - Add new employee
- GET    /employees/{id} - Get specific employee
- PUT    /employees/{id} - Update employee
- DELETE /employees/{id} - Delete employee
- GET    /employees/search - Search by designation

## 🎯 Sample Data
Comes pre-loaded with 10 employees across various roles:
- Senior Engineers, HR Managers, Production Supervisors
- Safety Officers, IT Specialists, Finance Controllers
- Quality Analysts, Operations Managers, Marketing Executives

## 🐛 Troubleshooting

**Database Connection Issues:**
- Verify MySQL service is running
- Check credentials in DatabaseConnection.java
- Ensure database exists
## 🎯 Sample Data
Comes pre-loaded with 10 employees across various roles:
- Senior Engineers, HR Managers, Production Supervisors
- Safety Officers, IT Specialists, Finance Controllers
- Quality Analysts, Operations Managers, Marketing Executives

## 🐛 Troubleshooting

**Database Connection Issues:**
- Verify MySQL service is running
- Check credentials in DatabaseConnection.java
- Ensure database exists

**Frontend Issues:**
- Backend server must be running on port 8080
- Check browser console for errors (F12)
- Open via HTTP server for full functionality

## 📄 License
Educational & Demonstration Purpose

---
**Start managing your payroll efficiently today!** 🚀

**Frontend Issues:**
- Backend server must be running on port 8080
- Check browser console for errors (F12)
- Open via HTTP server for full functionality

## 📄 License
Educational & Demonstration Purpose

---
**Start managing your payroll efficiently today!** 🚀