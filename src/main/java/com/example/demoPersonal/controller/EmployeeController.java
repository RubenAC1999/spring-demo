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
@Tag(name = "Employees", description = "Operaciones sobre empleados")
// Recomendado porque todos los métodos necesitan JWT
@SecurityRequirement(name = "bearerAuth")
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

    @Operation(
            summary = "Obtener datos del actual empleado",
            description = "Muestra los datos del empleado que ha iniciado sesión"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista los datos del empleado actual"),
            @ApiResponse(responseCode = "403", description = "El usuario no está autorizado por que no tiene JWT")
    })
    @GetMapping("/me")
    public ResponseEntity<EmployeeResponseDTO> getCurrentEmployee(Authentication authentication) {
        return ResponseEntity.ok(employeeService.getCurrentEmployee(authentication.getName()));
    }

    @Operation(
            summary = "Buscar empleado por email",
            description = "Si existe, muestra al empleado buscado por email"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Si lo encuentra muestra al usuario, si no devuelve campo vacío"),
            @ApiResponse(responseCode = "403", description = "JWT no encontrado o no tiene autorización para hacer esta petición")
    })
    @GetMapping("/search")
    public ResponseEntity<EmployeeResponseDTO> getEmployeeByEmail(@RequestParam String email) {
        return ResponseEntity.ok(employeeService.getEmployeeByEmail(email));
    }

    @Operation(
            summary = "Buscar empleado por nombre",
            description = "Si existe, muestra al empleado buscado por nombre"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Si lo encuentra muestra al usuario, si no devuelve campo vacío"),
            @ApiResponse(responseCode = "403", description = "JWT no encontrado o no tiene autorización para hacer esta petición")
    })
    @GetMapping("/search-by-name")
    public ResponseEntity<List<EmployeeResponseDTO>> getEmployeesByName(@RequestParam String name) {
        return ResponseEntity.ok(employeeService.getEmployeeByName(name));
    }

    @Operation(
            summary = "Buscar empleado por posición",
            description = "Si existe, muestra al empleado buscado por posición"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Si lo encuentra muestra al usuario, si no devuelve campo vacío"),
            @ApiResponse(responseCode = "403", description = "JWT no encontrado o no tiene autorización para hacer esta petición")
    })
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


    @Operation(
            summary = "Listar mis tareas",
            description = "Lista las tareas asignadas del usuario actual"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Muestra las tareas asignadas del usuario que ha iniciado sesión"),
            @ApiResponse(responseCode = "403", description = "El usuario no está autorizado por que no tiene JWT")
    })
    @GetMapping("/me/tasks")
    public ResponseEntity<List<TaskResponseDTO>> getCurrentEmployeeTasks(Authentication authentication) {
        return ResponseEntity.ok(employeeService.getCurrentEmployeeTasks(authentication.getName()));
    }

    @Operation(
            summary = "Asignar proyecto",
            description = "Asignar un proyecto a un empleado"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Se le ha asignado correctamente el proyecto al empleado"),
            @ApiResponse(responseCode = "403", description = "El usuario no tiene autorización o no existe el empleado/proyecto")
    })
    @PutMapping("/{uuid}/projects/{projectUuid}")
    public ResponseEntity<EmployeeResponseDTO> assignProject(@PathVariable UUID uuid, @PathVariable UUID projectUuid) {
        return ResponseEntity.ok(employeeService.assignProject(uuid, projectUuid));
    }

    @Operation(
            summary = "Desasignar proyecto",
            description = "Desasignar un proyecto a un empleado"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Se le ha desasignado correctamente el proyecto al empleado"),
            @ApiResponse(responseCode = "403", description = "El usuario no tiene autorización o no existe el empleado/proyecto")
    })
    @DeleteMapping("/{uuid}/projects/{projectUuid}")
    public ResponseEntity<EmployeeResponseDTO> unassignProject(@PathVariable UUID uuid, @PathVariable UUID projectUuid) {
        return ResponseEntity.ok(employeeService.unassignProject(uuid, projectUuid));
    }
}
