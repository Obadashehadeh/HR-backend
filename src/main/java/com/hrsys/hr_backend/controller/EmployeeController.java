package com.hrsys.hr_backend.controller;

import com.hrsys.hr_backend.dao.EmployeeDAO;
import com.hrsys.hr_backend.dao.DepartmentDAO;
import com.hrsys.hr_backend.dao.PositionDAO;
import com.hrsys.hr_backend.entity.Employee;
import com.hrsys.hr_backend.entity.Department;
import com.hrsys.hr_backend.entity.Position;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/employees")
@CrossOrigin(origins = "http://localhost:4200")
public class EmployeeController {

    @Autowired
    private EmployeeDAO employeeDAO;

    @Autowired
    private DepartmentDAO departmentDAO;

    @Autowired
    private PositionDAO positionDAO;

    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeDAO.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        Optional<Employee> employee = employeeDAO.findById(id);
        if (employee.isPresent()) {
            return ResponseEntity.ok(employee.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR_MANAGER')")
    public ResponseEntity<?> createEmployee(@Valid @RequestBody EmployeeRequest employeeRequest) {
        try {
            Employee employee = new Employee();
            employee.setFirstName(employeeRequest.getFirstName());
            employee.setLastName(employeeRequest.getLastName());
            employee.setEmail(employeeRequest.getEmail());
            employee.setPhoneNumber(employeeRequest.getPhoneNumber());
            employee.setHireDate(employeeRequest.getHireDate());
            employee.setSalary(employeeRequest.getSalary());

            if (employeeRequest.getDepartmentId() != null) {
                Department department = departmentDAO.findById(employeeRequest.getDepartmentId())
                        .orElseThrow(() -> new RuntimeException("Department not found"));
                employee.setDepartment(department);
            }

            if (employeeRequest.getPositionId() != null) {
                Position position = positionDAO.findById(employeeRequest.getPositionId())
                        .orElseThrow(() -> new RuntimeException("Position not found"));
                employee.setPosition(position);
            }

            Employee savedEmployee = employeeDAO.save(employee);
            return ResponseEntity.ok(savedEmployee);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR_MANAGER')")
    public ResponseEntity<?> updateEmployee(@PathVariable Long id, @Valid @RequestBody EmployeeRequest employeeRequest) {
        try {
            Employee employee = employeeDAO.findById(id)
                    .orElseThrow(() -> new RuntimeException("Employee not found"));

            employee.setFirstName(employeeRequest.getFirstName());
            employee.setLastName(employeeRequest.getLastName());
            employee.setEmail(employeeRequest.getEmail());
            employee.setPhoneNumber(employeeRequest.getPhoneNumber());
            employee.setHireDate(employeeRequest.getHireDate());
            employee.setSalary(employeeRequest.getSalary());

            if (employeeRequest.getDepartmentId() != null) {
                Department department = departmentDAO.findById(employeeRequest.getDepartmentId())
                        .orElseThrow(() -> new RuntimeException("Department not found"));
                employee.setDepartment(department);
            }

            if (employeeRequest.getPositionId() != null) {
                Position position = positionDAO.findById(employeeRequest.getPositionId())
                        .orElseThrow(() -> new RuntimeException("Position not found"));
                employee.setPosition(position);
            }

            Employee updatedEmployee = employeeDAO.save(employee);
            return ResponseEntity.ok(updatedEmployee);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR_MANAGER')")
    public ResponseEntity<?> deleteEmployee(@PathVariable Long id) {
        try {
            Employee employee = employeeDAO.findById(id)
                    .orElseThrow(() -> new RuntimeException("Employee not found"));

            employeeDAO.deleteById(id);
            return ResponseEntity.ok(new MessageResponse("Employee deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @GetMapping("/department/{departmentId}")
    public List<Employee> getEmployeesByDepartment(@PathVariable Long departmentId) {
        return employeeDAO.findByDepartmentId(departmentId);
    }

    @GetMapping("/search")
    public List<Employee> searchEmployees(@RequestParam String name) {
        return employeeDAO.findByFirstNameContainingOrLastNameContaining(name, name);
    }

    @GetMapping("/salary/above/{amount}")
    public List<Employee> getEmployeesWithSalaryAbove(@PathVariable BigDecimal amount) {
        return employeeDAO.findBySalaryGreaterThan(amount);
    }

    @GetMapping("/statistics/department/{departmentId}")
    public ResponseEntity<?> getDepartmentStatistics(@PathVariable Long departmentId) {
        try {
            Long employeeCount = employeeDAO.countEmployeesByDepartment(departmentId);
            BigDecimal avgSalary = employeeDAO.findAverageSalaryByDepartment(departmentId);

            DepartmentStatsResponse stats = new DepartmentStatsResponse(employeeCount, avgSalary);
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    public static class EmployeeRequest {
        private String firstName;
        private String lastName;
        private String email;
        private String phoneNumber;
        private LocalDate hireDate;
        private BigDecimal salary;
        private Long departmentId;
        private Long positionId;

        public String getFirstName() { return firstName; }
        public void setFirstName(String firstName) { this.firstName = firstName; }

        public String getLastName() { return lastName; }
        public void setLastName(String lastName) { this.lastName = lastName; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getPhoneNumber() { return phoneNumber; }
        public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

        public LocalDate getHireDate() { return hireDate; }
        public void setHireDate(LocalDate hireDate) { this.hireDate = hireDate; }

        public BigDecimal getSalary() { return salary; }
        public void setSalary(BigDecimal salary) { this.salary = salary; }

        public Long getDepartmentId() { return departmentId; }
        public void setDepartmentId(Long departmentId) { this.departmentId = departmentId; }

        public Long getPositionId() { return positionId; }
        public void setPositionId(Long positionId) { this.positionId = positionId; }
    }

    public static class MessageResponse {
        private String message;

        public MessageResponse(String message) { this.message = message; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }

    public static class DepartmentStatsResponse {
        private Long employeeCount;
        private BigDecimal avgSalary;

        public DepartmentStatsResponse(Long employeeCount, BigDecimal avgSalary) {
            this.employeeCount = employeeCount;
            this.avgSalary = avgSalary;
        }

        public Long getEmployeeCount() { return employeeCount; }
        public void setEmployeeCount(Long employeeCount) { this.employeeCount = employeeCount; }

        public BigDecimal getAvgSalary() { return avgSalary; }
        public void setAvgSalary(BigDecimal avgSalary) { this.avgSalary = avgSalary; }
    }
}