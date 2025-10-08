import java.util.List;
import java.util.Scanner;

/**
 * Main Application Class
 * Menu-driven console application for Employee Payroll Management System
 * Inspired by SAIL Salem Steel Plant industrial payroll operations
 * 
 * This system simulates how HR staff and payroll teams manage employee
 * compensation data in a real manufacturing environment.
 */
public class Main {
    
    private static Scanner scanner = new Scanner(System.in);
    private static PayrollService payrollService = new PayrollService();
    
    public static void main(String[] args) {
        // Display welcome banner
        displayWelcomeBanner();
        
        // Test database connection
        if (!DatabaseConnection.testConnection()) {
            System.err.println("\n⚠ Unable to connect to database. Please check your configuration.");
            System.err.println("Ensure MySQL server is running and credentials in DatabaseConnection.java are correct.\n");
            return;
        }
        
        // Main application loop
        boolean running = true;
        while (running) {
            displayMenu();
            int choice = getIntInput("Enter your choice: ");
            
            System.out.println(); // Blank line for readability
            
            switch (choice) {
                case 1:
                    addNewEmployee();
                    break;
                case 2:
                    updateEmployeeDetails();
                    break;
                case 3:
                    deleteEmployee();
                    break;
                case 4:
                    viewAllEmployees();
                    break;
                case 5:
                    viewEmployeeById();
                    break;
                case 6:
                    generateEmployeePayslip();
                    break;
                case 7:
                    searchByDesignation();
                    break;
                case 8:
                    calculateTotalPayroll();
                    break;
                case 9:
                    running = false;
                    exitApplication();
                    break;
                default:
                    System.out.println("✗ Invalid choice! Please select between 1-9.\n");
            }
            
            // Pause before showing menu again (except on exit)
            if (running && choice >= 1 && choice <= 8) {
                System.out.print("\nPress Enter to continue...");
                scanner.nextLine();
            }
        }
    }
    
    /**
     * Displays welcome banner
     */
    private static void displayWelcomeBanner() {
        System.out.println("\n╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║                                                                ║");
        System.out.println("║      EMPLOYEE PAYROLL MANAGEMENT SYSTEM                        ║");
        System.out.println("║      SAIL Salem Steel Plant - Digital Solution                 ║");
        System.out.println("║                                                                ║");
        System.out.println("║      Streamlining Industrial Workforce Management              ║");
        System.out.println("║                                                                ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝\n");
    }
    
    /**
     * Displays main menu
     */
    private static void displayMenu() {
        System.out.println("\n╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║                      MAIN MENU                                 ║");
        System.out.println("╠════════════════════════════════════════════════════════════════╣");
        System.out.println("║  1. Add New Employee                                           ║");
        System.out.println("║  2. Update Employee Details                                    ║");
        System.out.println("║  3. Delete Employee                                            ║");
        System.out.println("║  4. View All Employees                                         ║");
        System.out.println("║  5. View Employee by ID                                        ║");
        System.out.println("║  6. Generate Employee Payslip                                  ║");
        System.out.println("║  7. Search by Designation                                      ║");
        System.out.println("║  8. Calculate Total Payroll Expense                            ║");
        System.out.println("║  9. Exit                                                       ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝");
    }
    
    /**
     * Option 4: View all employees
     */
    private static void viewAllEmployees() {
        System.out.println("══════════ ALL EMPLOYEES ══════════\n");
        payrollService.displayAllEmployees();
    }
    
    /**
     * Option 5: View employee by ID
     */
    private static void viewEmployeeById() {
        System.out.println("══════════ VIEW EMPLOYEE BY ID ══════════\n");
        
        int empId = getIntInput("Enter Employee ID: ");
        Employee employee = payrollService.getEmployeeById(empId);
        
        if (employee != null) {
            System.out.println("\n" + employee);
        }
    }
    
    /**
     * Option 6: Generate payslip for an employee
     */
    private static void generateEmployeePayslip() {
        System.out.println("══════════ GENERATE PAYSLIP ══════════\n");
        
        int empId = getIntInput("Enter Employee ID: ");
        payrollService.generatePayslip(empId);
    }
    
    /**
     * Option 7: Search employees by designation
     */
    private static void searchByDesignation() {
        System.out.println("══════════ SEARCH BY DESIGNATION ══════════\n");
        
        scanner.nextLine(); // Clear buffer
        System.out.print("Enter Designation to search: ");
        String designation = scanner.nextLine();
        
        List<Employee> employees = payrollService.searchByDesignation(designation);
        
        if (!employees.isEmpty()) {
            System.out.println("\n📋 Search Results:\n");
            for (Employee emp : employees) {
                System.out.println(emp);
            }
        }
    }
    
    /**
     * Option 8: Calculate total payroll expense
     */
    private static void calculateTotalPayroll() {
        System.out.println("══════════ TOTAL PAYROLL EXPENSE ══════════\n");
        payrollService.calculateTotalPayrollExpense();
    }
    
    /**
     * Option 9: Exit application
     */
    private static void exitApplication() {
        System.out.println("\n╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║                                                                ║");
        System.out.println("║           Thank you for using the Payroll System!              ║");
        System.out.println("║                                                                ║");
        System.out.println("║        SAIL Salem Steel Plant - Workforce Management           ║");
        System.out.println("║                                                                ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝\n");
        
        // Close database connection
        DatabaseConnection.closeConnection();
        scanner.close();
    }
    
    /**
     * Helper method to safely get integer input
     * @param prompt Message to display
     * @return Integer value entered by user
     */
    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return scanner.nextInt();
            } catch (Exception e) {
                System.out.println("✗ Invalid input! Please enter a valid number.");
                scanner.nextLine(); // Clear invalid input
            }
        }
    }
    
    /**
     * Helper method to safely get double input
     * @param prompt Message to display
     * @return Double value entered by user
     */
    private static double getDoubleInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return scanner.nextDouble();
            } catch (Exception e) {
                System.out.println("✗ Invalid input! Please enter a valid number.");
                scanner.nextLine(); // Clear invalid input
            }
        }
    }

     
    private static void addNewEmployee() {
    System.out.println("══════════ ADD NEW EMPLOYEE ══════════\n");
    
    scanner.nextLine(); // Clear buffer before taking string input
    
    System.out.print("Enter Employee Name: ");
    String name = scanner.nextLine();
    
    System.out.print("Enter Designation: ");
    String designation = scanner.nextLine();
    
    double basicSalary = getDoubleInput("Enter Basic Salary (₹): ");
    scanner.nextLine(); // Consume leftover newline
    
    double hra = getDoubleInput("Enter HRA (₹): ");
    scanner.nextLine(); // Consume leftover newline
    
    double tax = getDoubleInput("Enter Tax Deduction (₹): ");
    scanner.nextLine(); // Consume leftover newline
    
    // Create employee object
    Employee employee = new Employee(name, designation, basicSalary, hra, tax);
    
    // Display calculated net salary
    System.out.printf("\n💼 Calculated Net Salary: ₹%.2f\n", employee.calculateNetSalary());
    
    // Confirm and add
    System.out.print("\nConfirm adding this employee? (Y/N): ");
    String confirm = scanner.nextLine();
    
    if (confirm.equalsIgnoreCase("Y")) {
        payrollService.addEmployee(employee);
    } else {
        System.out.println("✗ Operation cancelled.");
    }
}

    
    /**
     * Option 2: Update existing employee details
     */
    private static void updateEmployeeDetails() {
        System.out.println("══════════ UPDATE EMPLOYEE DETAILS ══════════\n");
        
        int empId = getIntInput("Enter Employee ID to update: ");
        
        // Check if employee exists
        Employee employee = payrollService.getEmployeeById(empId);
        
        if (employee == null) {
            return;
        }
        
        // Display current details
        System.out.println("\nCurrent Details:");
        System.out.println(employee);
        
        scanner.nextLine(); // Clear buffer
        
        // Get updated details
        System.out.print("\nEnter New Name (or press Enter to keep current): ");
        String name = scanner.nextLine();
        if (!name.trim().isEmpty()) {
            employee.setName(name);
        }
        
        System.out.print("Enter New Designation (or press Enter to keep current): ");
        String designation = scanner.nextLine();
        if (!designation.trim().isEmpty()) {
            employee.setDesignation(designation);
        }
        
        System.out.print("Enter New Basic Salary (or 0 to keep current): ");
        double basicSalary = scanner.nextDouble();
        if (basicSalary > 0) {
            employee.setBasicSalary(basicSalary);
        }
        
        System.out.print("Enter New HRA (or 0 to keep current): ");
        double hra = scanner.nextDouble();
        if (hra > 0) {
            employee.setHra(hra);
        }
        
        System.out.print("Enter New Tax (or 0 to keep current): ");
        double tax = scanner.nextDouble();
        if (tax > 0) {
            employee.setTax(tax);
        }
        
        scanner.nextLine(); // Clear buffer
        
        // Update in database
        payrollService.updateEmployee(employee);
    }
    
    /**
     * Option 3: Delete employee from payroll
     */
    private static void deleteEmployee() {
        System.out.println("══════════ DELETE EMPLOYEE ══════════\n");
        
        int empId = getIntInput("Enter Employee ID to delete: ");
        
        // Verify employee exists
        Employee employee = payrollService.getEmployeeById(empId);
        
        if (employee != null) {
            System.out.println("\nEmployee Details:");
            System.out.println(employee);
            
            scanner.nextLine(); // Clear buffer
            System.out.print("\n⚠ Are you sure you want to delete this employee? (Y/N): ");
            String confirm = scanner.nextLine();
            
            if (confirm.equalsIgnoreCase("Y")) {
                payrollService.deleteEmployee(empId);
            } else {
                System.out.println("✗ Deletion cancelled.");
            }
        }
    }
}
    
