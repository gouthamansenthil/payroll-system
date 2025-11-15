# 🏢 Employee Payroll Management System

A complete full-stack payroll management system built with Java backend and modern web frontend. Inspired by industrial payroll operations at SAIL Salem Steel Plant.

## ✨ Features

### 👥 Employee Management
- **Add New Employees** - Complete registration with all details
- **Edit Employee Records** - Update information anytime
- **Delete Employees** - Remove from payroll system
- **View All Employees** - Complete directory with search
- **Search by Designation** - Filter by job roles

### 💰 Payroll Processing
- **Automatic Calculations** - Net Salary = Basic + HRA - Tax
- **Professional Payslips** - Printable employee payslips
- **Tax Management** - Flexible tax deductions
- **HRA Calculations** - House Rent Allowance handling

### 📊 Analytics & Reports
- **Live Dashboard** - Real-time payroll statistics
- **Total Payroll Expense** - Monthly cost overview
- **Role Distribution** - Employee designation analytics
- **Salary Reports** - Average and total salary insights

## 🛠️ Tech Stack

**Backend:**
- Java • MySQL • JDBC • HTTP Server

**Frontend:**
- HTML5 • CSS3 • JavaScript • Font Awesome

## 🚀 Quick Start

### Prerequisites
- Java JDK 8+
- MySQL Server 5.7+
- Modern Web Browser

### Installation

1. **Setup Database**
mysql -u root -p
CREATE DATABASE employee_payroll_system;
USE employee_payroll_system;
source payroll_db_setup.sql

2. **Configure Database**
Edit DatabaseConnection.java with your MySQL credentials.

3. **Compile & Run**
cd backend
javac -cp ".;mysql-connector-j-9.4.0.jar" *.java
java -cp ".;mysql-connector-j-9.4.0.jar" PayrollHttpServer

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

**Frontend Issues:**
- Backend server must be running on port 8080
- Check browser console for errors (F12)
- Open via HTTP server for full functionality

## 📄 License
Educational & Demonstration Purpose

---
**Start managing your payroll efficiently today!** 🚀