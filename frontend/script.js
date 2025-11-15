// API Base URL - Update this to match your backend endpoint
const API_BASE_URL = 'http://localhost:8080';

// Global state
let employees = [];
let currentEmployeeId = null;

// Initialize application
document.addEventListener('DOMContentLoaded', function() {
    loadDashboardData();
    loadEmployees();
    setupEventListeners();
});

// Setup event listeners
function setupEventListeners() {
    // Navigation
    document.querySelectorAll('.nav-link').forEach(link => {
        link.addEventListener('click', function(e) {
            e.preventDefault();
            const targetSection = this.getAttribute('href').substring(1);
            showSection(targetSection);
        });
    });

    // Real-time net salary calculation
    const salaryInputs = ['basicSalary', 'hra', 'tax'];
    salaryInputs.forEach(inputId => {
        const input = document.getElementById(inputId);
        if (input) {
            input.addEventListener('input', calculateNetSalary);
        }
    });
}

// Show/hide sections
function showSection(sectionId) {
    // Hide all sections
    document.querySelectorAll('.section').forEach(section => {
        section.classList.remove('active');
    });
    
    // Show target section
    document.getElementById(sectionId).classList.add('active');
    
    // Update navigation
    document.querySelectorAll('.nav-link').forEach(link => {
        link.classList.remove('active');
        if (link.getAttribute('href') === `#${sectionId}`) {
            link.classList.add('active');
        }
    });
    
    // Load section-specific data
    switch(sectionId) {
        case 'dashboard':
            loadDashboardData();
            break;
        case 'employees':
            loadEmployees();
            break;
        case 'payslips':
            loadEmployeeDropdown();
            break;
        case 'reports':
            loadReports();
            break;
    }
}

async function apiCall(endpoint, options = {}) {
    showLoading();
    try {
        const fullUrl = `${API_BASE_URL}${endpoint}`;
        console.log(`Making API call to: ${fullUrl}`);
        
        const response = await fetch(fullUrl, {
            mode: 'cors',
            headers: {
                'Content-Type': 'application/json',
                ...options.headers
            },
            ...options
        });
        
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        
        const data = await response.json();
        console.log('API response:', data);
        return data;
    } catch (error) {
        console.error('API call failed:', error);
        showNotification('Error: ' + error.message, 'error');
        return null;
    } finally {
        hideLoading();
    }
}

// Dashboard functions
async function loadDashboardData() {
    const employees = await apiCall('/employees');
    if (employees) {
        updateDashboardStats(employees);
    }
}

function updateDashboardStats(employees) {
    const totalEmployees = employees.length;
    const totalPayroll = employees.reduce((sum, emp) => sum + emp.netSalary, 0);
    const avgSalary = totalEmployees > 0 ? totalPayroll / totalEmployees : 0;
    
    document.getElementById('totalEmployees').textContent = totalEmployees;
    document.getElementById('totalPayroll').textContent = `₹${totalPayroll.toFixed(2)}`;
    document.getElementById('avgSalary').textContent = `₹${avgSalary.toFixed(2)}`;
}

// Employee management functions
async function loadEmployees() {
    const employees = await apiCall('/employees');
    if (employees) {
        displayEmployees(employees);
    }
}

function displayEmployees(employeesList) {
    const tbody = document.getElementById('employeesTableBody');
    tbody.innerHTML = '';
    
    employeesList.forEach(employee => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${employee.id}</td>
            <td>${employee.name}</td>
            <td>${employee.designation}</td>
            <td>₹${employee.basicSalary.toFixed(2)}</td>
            <td>₹${employee.hra.toFixed(2)}</td>
            <td>₹${employee.tax.toFixed(2)}</td>
            <td>₹${employee.netSalary.toFixed(2)}</td>
            <td class="action-buttons-cell">
                <button class="btn btn-info btn-sm" onclick="editEmployee(${employee.id})">
                    <i class="fas fa-edit"></i>
                </button>
                <button class="btn btn-danger btn-sm" onclick="deleteEmployee(${employee.id})">
                    <i class="fas fa-trash"></i>
                </button>
                <button class="btn btn-primary btn-sm" onclick="generatePayslip(${employee.id})">
                    <i class="fas fa-file-invoice"></i>
                </button>
            </td>
        `;
        tbody.appendChild(row);
    });
}

function showAddEmployeeForm() {
    document.getElementById('addEmployeeForm').style.display = 'block';
    document.getElementById('employeeForm').reset();
    calculateNetSalary();
}

function hideAddEmployeeForm() {
    document.getElementById('addEmployeeForm').style.display = 'none';
}

function calculateNetSalary() {
    const basicSalary = parseFloat(document.getElementById('basicSalary').value) || 0;
    const hra = parseFloat(document.getElementById('hra').value) || 0;
    const tax = parseFloat(document.getElementById('tax').value) || 0;
    const netSalary = basicSalary + hra - tax;
    
    document.getElementById('netSalaryDisplay').textContent = `₹${netSalary.toFixed(2)}`;
}

async function addEmployee(event) {
    event.preventDefault();
    
    const formData = new FormData(event.target);
    const employee = {
        name: formData.get('name'),
        designation: formData.get('designation'),
        basicSalary: parseFloat(formData.get('basicSalary')),
        hra: parseFloat(formData.get('hra')),
        tax: parseFloat(formData.get('tax'))
    };
    
    const result = await apiCall('/employees', {
        method: 'POST',
        body: JSON.stringify(employee)
    });
    
    if (result) {
        showNotification('Employee added successfully!', 'success');
        hideAddEmployeeForm();
        loadEmployees();
        loadDashboardData();
    }
}

async function editEmployee(employeeId) {
    const employee = await apiCall(`/employees/${employeeId}`);
    if (employee) {
        // Populate form with employee data
        document.getElementById('name').value = employee.name;
        document.getElementById('designation').value = employee.designation;
        document.getElementById('basicSalary').value = employee.basicSalary;
        document.getElementById('hra').value = employee.hra;
        document.getElementById('tax').value = employee.tax;
        
        currentEmployeeId = employeeId;
        showAddEmployeeForm();
        
        // Change form to update mode
        const form = document.getElementById('employeeForm');
        form.onsubmit = updateEmployee;
        form.querySelector('button[type="submit"]').textContent = 'Update Employee';
    }
}

async function updateEmployee(event) {
    event.preventDefault();
    
    const formData = new FormData(event.target);
    const employee = {
        name: formData.get('name'),
        designation: formData.get('designation'),
        basicSalary: parseFloat(formData.get('basicSalary')),
        hra: parseFloat(formData.get('hra')),
        tax: parseFloat(formData.get('tax'))
    };
    
    const result = await apiCall(`/employees/${currentEmployeeId}`, {
        method: 'PUT',
        body: JSON.stringify(employee)
    });
    
    if (result) {
        showNotification('Employee updated successfully!', 'success');
        hideAddEmployeeForm();
        loadEmployees();
        loadDashboardData();
        currentEmployeeId = null;
        
        // Reset form to add mode
        const form = document.getElementById('employeeForm');
        form.onsubmit = addEmployee;
        form.querySelector('button[type="submit"]').textContent = 'Add Employee';
    }
}

async function deleteEmployee(employeeId) {
    if (confirm('Are you sure you want to delete this employee?')) {
        const result = await apiCall(`/employees/${employeeId}`, {
            method: 'DELETE'
        });
        
        if (result) {
            showNotification('Employee deleted successfully!', 'success');
            loadEmployees();
            loadDashboardData();
        }
    }
}

// Payslip functions
async function loadEmployeeDropdown() {
    const employees = await apiCall('/employees');
    if (employees) {
        const select = document.getElementById('employeeSelect');
        select.innerHTML = '<option value="">Select an employee</option>';
        
        employees.forEach(employee => {
            const option = document.createElement('option');
            option.value = employee.id;
            option.textContent = `${employee.name} - ${employee.designation}`;
            select.appendChild(option);
        });
    }
}

async function loadEmployeeForPayslip() {
    const employeeId = document.getElementById('employeeSelect').value;
    if (employeeId) {
        await generatePayslip(employeeId);
    }
}

async function generatePayslip(employeeId) {
    const employee = await apiCall(`/employees/${employeeId}`);
    if (employee) {
        displayPayslip(employee);
    }
}

function generatePayslipPrompt() {
    const employeeId = prompt('Enter Employee ID:');
    if (employeeId) {
        generatePayslip(employeeId);
        showSection('payslips');
    }
}

function displayPayslip(employee) {
    const container = document.getElementById('payslipContainer');
    const grossSalary = employee.basicSalary + employee.hra;
    
    container.innerHTML = `
        <div class="payslip">
            <div class="payslip-header">
                <h2>SAIL Salem Steel Plant</h2>
                <h3>Monthly Payslip</h3>
            </div>
            <div class="payslip-body">
                <div class="payslip-row">
                    <span>Employee ID:</span>
                    <span>${employee.id}</span>
                </div>
                <div class="payslip-row">
                    <span>Employee Name:</span>
                    <span>${employee.name}</span>
                </div>
                <div class="payslip-row">
                    <span>Designation:</span>
                    <span>${employee.designation}</span>
                </div>
                <div class="payslip-row">
                    <span>Basic Salary:</span>
                    <span>₹${employee.basicSalary.toFixed(2)}</span>
                </div>
                <div class="payslip-row">
                    <span>HRA:</span>
                    <span>₹${employee.hra.toFixed(2)}</span>
                </div>
                <div class="payslip-row">
                    <span>Gross Salary:</span>
                    <span>₹${grossSalary.toFixed(2)}</span>
                </div>
                <div class="payslip-row">
                    <span>Tax Deduction:</span>
                    <span>₹${employee.tax.toFixed(2)}</span>
                </div>
                <div class="payslip-row payslip-total">
                    <span>Net Salary:</span>
                    <span>₹${employee.netSalary.toFixed(2)}</span>
                </div>
            </div>
        </div>
        <div style="text-align: center; margin-top: 1rem;">
            <button class="btn btn-primary" onclick="printPayslip()">
                <i class="fas fa-print"></i> Print Payslip
            </button>
        </div>
    `;
    
    container.style.display = 'block';
    
    // Auto-scroll to the payslip section
    showSection('payslips');
}

function printPayslip() {
    const payslipContent = document.querySelector('.payslip').outerHTML;
    const printWindow = window.open('', '_blank');
    printWindow.document.write(`
        <html>
            <head>
                <title>Payslip - SAIL Salem Steel Plant</title>
                <style>
                    body { font-family: Arial, sans-serif; margin: 20px; }
                    .payslip { border: 2px solid #000; padding: 20px; }
                    .payslip-header { text-align: center; margin-bottom: 20px; }
                    .payslip-row { display: flex; justify-content: space-between; margin: 10px 0; }
                    .payslip-total { border-top: 2px solid #000; padding-top: 10px; font-weight: bold; }
                </style>
            </head>
            <body>${payslipContent}</body>
        </html>
    `);
    printWindow.document.close();
    printWindow.print();
}

// Report functions
async function loadReports() {
    const employees = await apiCall('/employees');
    if (employees) {
        updateReportStats(employees);
        renderDesignationChart(employees);
    }
}

function updateReportStats(employees) {
    const totalEmployees = employees.length;
    const totalPayroll = employees.reduce((sum, emp) => sum + emp.netSalary, 0);
    const avgSalary = totalEmployees > 0 ? totalPayroll / totalEmployees : 0;
    
    document.getElementById('reportTotalEmployees').textContent = totalEmployees;
    document.getElementById('reportTotalPayroll').textContent = `₹${totalPayroll.toFixed(2)}`;
    document.getElementById('reportAvgSalary').textContent = `₹${avgSalary.toFixed(2)}`;
}

function renderDesignationChart(employees) {
    const chartContainer = document.getElementById('designationChart');
    
    // Group by designation
    const designationCount = {};
    employees.forEach(emp => {
        designationCount[emp.designation] = (designationCount[emp.designation] || 0) + 1;
    });
    
    // Create simple text chart
    let chartHTML = '<div style="text-align: left;">';
    Object.entries(designationCount).forEach(([designation, count]) => {
        const percentage = (count / employees.length * 100).toFixed(1);
        chartHTML += `
            <div style="margin: 10px 0;">
                <div style="display: flex; justify-content: space-between;">
                    <span>${designation}</span>
                    <span>${count} (${percentage}%)</span>
                </div>
                <div style="background: #3498db; height: 20px; width: ${percentage}%; border-radius: 3px;"></div>
            </div>
        `;
    });
    chartHTML += '</div>';
    
    chartContainer.innerHTML = chartHTML;
}

async function searchByDesignation() {
    const designation = document.getElementById('designationSearch').value;
    if (!designation) {
        showNotification('Please enter a designation to search', 'warning');
        return;
    }
    
    const employees = await apiCall(`/employees/search?designation=${encodeURIComponent(designation)}`);
    const resultsContainer = document.getElementById('searchResults');
    
    if (employees && employees.length > 0) {
        let resultsHTML = '<h4>Search Results:</h4><div class="table-container"><table><thead><tr><th>ID</th><th>Name</th><th>Designation</th><th>Net Salary</th></tr></thead><tbody>';
        
        employees.forEach(emp => {
            resultsHTML += `
                <tr>
                    <td>${emp.id}</td>
                    <td>${emp.name}</td>
                    <td>${emp.designation}</td>
                    <td>₹${emp.netSalary.toFixed(2)}</td>
                </tr>
            `;
        });
        
        resultsHTML += '</tbody></table></div>';
        resultsContainer.innerHTML = resultsHTML;
    } else {
        resultsContainer.innerHTML = '<p>No employees found with the specified designation.</p>';
    }
}

// Utility functions
function showLoading() {
    document.getElementById('loading').style.display = 'flex';
}

function hideLoading() {
    document.getElementById('loading').style.display = 'none';
}

function showNotification(message, type = 'info') {
    // Create notification element
    const notification = document.createElement('div');
    notification.className = `notification notification-${type}`;
    notification.innerHTML = `
        <span>${message}</span>
        <button onclick="this.parentElement.remove()">&times;</button>
    `;
    
    // Add styles
    notification.style.cssText = `
        position: fixed;
        top: 20px;
        right: 20px;
        background: ${type === 'success' ? '#27ae60' : type === 'error' ? '#e74c3c' : '#3498db'};
        color: white;
        padding: 1rem 2rem 1rem 1rem;
        border-radius: 5px;
        box-shadow: 0 4px 6px rgba(0,0,0,0.1);
        z-index: 1000;
        max-width: 300px;
    `;
    
    notification.querySelector('button').style.cssText = `
        position: absolute;
        top: 5px;
        right: 5px;
        background: none;
        border: none;
        color: white;
        font-size: 1.2rem;
        cursor: pointer;
    `;
    
    document.body.appendChild(notification);
    
    // Auto remove after 5 seconds
    setTimeout(() => {
        if (notification.parentElement) {
            notification.remove();
        }
    }, 5000);
}