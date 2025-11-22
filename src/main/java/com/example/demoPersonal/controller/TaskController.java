package com.example.demoPersonal.controller;

import com.example.demoPersonal.dto.task.TaskRequestDTO;
import com.example.demoPersonal.dto.task.TaskResponseDTO;
import com.example.demoPersonal.entity.enums.Status;
import com.example.demoPersonal.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<List<TaskResponseDTO>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> getTaskById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    @GetMapping("/search-by-description")
    public ResponseEntity<List<TaskResponseDTO>> getTasksByDescription(@RequestParam String description) {
        return ResponseEntity.ok(taskService.getTasksByDescription(description));
    }

    @GetMapping("search-by-status")
    public ResponseEntity<List<TaskResponseDTO>> getTasksByStatus(@RequestParam Status status) {
        return ResponseEntity.ok(taskService.getTaskByStatus(status));
    }

    @GetMapping("search-unassigned")
    public ResponseEntity<List<TaskResponseDTO>> getUnassignedTasks() {
        return ResponseEntity.ok(taskService.getUnassingedTasks());
    }

    @PostMapping
    public ResponseEntity<TaskResponseDTO> createTask(@RequestBody @Valid TaskRequestDTO dto) {
        TaskResponseDTO task = taskService.createTask(dto);

        URI location = URI.create("/tasks/" + task.getId());

        return ResponseEntity.created(location).body(task);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> updateTask(@PathVariable Long id, @RequestParam @Valid TaskRequestDTO dto) {
        return ResponseEntity.ok(taskService.updateTask(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeTask(Long id) {
        taskService.removeTask(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/assign/{employeeId}")
    public ResponseEntity<TaskResponseDTO> assignTask(@PathVariable Long id, Long employeeId) {
        return ResponseEntity.ok(taskService.assignTask(id, employeeId));
    }

    @PutMapping("/{id}/unassign")
    public ResponseEntity<TaskResponseDTO> unassignTask(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.unassingTask(id));
    }
}
