package com.hrsys.hr_backend.dao;

import com.hrsys.hr_backend.entity.Position;
import java.util.List;
import java.util.Optional;

public interface PositionDAO {
    List<Position> findAll();
    Optional<Position> findById(Long id);
    Position save(Position position);
    void deleteById(Long id);
    Optional<Position> findByTitle(String title);
    List<Position> findByTitleContaining(String title);
    Boolean existsByTitle(String title);
    List<Position> findByDepartmentId(Long departmentId);
    List<Position> findByDepartmentName(String departmentName);
    List<Position> findAllWithEmployees();
    List<Position> findPositionsWithEmployees();
}