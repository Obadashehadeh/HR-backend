package com.hrsys.hr_backend.dao.impl;

import com.hrsys.hr_backend.dao.EmployeeDAO;
import com.hrsys.hr_backend.entity.Employee;
import com.hrsys.hr_backend.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class EmployeeDAOImpl implements EmployeeDAO {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    @Override
    public Optional<Employee> findById(Long id) {
        return employeeRepository.findById(id);
    }

    @Override
    public Employee save(Employee employee) {
        return employeeRepository.save(employee);
    }

    @Override
    public void deleteById(Long id) {
        employeeRepository.deleteById(id);
    }

    @Override
    public Optional<Employee> findByEmail(String email) {
        return employeeRepository.findByEmail(email);
    }

    @Override
    public List<Employee> findByDepartmentId(Long departmentId) {
        return employeeRepository.findByDepartmentId(departmentId);
    }

    @Override
    public List<Employee> findByPositionId(Long positionId) {
        return employeeRepository.findByPositionId(positionId);
    }

    @Override
    public List<Employee> findByFirstNameContainingOrLastNameContaining(String firstName, String lastName) {
        return employeeRepository.findByFirstNameContainingOrLastNameContaining(firstName, lastName);
    }

    @Override
    public List<Employee> findByHireDateBetween(LocalDate startDate, LocalDate endDate) {
        return employeeRepository.findByHireDateBetween(startDate, endDate);
    }

    @Override
    public List<Employee> findBySalaryGreaterThan(BigDecimal salary) {
        return employeeRepository.findBySalaryGreaterThan(salary);
    }

    @Override
    public List<Employee> findByDepartmentName(String departmentName) {
        return employeeRepository.findByDepartmentName(departmentName);
    }

    @Override
    public List<Employee> findByPositionTitle(String positionTitle) {
        return employeeRepository.findByPositionTitle(positionTitle);
    }

    @Override
    public BigDecimal findAverageSalaryByDepartment(Long departmentId) {
        return employeeRepository.findAverageSalaryByDepartment(departmentId);
    }

    @Override
    public Long countEmployeesByDepartment(Long departmentId) {
        return employeeRepository.countEmployeesByDepartment(departmentId);
    }

    @Override
    public List<Object[]> getDepartmentStatistics() {
        return employeeRepository.getDepartmentStatistics();
    }
}