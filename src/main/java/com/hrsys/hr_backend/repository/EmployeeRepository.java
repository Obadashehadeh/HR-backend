package com.hrsys.hr_backend.repository;

import com.hrsys.hr_backend.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByEmail(String email);

    List<Employee> findByDepartmentId(Long departmentId);

    List<Employee> findByPositionId(Long positionId);

    List<Employee> findByFirstNameContainingOrLastNameContaining(String firstName, String lastName);

    List<Employee> findByHireDateBetween(LocalDate startDate, LocalDate endDate);

    List<Employee> findBySalaryGreaterThan(BigDecimal salary);

    @Query("SELECT e FROM Employee e JOIN e.department d WHERE d.name = :departmentName")
    List<Employee> findByDepartmentName(@Param("departmentName") String departmentName);

    @Query("SELECT e FROM Employee e JOIN e.position p WHERE p.title = :positionTitle")
    List<Employee> findByPositionTitle(@Param("positionTitle") String positionTitle);

    @Query("SELECT AVG(e.salary) FROM Employee e WHERE e.department.id = :departmentId")
    BigDecimal findAverageSalaryByDepartment(@Param("departmentId") Long departmentId);

    @Query("SELECT COUNT(e) FROM Employee e WHERE e.department.id = :departmentId")
    Long countEmployeesByDepartment(@Param("departmentId") Long departmentId);

    @Query(value = "SELECT d.name as department_name, COUNT(e.id) as employee_count, AVG(e.salary) as avg_salary " +
            "FROM employees e JOIN departments d ON e.department_id = d.id " +
            "GROUP BY d.id, d.name", nativeQuery = true)
    List<Object[]> getDepartmentStatistics();
}