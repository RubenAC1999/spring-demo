package com.example.demoPersonal.controller;

import com.example.demoPersonal.dto.employee.EmployeeRequestDTO;
import com.example.demoPersonal.dto.employee.EmployeeResponseDTO;
import com.example.demoPersonal.dto.task.TaskResponseDTO;
import com.example.demoPersonal.entity.enums.Position;
import com.example.demoPersonal.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/employees")
@Tag(name = "Employees", description = "Employee operations")
// Recomendado porque todos los métodos necesitan JWT
@SecurityRequirement(name = "bearerAuth")
public class EmployeeController {
    private final EmployeeService employeeService;
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    @Operation(
            summary = "Get all employees"
    )
    @ApiResponse(responseCode = "200", description = "Show all employees")
    public ResponseEntity<List<EmployeeResponseDTO>> getAllEmployees(Pageable pageable) {
       return ResponseEntity.ok(employeeService.getAllEmployees(pageable));
    }


    @GetMapping("/{uuid}")
    @Operation(
            summary = "Get an employee"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Get the employee"),
            @ApiResponse(responseCode = "400", description = "Employee not found")
    })
    public ResponseEntity<EmployeeResponseDTO> getEmployee(@PathVariable UUID uuid) {
        return ResponseEntity.ok(employeeService.getEmployeeByUuid(uuid));
    }


    @Operation(
            summary = "Get current employee data"
    )
    @ApiResponse(responseCode = "200", description = "Show current employee data")
    @GetMapping("/me")
    public ResponseEntity<EmployeeResponseDTO> getCurrentEmployee(Authentication authentication) {
        return ResponseEntity.ok(employeeService.getCurrentEmployee(authentication.getName()));
    }


    @Operation(
            summary = "Get employee by email"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Employee found"),
            @ApiResponse(responseCode = "400", description = "Employee not exists"),
    })
    @GetMapping("/search")
    public ResponseEntity<EmployeeResponseDTO> getEmployeeByEmail(@RequestParam String email) {
        return ResponseEntity.ok(employeeService.getEmployeeByEmail(email));
    }


    @Operation(
            summary = "Get employee by name"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Employee found"),
            @ApiResponse(responseCode = "400", description = "Employee not exists"),
    })
    @GetMapping("/search-by-name")
    public ResponseEntity<List<EmployeeResponseDTO>> getEmployeesByName(@RequestParam String name) {
        return ResponseEntity.ok(employeeService.getEmployeeByName(name));
    }


    @Operation(
            summary = "List employees by position"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List all employees or an empty list"),
    })
    @GetMapping("/search-by-position")
    public ResponseEntity<List<EmployeeResponseDTO>> getEmployeesByPosition(@RequestParam Position position) {
        return ResponseEntity.ok(employeeService.getEmployeesByPosition(position));
    }


    @Operation(
        summary = "Create an employee"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Create an employee"),
            @ApiResponse(responseCode = "409", description = "An employee with same email exists")
    })
    @PostMapping
    public ResponseEntity<EmployeeResponseDTO> createEmployee(@RequestBody @Valid EmployeeRequestDTO dto) {
        EmployeeResponseDTO created = employeeService.createEmployee(dto);

        URI location = URI.create("/employees/" + created.uuid());

        return ResponseEntity.created(location).body(created);
    }


    @Operation(
            summary = "Update an employee"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Employee updated"),
            @ApiResponse(responseCode = "400", description = "Employee not found"),
            @ApiResponse(responseCode = "409", description = "An employee with the same email exists")
    })
    @PutMapping("/{uuid}")
    public ResponseEntity<EmployeeResponseDTO> updateEmployee(@PathVariable UUID uuid,
                                                              @RequestBody @Valid EmployeeRequestDTO dto) {
        return ResponseEntity.ok(employeeService.updateEmployee(uuid, dto));
    }


    @Operation(
            summary = "Delete an employee"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Employee deleted"),
            @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> removeEmployee(@PathVariable UUID uuid) {
        employeeService.removeEmployee(uuid);

        return ResponseEntity.noContent().build();
    }


   @Operation(
           summary = "Get employee tasks"
   )
   @ApiResponses({
           @ApiResponse(responseCode = "200", description = "Employee found"),
           @ApiResponse(responseCode = "404", description = "Employee not found")
   })
    @GetMapping("/{uuid}/tasks")
    public ResponseEntity<List<TaskResponseDTO>> getEmployeeTasks(@PathVariable UUID uuid) {
        return ResponseEntity.ok(employeeService.getEmployeeTasks(uuid));
    }


    @Operation(
            summary = "Get my tasks"
    )
    @ApiResponse(responseCode = "200", description = "Show the tasks")
    @GetMapping("/me/tasks")
    public ResponseEntity<List<TaskResponseDTO>> getCurrentEmployeeTasks(Authentication authentication) {
        return ResponseEntity.ok(employeeService.getCurrentEmployeeTasks(authentication.getName()));
    }


    @Operation(
            summary = "Assign a project"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Project assigned"),
            @ApiResponse(responseCode = "404", description = "Project or employee not found")
    })
    @PutMapping("/{uuid}/projects/{projectUuid}")
    public ResponseEntity<EmployeeResponseDTO> assignProject(@PathVariable UUID uuid, @PathVariable UUID projectUuid) {
        return ResponseEntity.ok(employeeService.assignProject(uuid, projectUuid));
    }


    @Operation(
            summary = "Unassign project"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Project unassigned"),
            @ApiResponse(responseCode = "400", description = "Project or employee not found")
    })
    @DeleteMapping("/{uuid}/projects/{projectUuid}")
    public ResponseEntity<EmployeeResponseDTO> unassignProject(@PathVariable UUID uuid, @PathVariable UUID projectUuid) {
        return ResponseEntity.ok(employeeService.unassignProject(uuid, projectUuid));
    }
}
