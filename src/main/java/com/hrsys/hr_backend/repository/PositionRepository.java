package com.hrsys.hr_backend.repository;

import com.hrsys.hr_backend.entity.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PositionRepository extends JpaRepository<Position, Long> {

    Optional<Position> findByTitle(String title);

    List<Position> findByTitleContaining(String title);

    Boolean existsByTitle(String title);

    List<Position> findByDepartmentId(Long departmentId);

    @Query("SELECT p FROM Position p JOIN p.department d WHERE d.name = :departmentName")
    List<Position> findByDepartmentName(@Param("departmentName") String departmentName);

    @Query("SELECT p FROM Position p LEFT JOIN FETCH p.employees")
    List<Position> findAllWithEmployees();

    @Query("SELECT p FROM Position p WHERE SIZE(p.employees) > 0")
    List<Position> findPositionsWithEmployees();
}