import java.util.*;
import java.io.*;
import java.time.LocalDate;

// Employee Class - Represents an employee
class Employee {
    private int id;
    private String name;
    private String department;
    private double baseSalary;
    private double bonus;
    
    public Employee(int id, String name, String department, double baseSalary, double bonus) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.baseSalary = baseSalary;
        this.bonus = bonus;
    }
    
    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getDepartment() { return department; }
    public double getBaseSalary() { return baseSalary; }
    public double getBonus() { return bonus; }
    
    // Calculate total salary
    public double calculateTotalSalary() {
        return baseSalary + bonus;
    }
    
    // Calculate tax (10% for salary > 50000, else 5%)
    public double calculateTax() {
        double total = calculateTotalSalary();
        return total > 50000 ? total * 0.10 : total * 0.05;
    }
    
    // Net salary after tax
    public double getNetSalary() {
        return calculateTotalSalary() - calculateTax();
    }
    
    @Override
    public String toString() {
        return String.format("ID: %d | Name: %s | Dept: %s | Base: %.2f | Bonus: %.2f | Net: %.2f", 
                            id, name, department, baseSalary, bonus, getNetSalary());
    }
}

// PayrollSystem - Manages employees and payroll operations
class PayrollManager {
    private List<Employee> employees;
    private int nextId;
    
    public PayrollManager() {
        employees = new ArrayList<>();
        nextId = 1;
        loadSampleData();
    }
    
    // Load sample data
    private void loadSampleData() {
        addEmployee("John Doe", "IT", 60000, 5000);
        addEmployee("Jane Smith", "HR", 45000, 3000);
        addEmployee("Mike Johnson", "Finance", 55000, 4000);
        addEmployee("Sarah Williams", "IT", 48000, 2000);
    }
    
    // Add new employee
    public void addEmployee(String name, String dept, double salary, double bonus) {
        Employee emp = new Employee(nextId++, name, dept, salary, bonus);
        employees.add(emp);
        System.out.println("✓ Employee added successfully!");
    }
    
    // View all employees
    public void viewAllEmployees() {
        if (employees.isEmpty()) {
            System.out.println("No employees found.");
            return;
        }
        
        System.out.println("\n" + "=".repeat(80));
        System.out.println("EMPLOYEE PAYROLL REPORT - " + LocalDate.now());
        System.out.println("=".repeat(80));
        
        for (Employee emp : employees) {
            System.out.println(emp);
        }
        
        System.out.println("=".repeat(80));
        System.out.printf("Total Employees: %d | Total Payroll: %.2f\n", 
                         employees.size(), calculateTotalPayroll());
        System.out.println("=".repeat(80) + "\n");
    }
    
    // Search employee by ID
    public Employee findEmployee(int id) {
        for (Employee emp : employees) {
            if (emp.getId() == id) {
                return emp;
            }
        }
        return null;
    }
    
    // View employee details
    public void viewEmployeeDetails(int id) {
        Employee emp = findEmployee(id);
        if (emp == null) {
            System.out.println("✗ Employee not found!");
            return;
        }
        
        System.out.println("\n" + "-".repeat(60));
        System.out.println("EMPLOYEE PAYSLIP");
        System.out.println("-".repeat(60));
        System.out.println("Employee ID      : " + emp.getId());
        System.out.println("Name             : " + emp.getName());
        System.out.println("Department       : " + emp.getDepartment());
        System.out.println("Base Salary      : $" + String.format("%.2f", emp.getBaseSalary()));
        System.out.println("Bonus            : $" + String.format("%.2f", emp.getBonus()));
        System.out.println("-".repeat(60));
        System.out.println("Gross Salary     : $" + String.format("%.2f", emp.calculateTotalSalary()));
        System.out.println("Tax Deduction    : $" + String.format("%.2f", emp.calculateTax()));
        System.out.println("-".repeat(60));
        System.out.println("NET SALARY       : $" + String.format("%.2f", emp.getNetSalary()));
        System.out.println("-".repeat(60) + "\n");
    }
    
    // Delete employee
    public void deleteEmployee(int id) {
        Employee emp = findEmployee(id);
        if (emp == null) {
            System.out.println("✗ Employee not found!");
            return;
        }
        employees.remove(emp);
        System.out.println("✓ Employee deleted successfully!");
    }
    
    // Calculate total payroll
    public double calculateTotalPayroll() {
        return employees.stream()
                       .mapToDouble(Employee::getNetSalary)
                       .sum();
    }
    
    // Export report to file
    public void exportReport() {
        try (PrintWriter writer = new PrintWriter("payroll_report.txt")) {
            writer.println("PAYROLL REPORT - " + LocalDate.now());
            writer.println("=".repeat(80));
            
            for (Employee emp : employees) {
                writer.println(emp);
            }
            
            writer.println("=".repeat(80));
            writer.printf("Total Payroll: %.2f\n", calculateTotalPayroll());
            
            System.out.println("✓ Report exported to 'payroll_report.txt'");
        } catch (IOException e) {
            System.out.println("✗ Error exporting report: " + e.getMessage());
        }
    }
}

// Main Application
public class PayrollSystem {
    public static void main(String[] args) {
        PayrollManager manager = new PayrollManager();
        Scanner sc = new Scanner(System.in);
        
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║   EMPLOYEE PAYROLL MANAGEMENT SYSTEM   ║");
        System.out.println("╚════════════════════════════════════════╝\n");
        
        while (true) {
            System.out.println("┌─────────────────────────────┐");
            System.out.println("│ 1. View All Employees       │");
            System.out.println("│ 2. Add New Employee         │");
            System.out.println("│ 3. View Employee Payslip    │");
            System.out.println("│ 4. Delete Employee          │");
            System.out.println("│ 5. Export Payroll Report    │");
            System.out.println("│ 6. Exit                     │");
            System.out.println("└─────────────────────────────┘");
            System.out.print("Enter your choice: ");
            
            int choice = sc.nextInt();
            sc.nextLine(); // consume newline
            
            switch (choice) {
                case 1:
                    manager.viewAllEmployees();
                    break;
                    
                case 2:
                    System.out.print("Enter Name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter Department: ");
                    String dept = sc.nextLine();
                    System.out.print("Enter Base Salary: ");
                    double salary = sc.nextDouble();
                    System.out.print("Enter Bonus: ");
                    double bonus = sc.nextDouble();
                    manager.addEmployee(name, dept, salary, bonus);
                    break;
                    
                case 3:
                    System.out.print("Enter Employee ID: ");
                    int viewId = sc.nextInt();
                    manager.viewEmployeeDetails(viewId);
                    break;
                    
                case 4:
                    System.out.print("Enter Employee ID to delete: ");
                    int delId = sc.nextInt();
                    manager.deleteEmployee(delId);
                    break;
                    
                case 5:
                    manager.exportReport();
                    break;
                    
                case 6:
                    System.out.println("\nThank you for using Payroll System!");
                    sc.close();
                    System.exit(0);
                    
                default:
                    System.out.println("✗ Invalid choice! Please try again.\n");
            }
        }
    }
}