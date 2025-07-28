package com.hrsys.hr_backend.controller;

import com.hrsys.hr_backend.dao.PositionDAO;
import com.hrsys.hr_backend.dao.DepartmentDAO;
import com.hrsys.hr_backend.entity.Position;
import com.hrsys.hr_backend.entity.Department;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/positions")
@CrossOrigin(origins = "http://localhost:4200")
public class PositionController {

    @Autowired
    private PositionDAO positionDAO;

    @Autowired
    private DepartmentDAO departmentDAO;

    @GetMapping
    public List<Position> getAllPositions() {
        return positionDAO.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Position> getPositionById(@PathVariable Long id) {
        Optional<Position> position = positionDAO.findById(id);
        if (position.isPresent()) {
            return ResponseEntity.ok(position.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR_MANAGER')")
    public ResponseEntity<?> createPosition(@Valid @RequestBody PositionRequest positionRequest) {
        try {
            if (positionDAO.existsByTitle(positionRequest.getTitle())) {
                return ResponseEntity.badRequest().body(new MessageResponse("Position title already exists"));
            }

            Position position = new Position();
            position.setTitle(positionRequest.getTitle());
            position.setDescription(positionRequest.getDescription());

            if (positionRequest.getDepartmentId() != null) {
                Department department = departmentDAO.findById(positionRequest.getDepartmentId())
                        .orElseThrow(() -> new RuntimeException("Department not found"));
                position.setDepartment(department);
            }

            Position savedPosition = positionDAO.save(position);
            return ResponseEntity.ok(savedPosition);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR_MANAGER')")
    public ResponseEntity<?> updatePosition(@PathVariable Long id, @Valid @RequestBody PositionRequest positionRequest) {
        try {
            Position position = positionDAO.findById(id)
                    .orElseThrow(() -> new RuntimeException("Position not found"));

            Optional<Position> existingPosition = positionDAO.findByTitle(positionRequest.getTitle());
            if (existingPosition.isPresent() && !existingPosition.get().getId().equals(id)) {
                return ResponseEntity.badRequest().body(new MessageResponse("Position title already exists"));
            }

            position.setTitle(positionRequest.getTitle());
            position.setDescription(positionRequest.getDescription());

            if (positionRequest.getDepartmentId() != null) {
                Department department = departmentDAO.findById(positionRequest.getDepartmentId())
                        .orElseThrow(() -> new RuntimeException("Department not found"));
                position.setDepartment(department);
            }

            Position updatedPosition = positionDAO.save(position);
            return ResponseEntity.ok(updatedPosition);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR_MANAGER')")
    public ResponseEntity<?> deletePosition(@PathVariable Long id) {
        try {
            Position position = positionDAO.findById(id)
                    .orElseThrow(() -> new RuntimeException("Position not found"));

            positionDAO.deleteById(id);
            return ResponseEntity.ok(new MessageResponse("Position deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @GetMapping("/department/{departmentId}")
    public List<Position> getPositionsByDepartment(@PathVariable Long departmentId) {
        return positionDAO.findByDepartmentId(departmentId);
    }

    @GetMapping("/department/name/{departmentName}")
    public List<Position> getPositionsByDepartmentName(@PathVariable String departmentName) {
        return positionDAO.findByDepartmentName(departmentName);
    }

    @GetMapping("/search")
    public List<Position> searchPositions(@RequestParam String title) {
        return positionDAO.findByTitleContaining(title);
    }

    @GetMapping("/with-employees")
    public List<Position> getPositionsWithEmployees() {
        return positionDAO.findPositionsWithEmployees();
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