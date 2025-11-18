package com.example.demoPersonal.service;

import com.example.demoPersonal.dto.task.TaskRequestDTO;
import com.example.demoPersonal.dto.task.TaskResponseDTO;
import com.example.demoPersonal.entity.Project;
import com.example.demoPersonal.entity.Task;
import com.example.demoPersonal.exception.ProjectNotFoundException;
import com.example.demoPersonal.exception.TaskNotFoundException;
import com.example.demoPersonal.repository.ProjectRepository;
import com.example.demoPersonal.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;

    public TaskService(TaskRepository taskRepository, ProjectRepository projectRepository) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
    }

    private TaskResponseDTO mapTaskToResponse(Task task) {
        Long employeeId = task.getEmployee() != null ? task.getEmployee().getId() : null;
        Long projectId = task.getProject().getId();

        return new TaskResponseDTO(
                task.getId(),
                task.getDescription(),
                task.getStatus(),
                task.getCreatedAt(),
                task.getUpdatedAt(),
                employeeId,
                projectId
        );
    }

    private Task findTaskOrThrow(Long id) {
        return taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
    }

    public TaskResponseDTO createTask(TaskRequestDTO dto) {
        Project project = projectRepository.findById(dto.getProjectId()).orElseThrow(() ->
                new ProjectNotFoundException(dto.getProjectId()));

        Task task = new Task(
                dto.getDescription(),
                project
        );

        Task saved = taskRepository.save(task);
        return mapTaskToResponse(saved);
    }

    public TaskResponseDTO getTaskById(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));

        return mapTaskToResponse(task);
    }

    public List<TaskResponseDTO> getAllTasks(Long id) {
        return taskRepository.findAll().stream().map(this::mapTaskToResponse).toList();
    }

    public TaskResponseDTO updateTask(Long id, TaskRequestDTO dto) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
        Project project = projectRepository.findById(dto.getProjectId()).orElseThrow(() ->
                new ProjectNotFoundException(dto.getProjectId()));

        task.setDescription(dto.getDescription());
        task.setStatus(dto.getStatus());
        task.setProject(project);

        Task updated = taskRepository.save(task);

        return mapTaskToResponse(updated);
    }


}
