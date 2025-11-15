/**
 * Employee Model Class
 * Represents an employee record in the payroll system
 * Inspired by SAIL Salem Steel Plant employee structure
 */
public class Employee {
    private int id;
    private String name;
    private String designation;
    private double basicSalary;
    private double hra;  // House Rent Allowance
    private double tax;
    private double netSalary;

    // Default Constructor
    public Employee() {
    }

    // Parameterized Constructor (without id - for new employees)
    public Employee(String name, String designation, double basicSalary, double hra, double tax) {
        this.name = name;
        this.designation = designation;
        this.basicSalary = basicSalary;
        this.hra = hra;
        this.tax = tax;
        this.netSalary = calculateNetSalary();
    }

    // Parameterized Constructor (with id - for existing employees)
    public Employee(int id, String name, String designation, double basicSalary, double hra, double tax, double netSalary) {
        this.id = id;
        this.name = name;
        this.designation = designation;
        this.basicSalary = basicSalary;
        this.hra = hra;
        this.tax = tax;
        this.netSalary = netSalary;
    }

    // Method to calculate net salary
    // Formula: Net Salary = Basic Salary + HRA - Tax
    public double calculateNetSalary() {
        return basicSalary + hra - tax;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public double getBasicSalary() {
        return basicSalary;
    }

    public void setBasicSalary(double basicSalary) {
        this.basicSalary = basicSalary;
        this.netSalary = calculateNetSalary(); // Recalculate when basic salary changes
    }

    public double getHra() {
        return hra;
    }

    public void setHra(double hra) {
        this.hra = hra;
        this.netSalary = calculateNetSalary(); // Recalculate when HRA changes
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
        this.netSalary = calculateNetSalary(); // Recalculate when tax changes
    }

    public double getNetSalary() {
        return netSalary;
    }

    public void setNetSalary(double netSalary) {
        this.netSalary = netSalary;
    }

    // toString method for easy printing
    @Override
    public String toString() {
        return String.format(
            "Employee [ID=%d, Name=%s, Designation=%s, Basic Salary=₹%.2f, HRA=₹%.2f, Tax=₹%.2f, Net Salary=₹%.2f]",
            id, name, designation, basicSalary, hra, tax, netSalary
        );
    }

    // Method to display detailed payslip
    public void displayPayslip() {
        System.out.println("\n╔════════════════════════════════════════════════════════╗");
        System.out.println("║              EMPLOYEE MONTHLY PAYSLIP                  ║");
        System.out.println("║           SAIL Salem Steel Plant System                ║");
        System.out.println("╠════════════════════════════════════════════════════════╣");
        System.out.printf("║ Employee ID       : %-33d  ║%n", id);
        System.out.printf("║ Employee Name     : %-33s  ║%n", name);
        System.out.printf("║ Designation       : %-33s  ║%n", designation);
        System.out.println("╠════════════════════════════════════════════════════════╣");
        System.out.printf("║ Basic Salary      : ₹%-32.2f  ║%n", basicSalary);
        System.out.printf("║ HRA               : ₹%-32.2f  ║%n", hra);
        System.out.println("║                     ─────────────────────────────────  ║");
        System.out.printf("║ Gross Salary      : ₹%-32.2f  ║%n", (basicSalary + hra));
        System.out.println("╠════════════════════════════════════════════════════════╣");
        System.out.printf("║ Tax Deduction     : ₹%-32.2f  ║%n", tax);
        System.out.println("╠════════════════════════════════════════════════════════╣");
        System.out.printf("║ NET SALARY        : ₹%-32.2f  ║%n", netSalary);
        System.out.println("╚════════════════════════════════════════════════════════╝\n");
    }
}