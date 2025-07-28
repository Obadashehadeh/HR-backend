package com.hrsys.hr_backend.dao.impl;

import com.hrsys.hr_backend.dao.DepartmentDAO;
import com.hrsys.hr_backend.entity.Department;
import com.hrsys.hr_backend.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public class DepartmentDAOImpl implements DepartmentDAO {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    public List<Department> findAll() {
        return departmentRepository.findAll();
    }

    @Override
    public Optional<Department> findById(Long id) {
        return departmentRepository.findById(id);
    }

    @Override
    public Department save(Department department) {
        return departmentRepository.save(department);
    }

    @Override
    public void deleteById(Long id) {
        departmentRepository.deleteById(id);
    }

    @Override
    public Optional<Department> findByName(String name) {
        return departmentRepository.findByName(name);
    }

    @Override
    public List<Department> findByNameContaining(String name) {
        return departmentRepository.findByNameContaining(name);
    }

    @Override
    public Boolean existsByName(String name) {
        return departmentRepository.existsByName(name);
    }

    @Override
    public List<Department> findByManagerName(String managerName) {
        return departmentRepository.findByManagerName(managerName);
    }

    @Override
    public List<Department> findAllWithEmployees() {
        return departmentRepository.findAllWithEmployees();
    }

    @Override
    public List<Department> findDepartmentsWithEmployees() {
        return departmentRepository.findDepartmentsWithEmployees();
    }

    @Override
    public List<Department> findEmptyDepartments() {
        return departmentRepository.findEmptyDepartments();
    }
}