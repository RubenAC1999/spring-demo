package com.example.demoPersonal.controller;

import com.example.demoPersonal.dto.employee.EmployeeRequestDTO;
import com.example.demoPersonal.dto.employee.EmployeeResponseDTO;
import com.example.demoPersonal.dto.task.TaskResponseDTO;
import com.example.demoPersonal.entity.enums.Position;
import com.example.demoPersonal.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public ResponseEntity<List<EmployeeResponseDTO>> getAllEmployees(Pageable pageable) {
       return ResponseEntity.ok(employeeService.getAllEmployees(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponseDTO> getEmployee(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<EmployeeResponseDTO> getEmployeeByEmail(@RequestParam String email) {
        return ResponseEntity.ok(employeeService.getEmployeeByEmail(email));
    }

    @GetMapping("/search-by-name")
    public ResponseEntity<List<EmployeeResponseDTO>> getEmployeesByName(@RequestParam String name) {
        return ResponseEntity.ok(employeeService.getEmployeeByName(name));
    }

    @GetMapping("/search-by-position")
    public ResponseEntity<List<EmployeeResponseDTO>> getEmployeesByPosition(@RequestParam Position position) {
        return ResponseEntity.ok(employeeService.getEmployeesByPosition(position));
    }

    @PostMapping
    public ResponseEntity<EmployeeResponseDTO> createEmployee(@RequestBody @Valid EmployeeRequestDTO dto) {
        EmployeeResponseDTO created = employeeService.createEmployee(dto);

        URI location = URI.create("/employees/" + created.id());

        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponseDTO> updateEmployee(@PathVariable Long id,
                                                              @RequestBody @Valid EmployeeRequestDTO dto) {
        return ResponseEntity.ok(employeeService.updateEmployee(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeEmployee(@PathVariable Long id) {
        employeeService.removeEmployee(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/tasks")
    public ResponseEntity<List<TaskResponseDTO>> getEmployeeTasks(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.getEmployeeTasks(id));
    }

    @PutMapping("/{id}/assignProject/{projectId}")
    public ResponseEntity<EmployeeResponseDTO> assignProject(@PathVariable Long id, @PathVariable Long projectId) {
        return ResponseEntity.ok(employeeService.assignProject(id, projectId));
    }

    @PutMapping("/{id}/unassignProject/{projectId}")
    public ResponseEntity<EmployeeResponseDTO> unassignProject(@PathVariable Long id, @PathVariable Long projectId) {
        return ResponseEntity.ok(employeeService.unassignProject(id, projectId));
    }
}
