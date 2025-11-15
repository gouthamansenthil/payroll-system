import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Database Connection Class
 * Manages MySQL database connectivity for the Employee Payroll System
 * Uses singleton pattern to ensure single database connection instance
 */
public class DatabaseConnection {
    
    // Database credentials - Update these according to your MySQL setup
private static final String URL = "jdbc:mysql://localhost:3306/employee_payroll_system?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC";
private static final String USERNAME = "root";  // Your MySQL username
private static final String PASSWORD = "admin"; // Your MySQL password

    
    // Single connection instance (Singleton pattern)
    private static Connection connection = null;
    
    // Private constructor to prevent instantiation
    private DatabaseConnection() {
    }
    
    /**
     * Establishes and returns database connection
     * @return Connection object
     */
    public static Connection getConnection() {
        try {
            // Check if connection exists and is valid
            if (connection == null || connection.isClosed()) {
                // Load MySQL JDBC Driver
                Class.forName("com.mysql.cj.jdbc.Driver");
                
                // Establish connection
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                System.out.println("✓ Database connection established successfully!");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("✗ MySQL JDBC Driver not found!");
            System.err.println("Please add mysql-connector-java JAR to your project.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("✗ Database connection failed!");
            System.err.println("Please check your database credentials and ensure MySQL server is running.");
            e.printStackTrace();
        }
        return connection;
    }
    
    /**
     * Closes the database connection
     */
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("✓ Database connection closed successfully!");
            }
        } catch (SQLException e) {
            System.err.println("✗ Error closing database connection!");
            e.printStackTrace();
        }
    }
    
    /**
     * Tests database connectivity
     * @return true if connection is successful, false otherwise
     */
    public static boolean testConnection() {
        try {
            Connection conn = getConnection();
            if (conn != null && !conn.isClosed()) {
                System.out.println("✓ Database connectivity test PASSED!");
                return true;
            }
        } catch (SQLException e) {
            System.err.println("✗ Database connectivity test FAILED!");
            e.printStackTrace();
        }
        return false;
    }
}