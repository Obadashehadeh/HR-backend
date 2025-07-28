package com.hrsys.hr_backend.dao;

import com.hrsys.hr_backend.entity.Department;
import java.util.List;
import java.util.Optional;

public interface DepartmentDAO {
    List<Department> findAll();
    Optional<Department> findById(Long id);
    Department save(Department department);
    void deleteById(Long id);
    Optional<Department> findByName(String name);
    List<Department> findByNameContaining(String name);
    Boolean existsByName(String name);
    List<Department> findByManagerName(String managerName);
    List<Department> findAllWithEmployees();
    List<Department> findDepartmentsWithEmployees();
    List<Department> findEmptyDepartments();
}