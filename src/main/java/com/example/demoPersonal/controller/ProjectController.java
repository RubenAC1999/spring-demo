package com.example.demoPersonal.controller;

import com.example.demoPersonal.dto.employee.EmployeeResponseDTO;
import com.example.demoPersonal.dto.project.ProjectRequestDTO;
import com.example.demoPersonal.dto.project.ProjectResponseDTO;
import com.example.demoPersonal.dto.task.TaskResponseDTO;
import com.example.demoPersonal.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/projects")
@Tag(name = "Projects", description = "Project operations")
@SecurityRequirement(name = "bearerAuth")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @Operation(summary = "Get all projects")
    @ApiResponse(responseCode = "200", description = "Get projects")
    @GetMapping
    public ResponseEntity<List<ProjectResponseDTO>> getAllProjects() {
        return ResponseEntity.ok(projectService.getAllProjects());
    }

    @Operation(summary = "Get project by Uuid")
    @ApiResponse(responseCode = "200", description = "Get project by uuid")
    @ApiResponse(responseCode = "400", description = "Project not found")
    @GetMapping("/{uuid}")
    public ResponseEntity<ProjectResponseDTO> getProjectByUuid(@PathVariable UUID uuid) {
        return ResponseEntity.ok(projectService.getProjectByUuid(uuid));
    }

    @Operation(summary = "Get project by name")
    @ApiResponse(responseCode = "200", description = "Get project by name")
    @ApiResponse(responseCode = "400", description = "Project not found")
    @GetMapping("/search")
    public ResponseEntity<List<ProjectResponseDTO>> getProjectsByName(
            @RequestParam String name) {

        return ResponseEntity.ok(projectService.getProjectsByName(name));
    }

    @Operation(summary = "Create a project")
    @ApiResponse(responseCode = "201", description = "Project created")
    @PostMapping
    public ResponseEntity<ProjectResponseDTO> createProject(
            @RequestBody @Valid ProjectRequestDTO dto) {

        ProjectResponseDTO created = projectService.createProject(dto);

        URI location = URI.create("/projects/" + created.uuid());

        return ResponseEntity
                .created(location)
                .body(created);
    }

    @Operation(summary = "Update a project")
    @ApiResponse(responseCode = "201", description = "Project updated")
    @ApiResponse(responseCode = "400", description = "Project not found")
    @PutMapping("/{uuid}")
    public ResponseEntity<ProjectResponseDTO> updateProject(
            @PathVariable UUID uuid,
            @RequestBody @Valid ProjectRequestDTO dto) {

        return ResponseEntity.ok(projectService.updateProject(uuid, dto));
    }

    @Operation(summary = "Remove a project")
    @ApiResponse(responseCode = "204", description = "Project removed")
    @ApiResponse(responseCode = "400", description = "Project not found")
    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> removeProject(@PathVariable UUID uuid) {
        projectService.removeProject(uuid);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get project employees")
    @ApiResponse(responseCode = "200", description = "Get project employees list")
    @ApiResponse(responseCode = "404", description = "Project not found")
    @GetMapping("/{uuid}/employees")
    public ResponseEntity<List<EmployeeResponseDTO>> getProjectEmployees(
            @PathVariable UUID uuid) {

        return ResponseEntity.ok(projectService.getProjectEmployees(uuid));
    }

    @Operation(summary = "Get project tasks")
    @ApiResponse(responseCode = "200", description = "Get project tasks list")
    @ApiResponse(responseCode = "404", description = "Project not found")
    @GetMapping("/{uuid}/tasks")
    public ResponseEntity<List<TaskResponseDTO>> getProjectTasks(
            @PathVariable UUID uuid) {

        return ResponseEntity.ok(projectService.getProjectTasks(uuid));
    }
}
