import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
import java.util.List;

class PayrollHttpServer {
    private static PayrollService payrollService = new PayrollService();
    
    public static void main(String[] args) throws IOException {
        // Test database connection first
        if (!DatabaseConnection.testConnection()) {
            System.out.println("❌ Database connection failed. Server cannot start.");
            return;
        }
        
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        
        // Create contexts - All endpoints the frontend needs
        server.createContext("/employees", new EmployeesHandler());
        server.createContext("/employees/search", new SearchHandler());
        server.createContext("/employees/", new EmployeeByIdHandler());
        
        server.setExecutor(null);
        server.start();
        System.out.println("✅ Payroll HTTP Server started on http://localhost:8080");
        System.out.println("📊 Available endpoints:");
        System.out.println("   GET    /employees");
        System.out.println("   POST   /employees");
        System.out.println("   GET    /employees/{id}");
        System.out.println("   PUT    /employees/{id}");
        System.out.println("   DELETE /employees/{id}");
        System.out.println("   GET    /employees/search?designation=XXX");
        
    }
    private static void logRequest(HttpExchange exchange) {
    String method = exchange.getRequestMethod();
    String path = exchange.getRequestURI().getPath();
    String query = exchange.getRequestURI().getQuery();
    String fullUrl = path + (query != null ? "?" + query : "");
    
    System.out.println("📥 " + method + " " + fullUrl);
    System.out.println("   Headers: " + exchange.getRequestHeaders().entrySet());
}
    
    // Handler for /employees (GET all, POST new)
    static class EmployeesHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
             logRequest(exchange);
            setupCORS(exchange);
            
            if ("OPTIONS".equals(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(204, -1);
                return;
            }
            
            try {
                switch (exchange.getRequestMethod()) {
                    case "GET":
                        handleGetEmployees(exchange);
                        break;
                    case "POST":
                        handleAddEmployee(exchange);
                        break;
                    default:
                        sendResponse(exchange, 405, "{\"error\":\"Method not allowed\"}");
                }
            } catch (Exception e) {
                e.printStackTrace();
                sendResponse(exchange, 500, "{\"error\":\"Internal server error: " + e.getMessage() + "\"}");
            }
        }
        
        private void handleGetEmployees(HttpExchange exchange) throws IOException {
            List<Employee> employees = payrollService.getAllEmployees();
            String jsonResponse = convertEmployeesToJson(employees);
            sendResponse(exchange, 200, jsonResponse);
        }
        
        private void handleAddEmployee(HttpExchange exchange) throws IOException {
            String requestBody = getRequestBody(exchange);
            
            try {
                // Parse JSON manually
                Employee employee = parseEmployeeFromJson(requestBody);
                
                if (employee == null) {
                    sendResponse(exchange, 400, "{\"error\":\"Invalid employee data\"}");
                    return;
                }
                
                boolean success = payrollService.addEmployee(employee);
                if (success) {
                    sendResponse(exchange, 201, "{\"status\":\"success\", \"message\":\"Employee added successfully\"}");
                } else {
                    sendResponse(exchange, 500, "{\"error\":\"Failed to add employee to database\"}");
                }
            } catch (Exception e) {
                sendResponse(exchange, 400, "{\"error\":\"Invalid JSON format\"}");
            }
        }
    }
    
    // Handler for /employees/{id} (GET, PUT, DELETE)
    static class EmployeeByIdHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
             logRequest(exchange);
            setupCORS(exchange);
            
            if ("OPTIONS".equals(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(204, -1);
                return;
            }
            
            try {
                String path = exchange.getRequestURI().getPath();
                String[] pathParts = path.split("/");
                
                if (pathParts.length < 3) {
                    sendResponse(exchange, 400, "{\"error\":\"Employee ID is required\"}");
                    return;
                }
                
                int employeeId = Integer.parseInt(pathParts[2]);
                
                switch (exchange.getRequestMethod()) {
                    case "GET":
                        handleGetEmployeeById(exchange, employeeId);
                        break;
                    case "PUT":
                        handleUpdateEmployee(exchange, employeeId);
                        break;
                    case "DELETE":
                        handleDeleteEmployee(exchange, employeeId);
                        break;
                    default:
                        sendResponse(exchange, 405, "{\"error\":\"Method not allowed\"}");
                }
            } catch (NumberFormatException e) {
                sendResponse(exchange, 400, "{\"error\":\"Invalid employee ID\"}");
            } catch (Exception e) {
                e.printStackTrace();
                sendResponse(exchange, 500, "{\"error\":\"Internal server error: " + e.getMessage() + "\"}");
            }
        }
        
        private void handleGetEmployeeById(HttpExchange exchange, int employeeId) throws IOException {
            Employee employee = payrollService.getEmployeeById(employeeId);
            
            if (employee != null) {
                String jsonResponse = convertEmployeeToJson(employee);
                sendResponse(exchange, 200, jsonResponse);
            } else {
                sendResponse(exchange, 404, "{\"error\":\"Employee not found\"}");
            }
        }
        
        private void handleUpdateEmployee(HttpExchange exchange, int employeeId) throws IOException {
            Employee existingEmployee = payrollService.getEmployeeById(employeeId);
            if (existingEmployee == null) {
                sendResponse(exchange, 404, "{\"error\":\"Employee not found\"}");
                return;
            }
            
            String requestBody = getRequestBody(exchange);
            
            try {
                Employee updatedEmployee = parseEmployeeFromJson(requestBody);
                if (updatedEmployee == null) {
                    sendResponse(exchange, 400, "{\"error\":\"Invalid employee data\"}");
                    return;
                }
                
                // Set the ID for the update
                updatedEmployee.setId(employeeId);
                
                boolean success = payrollService.updateEmployee(updatedEmployee);
                if (success) {
                    sendResponse(exchange, 200, "{\"status\":\"success\", \"message\":\"Employee updated successfully\"}");
                } else {
                    sendResponse(exchange, 500, "{\"error\":\"Failed to update employee\"}");
                }
            } catch (Exception e) {
                sendResponse(exchange, 400, "{\"error\":\"Invalid JSON format\"}");
            }
        }
        
        private void handleDeleteEmployee(HttpExchange exchange, int employeeId) throws IOException {
            Employee existingEmployee = payrollService.getEmployeeById(employeeId);
            if (existingEmployee == null) {
                sendResponse(exchange, 404, "{\"error\":\"Employee not found\"}");
                return;
            }
            
            boolean success = payrollService.deleteEmployee(employeeId);
            if (success) {
                sendResponse(exchange, 200, "{\"status\":\"success\", \"message\":\"Employee deleted successfully\"}");
            } else {
                sendResponse(exchange, 500, "{\"error\":\"Failed to delete employee\"}");
            }
        }
    }
    
    // Handler for /employees/search
    static class SearchHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            logRequest(exchange);
            setupCORS(exchange);
            
            if ("OPTIONS".equals(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(204, -1);
                return;
            }
            
            if ("GET".equals(exchange.getRequestMethod())) {
                String query = exchange.getRequestURI().getQuery();
                String designation = getParameterValue(query, "designation");
                
                if (designation == null || designation.isEmpty()) {
                    sendResponse(exchange, 400, "{\"error\":\"Designation parameter is required\"}");
                    return;
                }
                
                List<Employee> employees = payrollService.searchByDesignation(designation);
                String jsonResponse = convertEmployeesToJson(employees);
                sendResponse(exchange, 200, jsonResponse);
            } else {
                sendResponse(exchange, 405, "{\"error\":\"Method not allowed\"}");
            }
        }
    }
    
    // Utility methods
    private static void setupCORS(HttpExchange exchange) {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type, Authorization");
    }
    
private static String getRequestBody(HttpExchange exchange) throws IOException {
    InputStream inputStream = exchange.getRequestBody();
    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
    
    try {
        return reader.lines().collect(Collectors.joining("\n"));
    } finally {
        // Don't close the reader here - the HttpExchange will handle closing the stream
        // Closing it here can cause "stream closed" errors
    }
}
private static Employee parseEmployeeFromJson(String json) {
    try {
        System.out.println("Raw JSON received: " + json);
        
        // Remove whitespace and braces
        json = json.trim();
        if (json.startsWith("{") && json.endsWith("}")) {
            json = json.substring(1, json.length() - 1);
        }
        
        String[] pairs = json.split(",");
        
        String name = null;
        String designation = null;
        double basicSalary = 0;
        double hra = 0;
        double tax = 0;
        
        for (String pair : pairs) {
            String[] keyValue = pair.split(":", 2);
            if (keyValue.length == 2) {
                String key = keyValue[0].trim().replace("\"", "");
                String value = keyValue[1].trim().replace("\"", "");
                
                System.out.println("Parsing key: " + key + ", value: " + value);
                
                switch (key) {
                    case "name":
                        name = value;
                        break;
                    case "designation":
                        designation = value;
                        break;
                    case "basicSalary":
                        basicSalary = Double.parseDouble(value);
                        break;
                    case "hra":
                        hra = Double.parseDouble(value);
                        break;
                    case "tax":
                        tax = Double.parseDouble(value);
                        break;
                }
            }
        }
        
        // Validate required fields
        if (name == null || name.isEmpty() || designation == null || designation.isEmpty()) {
            System.out.println("Missing required fields: name=" + name + ", designation=" + designation);
            return null;
        }
        
        System.out.println("Successfully parsed employee: " + name + ", " + designation);
        return new Employee(name, designation, basicSalary, hra, tax);
        
    } catch (Exception e) {
        System.out.println("JSON parsing error: " + e.getMessage());
        e.printStackTrace();
        return null;
    }
}
    
    private static String convertEmployeesToJson(List<Employee> employees) {
        StringBuilder jsonBuilder = new StringBuilder("[");
        
        for (int i = 0; i < employees.size(); i++) {
            Employee emp = employees.get(i);
            jsonBuilder.append(convertEmployeeToJson(emp));
            
            if (i < employees.size() - 1) {
                jsonBuilder.append(",");
            }
        }
        jsonBuilder.append("]");
        
        return jsonBuilder.toString();
    }
    
    private static String convertEmployeeToJson(Employee emp) {
        return new StringBuilder()
            .append("{")
            .append("\"id\":").append(emp.getId()).append(",")
            .append("\"name\":\"").append(escapeJson(emp.getName())).append("\",")
            .append("\"designation\":\"").append(escapeJson(emp.getDesignation())).append("\",")
            .append("\"basicSalary\":").append(emp.getBasicSalary()).append(",")
            .append("\"hra\":").append(emp.getHra()).append(",")
            .append("\"tax\":").append(emp.getTax()).append(",")
            .append("\"netSalary\":").append(emp.getNetSalary())
            .append("}")
            .toString();
    }
    
    private static String escapeJson(String text) {
        if (text == null) return "";
        return text.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\n", "\\n")
                  .replace("\r", "\\r")
                  .replace("\t", "\\t");
    }
    
    private static String getParameterValue(String query, String paramName) {
        if (query == null) return null;
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            if (keyValue.length == 2 && keyValue[0].equals(paramName)) {
                return keyValue[1];
            }
        }
        return null;
    }
    
    private static void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        exchange.sendResponseHeaders(statusCode, response.getBytes(StandardCharsets.UTF_8).length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes(StandardCharsets.UTF_8));
        os.close();
    }
}