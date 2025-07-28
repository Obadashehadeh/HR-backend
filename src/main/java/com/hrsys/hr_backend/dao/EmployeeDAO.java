package com.hrsys.hr_backend.dao;

import com.hrsys.hr_backend.entity.Employee;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EmployeeDAO {
    List<Employee> findAll();
    Optional<Employee> findById(Long id);
    Employee save(Employee employee);
    void deleteById(Long id);
    Optional<Employee> findByEmail(String email);
    List<Employee> findByDepartmentId(Long departmentId);
    List<Employee> findByPositionId(Long positionId);
    List<Employee> findByFirstNameContainingOrLastNameContaining(String firstName, String lastName);
    List<Employee> findByHireDateBetween(LocalDate startDate, LocalDate endDate);
    List<Employee> findBySalaryGreaterThan(BigDecimal salary);
    List<Employee> findByDepartmentName(String departmentName);
    List<Employee> findByPositionTitle(String positionTitle);
    BigDecimal findAverageSalaryByDepartment(Long departmentId);
    Long countEmployeesByDepartment(Long departmentId);
    List<Object[]> getDepartmentStatistics();
}