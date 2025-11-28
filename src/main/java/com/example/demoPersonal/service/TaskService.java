package com.example.demoPersonal.service;

import com.example.demoPersonal.dto.task.TaskRequestDTO;
import com.example.demoPersonal.dto.task.TaskResponseDTO;
import com.example.demoPersonal.entity.Employee;
import com.example.demoPersonal.entity.Project;
import com.example.demoPersonal.entity.Task;
import com.example.demoPersonal.entity.enums.Status;
import com.example.demoPersonal.exception.EmployeeNotFoundException;
import com.example.demoPersonal.exception.ProjectNotFoundException;
import com.example.demoPersonal.exception.TaskNotFoundException;
import com.example.demoPersonal.mapper.task.TaskMapper;
import com.example.demoPersonal.repository.EmployeeRepository;
import com.example.demoPersonal.repository.ProjectRepository;
import com.example.demoPersonal.repository.TaskRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final EmployeeRepository employeeRepository;

    private final TaskMapper taskMapper;

    public TaskService(TaskRepository taskRepository, ProjectRepository projectRepository,
                       EmployeeRepository employeeRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.employeeRepository = employeeRepository;
        this.taskMapper = taskMapper;
    }

    private Task findTaskOrThrow(Long id) {
        return taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
    }

    public TaskResponseDTO createTask(TaskRequestDTO dto) {
        Project project = projectRepository.findById(dto.projectId()).orElseThrow(() ->
                new ProjectNotFoundException(dto.projectId()));

        Task task = new Task(
                dto.description(),
                project
        );

        Task saved = taskRepository.save(task);
        return taskMapper.toDTO(saved);
    }

    public TaskResponseDTO getTaskById(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));

        return taskMapper.toDTO(task);
    }

    public List<TaskResponseDTO> getTasksByDescription(String description) {
        return taskRepository.findByDescription(description).stream().map(taskMapper::toDTO).toList();
    }
    public List<TaskResponseDTO> getTaskByStatus(Status status) {
        return taskRepository.findByStatus(status).stream().map(taskMapper::toDTO).toList();
    }

    public List<TaskResponseDTO> getUnassingedTasks() {
        return taskRepository.findByEmployeeIsNull().stream().map(taskMapper::toDTO).toList();
    }

    public List<TaskResponseDTO> getAllTasks(Pageable pageable) {
        return taskRepository.findAll(pageable).stream().map(taskMapper::toDTO).toList();
    }

    public TaskResponseDTO updateTask(Long id, TaskRequestDTO dto) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
        Project project = projectRepository.findById(dto.projectId()).orElseThrow(() ->
                new ProjectNotFoundException(dto.projectId()));

        task.setDescription(dto.description());
        task.setStatus(dto.status());
        task.setProject(project);

        Task updated = taskRepository.save(task);

        return taskMapper.toDTO(updated);
    }

    public void removeTask(Long id) {
        Task task = findTaskOrThrow(id);

        taskRepository.delete(task);
    }

    public TaskResponseDTO assignTask(Long taskId, Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException(employeeId));

        Task task = findTaskOrThrow(taskId);

        task.setEmployee(employee);

        Task assigned = taskRepository.save(task);

        return taskMapper.toDTO(assigned);
    }

    public TaskResponseDTO unassingTask(Long id) {
        Task task = findTaskOrThrow(id);

        task.setEmployee(null);

        Task unassigned = taskRepository.save(task);

        return taskMapper.toDTO(unassigned);
    }
}
