package com.example.demoPersonal.controller;

import com.example.demoPersonal.dto.task.TaskRequestDTO;
import com.example.demoPersonal.dto.task.TaskResponseDTO;
import com.example.demoPersonal.entity.enums.Status;
import com.example.demoPersonal.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<List<TaskResponseDTO>> getAllTasks(Pageable pageable) {
        return ResponseEntity.ok(taskService.getAllTasks(pageable));
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<TaskResponseDTO> getTaskByUuid(@PathVariable UUID uuid) {
        return ResponseEntity.ok(taskService.getTaskByUuid(uuid));
    }

    @GetMapping("/search-by-description")
    public ResponseEntity<List<TaskResponseDTO>> getTasksByDescription(@RequestParam String description) {
        return ResponseEntity.ok(taskService.getTasksByDescription(description));
    }

    @GetMapping("search-by-status")
    public ResponseEntity<List<TaskResponseDTO>> getTasksByStatus(@RequestParam Status status) {
        return ResponseEntity.ok(taskService.getTaskByStatus(status));
    }


    @Operation(
            summary = "Listar tareas sin asignar",
            description = "Muestra las tareas en las que no hay ningún empleado asinado"
    )
    @ApiResponse(responseCode = "200", description = "Lista si hay tareas sin asignar, si no, devuelve una lista vacía")
    @GetMapping("search-unassigned")
    public ResponseEntity<List<TaskResponseDTO>> getUnassignedTasks() {
        return ResponseEntity.ok(taskService.getUnassingedTasks());
    }

    @PostMapping
    public ResponseEntity<TaskResponseDTO> createTask(@RequestBody @Valid TaskRequestDTO dto) {
        TaskResponseDTO task = taskService.createTask(dto);

        URI location = URI.create("/tasks/" + task.uuid());

        return ResponseEntity.created(location).body(task);
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<TaskResponseDTO> updateTask(@PathVariable UUID uuid, @RequestBody @Valid TaskRequestDTO dto) {
        return ResponseEntity.ok(taskService.updateTask(uuid, dto));
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> removeTask(@PathVariable UUID uuid) {
        taskService.removeTask(uuid);

        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Asignar tarea",
            description = "Asigna la tarea a un emploeado"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Se asigna la tarea a un empleado correctamente"),
            @ApiResponse(responseCode = "403", description = "Error de autorización, JWT no válido")
    })
    @PutMapping("/{uuid}/employees/{employeeUuid}")
    public ResponseEntity<TaskResponseDTO> assignTask(@PathVariable UUID uuid, @PathVariable UUID employeeUuid) {
        return ResponseEntity.ok(taskService.assignTask(uuid, employeeUuid));
    }

    @Operation(
            summary = "Desasignar tarea",
            description = "Desasigna la tarea a un emploeado"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Se desasigna la tarea a un empleado correctamente"),
            @ApiResponse(responseCode = "403", description = "Error de autorización, JWT no válido")
    })
    @PutMapping("/{uuid}/employees")
    public ResponseEntity<TaskResponseDTO> unassignTask(@PathVariable UUID uuid) {
        return ResponseEntity.ok(taskService.unassingTask(uuid));
    }
}
