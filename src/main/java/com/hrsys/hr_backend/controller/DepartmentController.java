package com.hrsys.hr_backend.controller;

import com.hrsys.hr_backend.dao.DepartmentDAO;
import com.hrsys.hr_backend.entity.Department;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/departments")
@CrossOrigin(origins = "http://localhost:4200")
public class DepartmentController {

    @Autowired
    private DepartmentDAO departmentDAO;

    @GetMapping
    public List<Department> getAllDepartments() {
        return departmentDAO.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Department> getDepartmentById(@PathVariable Long id) {
        Optional<Department> department = departmentDAO.findById(id);
        if (department.isPresent()) {
            return ResponseEntity.ok(department.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR_MANAGER')")
    public ResponseEntity<?> createDepartment(@Valid @RequestBody DepartmentRequest departmentRequest) {
        try {
            if (departmentDAO.existsByName(departmentRequest.getName())) {
                return ResponseEntity.badRequest().body(new MessageResponse("Department name already exists"));
            }

            Department department = new Department();
            department.setName(departmentRequest.getName());
            department.setDescription(departmentRequest.getDescription());
            department.setManagerName(departmentRequest.getManagerName());

            Department savedDepartment = departmentDAO.save(department);
            return ResponseEntity.ok(savedDepartment);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR_MANAGER')")
    public ResponseEntity<?> updateDepartment(@PathVariable Long id, @Valid @RequestBody DepartmentRequest departmentRequest) {
        try {
            Department department = departmentDAO.findById(id)
                    .orElseThrow(() -> new RuntimeException("Department not found"));

            Optional<Department> existingDept = departmentDAO.findByName(departmentRequest.getName());
            if (existingDept.isPresent() && !existingDept.get().getId().equals(id)) {
                return ResponseEntity.badRequest().body(new MessageResponse("Department name already exists"));
            }

            department.setName(departmentRequest.getName());
            department.setDescription(departmentRequest.getDescription());
            department.setManagerName(departmentRequest.getManagerName());

            Department updatedDepartment = departmentDAO.save(department);
            return ResponseEntity.ok(updatedDepartment);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR_MANAGER')")
    public ResponseEntity<?> deleteDepartment(@PathVariable Long id) {
        try {
            Department department = departmentDAO.findById(id)
                    .orElseThrow(() -> new RuntimeException("Department not found"));

            departmentDAO.deleteById(id);
            return ResponseEntity.ok(new MessageResponse("Department deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @GetMapping("/search")
    public List<Department> searchDepartments(@RequestParam String name) {
        return departmentDAO.findByNameContaining(name);
    }

    @GetMapping("/manager/{managerName}")
    public List<Department> getDepartmentsByManager(@PathVariable String managerName) {
        return departmentDAO.findByManagerName(managerName);
    }

    @GetMapping("/with-employees")
    public List<Department> getDepartmentsWithEmployees() {
        return departmentDAO.findDepartmentsWithEmployees();
    }

    @GetMapping("/empty")
    public List<Department> getEmptyDepartments() {
        return departmentDAO.findEmptyDepartments();
    }

    public static class DepartmentRequest {
        private String name;
        private String description;
        private String managerName;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        public String getManagerName() { return managerName; }
        public void setManagerName(String managerName) { this.managerName = managerName; }
    }

    public static class MessageResponse {
        private String message;

        public MessageResponse(String message) { this.message = message; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }
}