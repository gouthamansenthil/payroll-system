import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Payroll Service Class
 * Handles all CRUD operations for employee payroll management
 * Inspired by real-world payroll processing at SAIL Salem Steel Plant
 */
public class PayrollService {
    
    private Connection connection;
    
    // Constructor initializes database connection
    public PayrollService() {
        this.connection = DatabaseConnection.getConnection();
    }
    
    /**
     * Adds a new employee to the payroll system
     * @param employee Employee object to be added
     * @return true if employee added successfully, false otherwise
     */
    public boolean addEmployee(Employee employee) {
        String query = "INSERT INTO employee_payroll (name, designation, basic_salary, hra, tax, net_salary) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            // Calculate net salary before inserting
            double netSalary = employee.calculateNetSalary();
            
            // Set parameters
            pstmt.setString(1, employee.getName());
            pstmt.setString(2, employee.getDesignation());
            pstmt.setDouble(3, employee.getBasicSalary());
            pstmt.setDouble(4, employee.getHra());
            pstmt.setDouble(5, employee.getTax());
            pstmt.setDouble(6, netSalary);
            
            // Execute update
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("✓ Employee added successfully to payroll system!");
                return true;
            }
        } catch (SQLException e) {
            System.err.println("✗ Error adding employee to database!");
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Updates existing employee details
     * @param employee Employee object with updated information
     * @return true if update successful, false otherwise
     */
    public boolean updateEmployee(Employee employee) {
        String query = "UPDATE employee_payroll SET name=?, designation=?, basic_salary=?, hra=?, tax=?, net_salary=? WHERE id=?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            // Calculate net salary before updating
            double netSalary = employee.calculateNetSalary();
            
            // Set parameters
            pstmt.setString(1, employee.getName());
            pstmt.setString(2, employee.getDesignation());
            pstmt.setDouble(3, employee.getBasicSalary());
            pstmt.setDouble(4, employee.getHra());
            pstmt.setDouble(5, employee.getTax());
            pstmt.setDouble(6, netSalary);
            pstmt.setInt(7, employee.getId());
            
            // Execute update
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("✓ Employee details updated successfully!");
                return true;
            } else {
                System.out.println("✗ Employee ID not found in database!");
            }
        } catch (SQLException e) {
            System.err.println("✗ Error updating employee details!");
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Deletes an employee from the payroll system
     * @param employeeId ID of the employee to be deleted
     * @return true if deletion successful, false otherwise
     */
    public boolean deleteEmployee(int employeeId) {
        String query = "DELETE FROM employee_payroll WHERE id=?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, employeeId);
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("✓ Employee removed from payroll system!");
                return true;
            } else {
                System.out.println("✗ Employee ID not found in database!");
            }
        } catch (SQLException e) {
            System.err.println("✗ Error deleting employee from database!");
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Retrieves a single employee by ID
     * @param employeeId ID of the employee
     * @return Employee object if found, null otherwise
     */
    public Employee getEmployeeById(int employeeId) {
        String query = "SELECT * FROM employee_payroll WHERE id=?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, employeeId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractEmployeeFromResultSet(rs);
            } else {
                System.out.println("✗ Employee ID not found!");
            }
        } catch (SQLException e) {
            System.err.println("✗ Error fetching employee details!");
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Retrieves all employees from the payroll system
     * @return List of all employees
     */
    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        String query = "SELECT * FROM employee_payroll ORDER BY id";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                employees.add(extractEmployeeFromResultSet(rs));
            }
            
            if (employees.isEmpty()) {
                System.out.println("ℹ No employees found in the payroll system.");
            }
        } catch (SQLException e) {
            System.err.println("✗ Error fetching employee list!");
            e.printStackTrace();
        }
        return employees;
    }
    
    /**
     * Displays all employees in tabular format
     */
    public void displayAllEmployees() {
        List<Employee> employees = getAllEmployees();
        
        if (employees.isEmpty()) {
            return;
        }
        
        System.out.println("\n╔════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                                        EMPLOYEE PAYROLL RECORDS                                                    ║");
        System.out.println("╠════╦═══════════════════════╦═══════════════════════╦═══════════════╦═══════════════╦═══════════════╦═══════════════╣");
        System.out.println("║ ID ║       Name            ║     Designation       ║ Basic Salary  ║      HRA      ║      Tax      ║  Net Salary   ║");
        System.out.println("╠════╬═══════════════════════╬═══════════════════════╬═══════════════╬═══════════════╬═══════════════╬═══════════════╣");
        
        for (Employee emp : employees) {
            System.out.printf("║ %-2d ║ %-21s ║ %-21s ║ ₹%-12.2f ║ ₹%-12.2f ║ ₹%-12.2f ║ ₹%-12.2f ║%n",
                emp.getId(),
                emp.getName(),
                emp.getDesignation(),
                emp.getBasicSalary(),
                emp.getHra(),
                emp.getTax(),
                emp.getNetSalary()
            );
        }
        
        System.out.println("╚════╩═══════════════════════╩═══════════════════════╩═══════════════╩═══════════════╩═══════════════╩═══════════════╝\n");
    }
    
    /**
     * Generates and displays payslip for a specific employee
     * @param employeeId ID of the employee
     */
    public void generatePayslip(int employeeId) {
        Employee employee = getEmployeeById(employeeId);
        
        if (employee != null) {
            employee.displayPayslip();
        }
    }
    
    /**
     * Searches employees by designation
     * @param designation Designation to search for
     * @return List of employees with matching designation
     */
    public List<Employee> searchByDesignation(String designation) {
        List<Employee> employees = new ArrayList<>();
        String query = "SELECT * FROM employee_payroll WHERE designation LIKE ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, "%" + designation + "%");
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                employees.add(extractEmployeeFromResultSet(rs));
            }
            
            if (employees.isEmpty()) {
                System.out.println("ℹ No employees found with designation: " + designation);
            }
        } catch (SQLException e) {
            System.err.println("✗ Error searching employees!");
            e.printStackTrace();
        }
        return employees;
    }
    
    /**
     * Helper method to extract Employee object from ResultSet
     * @param rs ResultSet from database query
     * @return Employee object
     * @throws SQLException if database access error occurs
     */
    private Employee extractEmployeeFromResultSet(ResultSet rs) throws SQLException {
        return new Employee(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getString("designation"),
            rs.getDouble("basic_salary"),
            rs.getDouble("hra"),
            rs.getDouble("tax"),
            rs.getDouble("net_salary")
        );
    }
    
    /**
     * Calculates total payroll expense (for management reporting)
     * @return Total net salary of all employees
     */
    public double calculateTotalPayrollExpense() {
        String query = "SELECT SUM(net_salary) as total FROM employee_payroll";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            if (rs.next()) {
                double total = rs.getDouble("total");
                System.out.printf("💰 Total Monthly Payroll Expense: ₹%.2f%n", total);
                return total;
            }
        } catch (SQLException e) {
            System.err.println("✗ Error calculating payroll expense!");
            e.printStackTrace();
        }
        return 0.0;
    }
}