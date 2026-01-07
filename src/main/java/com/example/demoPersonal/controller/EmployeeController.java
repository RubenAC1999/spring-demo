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
import java.util.UUID;

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

    @GetMapping("/{uuid}")
    public ResponseEntity<EmployeeResponseDTO> getEmployee(@PathVariable UUID uuid) {
        return ResponseEntity.ok(employeeService.getEmployeeByUuid(uuid));
    }

    @GetMapping("/me")
    public ResponseEntity<EmployeeResponseDTO> getCurrentEmployee(Authentication authentication) {
        return ResponseEntity.ok(employeeService.getCurrentEmployee(authentication.getName()));
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

        URI location = URI.create("/employees/" + created.uuid());

        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<EmployeeResponseDTO> updateEmployee(@PathVariable UUID uuid,
                                                              @RequestBody @Valid EmployeeRequestDTO dto) {
        return ResponseEntity.ok(employeeService.updateEmployee(uuid, dto));
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> removeEmployee(@PathVariable UUID uuid) {
        employeeService.removeEmployee(uuid);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{uuid}/tasks")
    public ResponseEntity<List<TaskResponseDTO>> getEmployeeTasks(@PathVariable UUID uuid) {
        return ResponseEntity.ok(employeeService.getEmployeeTasks(uuid));
    }

    @GetMapping("/me/tasks")
    public ResponseEntity<List<TaskResponseDTO>> getCurrentEmployeeTasks(Authentication authentication) {
        return ResponseEntity.ok(employeeService.getCurrentEmployeeTasks(authentication.getName()));
    }

    @PutMapping("/{uuid}/projects/{projectUuid}")
    public ResponseEntity<EmployeeResponseDTO> assignProject(@PathVariable UUID uuid, @PathVariable UUID projectUuid) {
        return ResponseEntity.ok(employeeService.assignProject(uuid, projectUuid));
    }

    @DeleteMapping("/{uuid}/projects/{projectUuid}")
    public ResponseEntity<EmployeeResponseDTO> unassignProject(@PathVariable UUID uuid, @PathVariable UUID projectUuid) {
        return ResponseEntity.ok(employeeService.unassignProject(uuid, projectUuid));
    }
}
