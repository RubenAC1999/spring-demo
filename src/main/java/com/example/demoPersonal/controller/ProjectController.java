package com.example.demoPersonal.controller;

import com.example.demoPersonal.dto.employee.EmployeeResponseDTO;
import com.example.demoPersonal.dto.project.ProjectRequestDTO;
import com.example.demoPersonal.dto.project.ProjectResponseDTO;
import com.example.demoPersonal.dto.task.TaskResponseDTO;
import com.example.demoPersonal.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/projects")
public class ProjectController {
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    public ResponseEntity<List<ProjectResponseDTO>> getAllProjects() {
        return ResponseEntity.ok(projectService.getAllProjects());
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<ProjectResponseDTO> getProjectById(@PathVariable UUID uuid) {
        return ResponseEntity.ok(projectService.getProjectByUuid(uuid));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProjectResponseDTO>> getProjectsByName(@RequestParam String name) {
        return ResponseEntity.ok(projectService.getProjectsByName(name));
    }

    @PostMapping
    public ResponseEntity<ProjectResponseDTO> createProject(@RequestBody @Valid ProjectRequestDTO dto) {
        ProjectResponseDTO created = projectService.createProject(dto);

        URI location = URI.create("/projects/" + created.uuid());

        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<ProjectResponseDTO> updateProject(@PathVariable UUID uuid,
                                                            @RequestBody @Valid ProjectRequestDTO dto) {
        return ResponseEntity.ok(projectService.updateProject(uuid, dto));
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> removeProject(@PathVariable UUID uuid) {
        projectService.removeProject(uuid);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{uuid}/employees")
    public ResponseEntity<List<EmployeeResponseDTO>> getProjectEmployees(@PathVariable UUID uuid) {
        return ResponseEntity.ok(projectService.getProjectEmployees(uuid));
    }

    @GetMapping("/{uuid}/tasks")
    public ResponseEntity<List<TaskResponseDTO>> getProjectTasks(@PathVariable UUID uuid) {
        return ResponseEntity.ok(projectService.getProjectTasks(uuid));
    }
}
