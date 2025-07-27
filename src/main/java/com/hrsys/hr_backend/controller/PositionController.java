package com.hrsys.hr_backend.controller;

import com.hrsys.hr_backend.entity.Position;
import com.hrsys.hr_backend.entity.Department;
import com.hrsys.hr_backend.repository.PositionRepository;
import com.hrsys.hr_backend.repository.DepartmentRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/positions")
@CrossOrigin(origins = "http://localhost:4200")
public class PositionController {

    @Autowired
    private PositionRepository positionRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @GetMapping
    public List<Position> getAllPositions() {
        return positionRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Position> getPositionById(@PathVariable Long id) {
        Optional<Position> position = positionRepository.findById(id);
        if (position.isPresent()) {
            return ResponseEntity.ok(position.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> createPosition(@Valid @RequestBody PositionRequest positionRequest) {
        try {
            Position position = new Position();
            position.setTitle(positionRequest.getTitle());
            position.setDescription(positionRequest.getDescription());

            if (positionRequest.getDepartmentId() != null) {
                Department department = departmentRepository.findById(positionRequest.getDepartmentId())
                        .orElseThrow(() -> new RuntimeException("Department not found"));
                position.setDepartment(department);
            }

            Position savedPosition = positionRepository.save(position);
            return ResponseEntity.ok(savedPosition);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePosition(@PathVariable Long id, @Valid @RequestBody PositionRequest positionRequest) {
        try {
            Position position = positionRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Position not found"));

            position.setTitle(positionRequest.getTitle());
            position.setDescription(positionRequest.getDescription());

            if (positionRequest.getDepartmentId() != null) {
                Department department = departmentRepository.findById(positionRequest.getDepartmentId())
                        .orElseThrow(() -> new RuntimeException("Department not found"));
                position.setDepartment(department);
            }

            Position updatedPosition = positionRepository.save(position);
            return ResponseEntity.ok(updatedPosition);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePosition(@PathVariable Long id) {
        try {
            Position position = positionRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Position not found"));

            positionRepository.delete(position);
            return ResponseEntity.ok(new MessageResponse("Position deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @GetMapping("/department/{departmentId}")
    public List<Position> getPositionsByDepartment(@PathVariable Long departmentId) {
        return positionRepository.findByDepartmentId(departmentId);
    }

    public static class PositionRequest {
        private String title;
        private String description;
        private Long departmentId;

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        public Long getDepartmentId() { return departmentId; }
        public void setDepartmentId(Long departmentId) { this.departmentId = departmentId; }
    }

    public static class MessageResponse {
        private String message;

        public MessageResponse(String message) { this.message = message; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }
}