package com.hrsys.hr_backend.repository;

import com.hrsys.hr_backend.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    Optional<Department> findByName(String name);

    List<Department> findByNameContaining(String name);

    Boolean existsByName(String name);

    List<Department> findByManagerName(String managerName);

    @Query("SELECT d FROM Department d LEFT JOIN FETCH d.employees")
    List<Department> findAllWithEmployees();

    @Query("SELECT d FROM Department d WHERE SIZE(d.employees) > 0")
    List<Department> findDepartmentsWithEmployees();

    @Query("SELECT d FROM Department d WHERE SIZE(d.employees) = 0")
    List<Department> findEmptyDepartments();
}