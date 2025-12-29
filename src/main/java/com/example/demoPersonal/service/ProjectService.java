package com.example.demoPersonal.service;

import com.example.demoPersonal.dto.employee.EmployeeResponseDTO;
import com.example.demoPersonal.dto.project.ProjectRequestDTO;
import com.example.demoPersonal.dto.project.ProjectResponseDTO;
import com.example.demoPersonal.dto.task.TaskResponseDTO;
import com.example.demoPersonal.entity.Project;
import com.example.demoPersonal.exception.ProjectNotFoundException;
import com.example.demoPersonal.mapper.employee.EmployeeMapper;
import com.example.demoPersonal.mapper.project.ProjectMapper;
import com.example.demoPersonal.mapper.task.TaskMapper;
import com.example.demoPersonal.repository.ProjectRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;

    private final ProjectMapper projectMapper;
    private final TaskMapper taskMapper;
    private final EmployeeMapper employeeMapper;

    private static final Logger log = LoggerFactory.getLogger(ProjectService.class);

    public ProjectService(ProjectRepository projectRepository, ProjectMapper projectMapper, TaskMapper taskMapper,
                          EmployeeMapper employeeMapper) {
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
        this.taskMapper = taskMapper;
        this.employeeMapper = employeeMapper;
    }

    private Project findProjectOrThrow(UUID uuid) {
        log.debug("Searching project with uuid = {}", uuid);

        return projectRepository.findByUuid(uuid).orElseThrow(() -> new ProjectNotFoundException(uuid));
    }

    public ProjectResponseDTO createProject(ProjectRequestDTO dto) {
        Project project = new Project();
        project.setName(dto.name());

        Project saved = projectRepository.save(project);

        log.info("Project {} (id={}) created successfully.", saved.getName(), saved.getId());
        return projectMapper.toDTO(saved);
    }

    public ProjectResponseDTO getProjectByUuid(UUID uuid) {
        Project project = findProjectOrThrow(uuid);

        return projectMapper.toDTO(project);
    }

    public List<ProjectResponseDTO> getAllProjects() {
        return projectRepository.findAll().stream().map(projectMapper::toDTO).toList();
    }

    public List<ProjectResponseDTO> getProjectsByName(String name) {
        return projectRepository.findByName(name).stream().map(projectMapper::toDTO).toList();
    }

    public ProjectResponseDTO updateProject(UUID uuid, ProjectRequestDTO dto) {
        Project project = findProjectOrThrow(uuid);

        project.setName(dto.name());

        Project updated = projectRepository.save(project);

        log.info("Project {} (id={}) updated successfully.", updated.getName(), updated.getId());

        return projectMapper.toDTO(updated);
    }

    public void removeProject(UUID uuid) {
        Project project = findProjectOrThrow(uuid);

        projectRepository.delete(project);

        log.info("Project {} (UUID={}) removed successfully.", project.getName(), project.getId());
    }

    public List<TaskResponseDTO> getProjectTasks(UUID uuid) {
        Project project = findProjectOrThrow(uuid);

        return project.getTasks().stream().map(taskMapper::toDTO).toList();
    }

    public List<EmployeeResponseDTO> getProjectEmployees(UUID uuid) {
        Project project = findProjectOrThrow(uuid);

        return project.getEmployees().stream().map(employeeMapper::toDTO).toList();
    }

}
