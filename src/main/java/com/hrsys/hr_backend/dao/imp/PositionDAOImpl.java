package com.hrsys.hr_backend.dao.impl;

import com.hrsys.hr_backend.dao.PositionDAO;
import com.hrsys.hr_backend.entity.Position;
import com.hrsys.hr_backend.repository.PositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public class PositionDAOImpl implements PositionDAO {

    @Autowired
    private PositionRepository positionRepository;

    @Override
    public List<Position> findAll() {
        return positionRepository.findAll();
    }

    @Override
    public Optional<Position> findById(Long id) {
        return positionRepository.findById(id);
    }

    @Override
    public Position save(Position position) {
        return positionRepository.save(position);
    }

    @Override
    public void deleteById(Long id) {
        positionRepository.deleteById(id);
    }

    @Override
    public Optional<Position> findByTitle(String title) {
        return positionRepository.findByTitle(title);
    }

    @Override
    public List<Position> findByTitleContaining(String title) {
        return positionRepository.findByTitleContaining(title);
    }

    @Override
    public Boolean existsByTitle(String title) {
        return positionRepository.existsByTitle(title);
    }

    @Override
    public List<Position> findByDepartmentId(Long departmentId) {
        return positionRepository.findByDepartmentId(departmentId);
    }

    @Override
    public List<Position> findByDepartmentName(String departmentName) {
        return positionRepository.findByDepartmentName(departmentName);
    }

    @Override
    public List<Position> findAllWithEmployees() {
        return positionRepository.findAllWithEmployees();
    }

    @Override
    public List<Position> findPositionsWithEmployees() {
        return positionRepository.findPositionsWithEmployees();
    }
}