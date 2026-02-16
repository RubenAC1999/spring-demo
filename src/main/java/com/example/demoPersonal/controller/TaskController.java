package com.example.demoPersonal.controller;

import com.example.demoPersonal.dto.task.TaskRequestDTO;
import com.example.demoPersonal.dto.task.TaskResponseDTO;
import com.example.demoPersonal.entity.enums.Status;
import com.example.demoPersonal.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tasks")
@Tag(name = "Tasks", description = "Tasks operations")
@SecurityRequirement(name = "bearerAuth")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @Operation(summary = "Get all tasks")
    @ApiResponse(responseCode = "200", description = "Get tasks")
    @GetMapping
    public ResponseEntity<List<TaskResponseDTO>> getAllTasks(Pageable pageable) {
        return ResponseEntity.ok(taskService.getAllTasks(pageable));
    }

    @Operation(summary = "Get task by uuid")
    @ApiResponse(responseCode = "200", description = "Task found")
    @ApiResponse(responseCode = "400", description = "Task not found")
    @GetMapping("/{uuid}")
    public ResponseEntity<TaskResponseDTO> getTaskByUuid(@PathVariable UUID uuid) {
        return ResponseEntity.ok(taskService.getTaskByUuid(uuid));
    }

    @Operation(summary = "Get task by description")
    @ApiResponse(responseCode = "200", description = "Task found")
    @ApiResponse(responseCode = "400", description = "Task not found")
    @GetMapping("/search-by-description")
    public ResponseEntity<List<TaskResponseDTO>> getTasksByDescription(
            @RequestParam String description) {

        return ResponseEntity.ok(taskService.getTasksByDescription(description));
    }

    @Operation(summary = "Get task by status")
    @ApiResponse(responseCode = "200", description = "Task found")
    @ApiResponse(responseCode = "400", description = "Task not found")
    @GetMapping("/search-by-status")
    public ResponseEntity<List<TaskResponseDTO>> getTasksByStatus(
            @RequestParam Status status) {

        return ResponseEntity.ok(taskService.getTaskByStatus(status));
    }

    @Operation(summary = "Get task unassigned tasks")
    @ApiResponse(responseCode = "200", description = "Get unassigned tasks")
    @GetMapping("/search-unassigned")
    public ResponseEntity<List<TaskResponseDTO>> getUnassignedTasks() {
        return ResponseEntity.ok(taskService.getUnassingedTasks());
    }

    @Operation(summary = "Create a task")
    @ApiResponse(responseCode = "201", description = "Task created")
    @PostMapping
    public ResponseEntity<TaskResponseDTO> createTask(
            @RequestBody @Valid TaskRequestDTO dto) {

        TaskResponseDTO task = taskService.createTask(dto);

        URI location = URI.create("/tasks/" + task.uuid());

        return ResponseEntity
                .created(location)
                .body(task);
    }

    @Operation(summary = "Update task")
    @ApiResponse(responseCode = "201", description = "Task updated")
    @ApiResponse(responseCode = "400", description = "Task not found")
    @PutMapping("/{uuid}")
    public ResponseEntity<TaskResponseDTO> updateTask(
            @PathVariable UUID uuid,
            @RequestBody @Valid TaskRequestDTO dto) {

        return ResponseEntity.ok(taskService.updateTask(uuid, dto));
    }

    @Operation(summary = "Delete a task")
    @ApiResponse(responseCode = "204", description = "Task deleted")
    @ApiResponse(responseCode = "400", description = "Task not found")
    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> removeTask(@PathVariable UUID uuid) {
        taskService.removeTask(uuid);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Assign a task")
    @ApiResponse(responseCode = "200", description = "Task assigned to an employee")
    @ApiResponse(responseCode = "400", description = "Task or employee not found")
    @PutMapping("/{uuid}/employees/{employeeUuid}")
    public ResponseEntity<TaskResponseDTO> assignTask(
            @PathVariable UUID uuid,
            @PathVariable UUID employeeUuid) {

        return ResponseEntity.ok(taskService.assignTask(uuid, employeeUuid));
    }

    @Operation(summary = "Unassign a task")
    @ApiResponse(responseCode = "200", description = "Task unassigned to an employee")
    @ApiResponse(responseCode = "400", description = "Task or employee not found")
    @PutMapping("/{uuid}/employees")
    public ResponseEntity<TaskResponseDTO> unassignTask(@PathVariable UUID uuid) {
        return ResponseEntity.ok(taskService.unassingTask(uuid));
    }
}
