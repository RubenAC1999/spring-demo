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

@RestController
@RequestMapping("/projects")
public class ProjectController {
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    public ResponseEntity<List<ProjectResponseDTO>> getAllProjects() {
        return ResponseEntity.ok(projectService.getAllProjects());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponseDTO> getProjectById(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.getProjectById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProjectResponseDTO>> getProjectsByName(@RequestParam String name) {
        return ResponseEntity.ok(projectService.getProjectsByName(name));
    }

    @PostMapping
    public ResponseEntity<ProjectResponseDTO> createProject(@RequestBody @Valid ProjectRequestDTO dto) {
        ProjectResponseDTO created = projectService.createProject(dto);

        URI location = URI.create("/projects/" + created.getId());

        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectResponseDTO> updateProject(@PathVariable Long id,
                                                            @RequestBody @Valid ProjectRequestDTO dto) {
        return ResponseEntity.ok(projectService.updateProject(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeProject(@PathVariable Long id) {
        projectService.removeProject(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/employees")
    public ResponseEntity<List<EmployeeResponseDTO>> getProjectEmployees(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.getProjectEmployees(id));
    }

    @GetMapping("/{id}/tasks")
    public ResponseEntity<List<TaskResponseDTO>> getProjectTasks(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.getProjectTasks(id));
    }
}
