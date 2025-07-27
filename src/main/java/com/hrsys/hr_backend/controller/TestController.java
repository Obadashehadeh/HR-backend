package com.hrsys.hr_backend.controller;

import com.hrsys.hr_backend.entity.Department;
import com.hrsys.hr_backend.entity.Employee;
import com.hrsys.hr_backend.entity.Position;
import com.hrsys.hr_backend.entity.User;
import com.hrsys.hr_backend.entity.Role;
import com.hrsys.hr_backend.repository.DepartmentRepository;
import com.hrsys.hr_backend.repository.EmployeeRepository;
import com.hrsys.hr_backend.repository.PositionRepository;
import com.hrsys.hr_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private PositionRepository positionRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @GetMapping("/create-sample-data")
    public String createSampleData() {

        User user1 = new User("john_doe", "john@company.com", "password123");
        user1.setRole(Role.ADMIN);
        userRepository.save(user1);

        User user2 = new User("jane_smith", "jane@company.com", "password123");
        user2.setRole(Role.HR_MANAGER);
        userRepository.save(user2);

        Department itDept = new Department("IT", "Information Technology Department", "John Manager");
        departmentRepository.save(itDept);

        Department hrDept = new Department("HR", "Human Resources Department", "Jane Manager");
        departmentRepository.save(hrDept);

        Position devPosition = new Position("Software Developer", "Develops software applications");
        devPosition.setDepartment(itDept);
        positionRepository.save(devPosition);

        Position hrPosition = new Position("HR Specialist", "Handles HR operations");
        hrPosition.setDepartment(hrDept);
        positionRepository.save(hrPosition);

        Employee emp1 = new Employee("Ahmed", "Ali", "ahmed.ali@company.com", LocalDate.of(2023, 1, 15), new BigDecimal("5000.00"));
        emp1.setDepartment(itDept);
        emp1.setPosition(devPosition);
        emp1.setPhoneNumber("123-456-7890");
        employeeRepository.save(emp1);

        Employee emp2 = new Employee("Sara", "Hassan", "sara.hassan@company.com", LocalDate.of(2023, 3, 10), new BigDecimal("4500.00"));
        emp2.setDepartment(hrDept);
        emp2.setPosition(hrPosition);
        emp2.setPhoneNumber("098-765-4321");
        employeeRepository.save(emp2);

        return "Sample data created successfully!";
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/departments")
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    @GetMapping("/employees")
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @GetMapping("/employees-with-department")
    public List<Employee> getEmployeesWithDepartment() {
        return employeeRepository.findByDepartmentName("IT");
    }

    @GetMapping("/update-employee-salary")
    public String updateEmployeeSalary() {
        Employee employee = employeeRepository.findByEmail("ahmed.ali@company.com").orElse(null);
        if (employee != null) {
            employee.setSalary(new BigDecimal("6000.00"));
            employeeRepository.save(employee);
            return "Ahmed's salary updated to 6000!";
        }
        return "Employee not found";
    }

    @GetMapping("/delete-user")
    public String deleteUser() {
        User user = userRepository.findByUsername("john_doe").orElse(null);
        if (user != null) {
            userRepository.delete(user);
            return "User john_doe deleted!";
        }
        return "User not found";
    }

    @GetMapping("/department-statistics")
    public String getDepartmentStats() {
        Long itEmployeeCount = employeeRepository.countEmployeesByDepartment(1L);
        BigDecimal avgSalary = employeeRepository.findAverageSalaryByDepartment(1L);
        return "IT Department: " + itEmployeeCount + " employees, Average salary: " + avgSalary;
    }
}