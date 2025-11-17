package com.example.demoPersonal.service;

import com.example.demoPersonal.dto.project.ProjectRequestDTO;
import com.example.demoPersonal.dto.project.ProjectResponseDTO;
import com.example.demoPersonal.entity.Project;
import com.example.demoPersonal.exception.ProjectNotFoundException;
import com.example.demoPersonal.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    private ProjectResponseDTO mapProjectToResponse(Project project) {
        return new ProjectResponseDTO(
                project.getId(),
                project.getName()
        );
    }

    private Project findProjectOrThrow(Long id) {
        return projectRepository.findById(id).orElseThrow(() -> new ProjectNotFoundException(id));
    }

    public ProjectResponseDTO createProject(ProjectRequestDTO dto) {
        Project project = new Project(
                dto.getName()
        );

        Project saved = projectRepository.save(project);

        return mapProjectToResponse(saved);
    }

    public ProjectResponseDTO getProjectById(Long id) {
        Project project = findProjectOrThrow(id);

        return mapProjectToResponse(project);
    }

    public List<ProjectResponseDTO> getAllProjects() {
        return projectRepository.findAll().stream().map(this::mapProjectToResponse).toList();
    }

    public List<ProjectResponseDTO> getProjectsByName(String name) {
        return projectRepository.findByName(name).stream().map(this::mapProjectToResponse).toList();
    }

    public ProjectResponseDTO updateProject(Long id, ProjectRequestDTO dto) {
        Project project = findProjectOrThrow(id);

        project.setName(dto.getName());

        Project updated = projectRepository.save(project);

        return mapProjectToResponse(updated);
    }



}
