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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final EmployeeRepository employeeRepository;

    private final TaskMapper taskMapper;

    private static final Logger log = LoggerFactory.getLogger(TaskService.class);

    public TaskService(TaskRepository taskRepository, ProjectRepository projectRepository,
                       EmployeeRepository employeeRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.employeeRepository = employeeRepository;
        this.taskMapper = taskMapper;
    }

    private Task findTaskOrThrow(UUID uuid) {
        log.debug("Searching task with uuid = {}", uuid);
        return taskRepository.findByUuid(uuid).orElseThrow(() -> new TaskNotFoundException(uuid));
    }

    public TaskResponseDTO createTask(TaskRequestDTO dto) {
        Project project = projectRepository.findByUuid(dto.projectUuid()).orElseThrow(() ->
                new ProjectNotFoundException(dto.projectUuid()));

        Task task = new Task();
        task.setDescription(dto.description());
        task.setProject(project);

        Task saved = taskRepository.save(task);
        log.info("Task {} (uuid = {}) created successfully.", saved.getDescription(), saved.getUuid());
        return taskMapper.toDTO(saved);
    }

    public TaskResponseDTO getTaskByUuid(UUID uuid) {
        Task task = taskRepository.findByUuid(uuid).orElseThrow(() -> new TaskNotFoundException(uuid));

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

    public TaskResponseDTO updateTask(UUID uuid, TaskRequestDTO dto) {
        Task task = taskRepository.findByUuid(uuid).orElseThrow(() -> new TaskNotFoundException(uuid));
        Project project = projectRepository.findByUuid(dto.projectUuid()).orElseThrow(() ->
                new ProjectNotFoundException(dto.projectUuid()));

        task.setDescription(dto.description());
        task.setStatus(dto.status());
        task.setProject(project);

        Task updated = taskRepository.save(task);

        log.info("Task {} (uuid = {}) updated successfully.", updated.getDescription(), updated.getUuid());

        return taskMapper.toDTO(updated);
    }

    public void removeTask(UUID uuid) {
        Task task = findTaskOrThrow(uuid);

        taskRepository.delete(task);

        log.info("Task {} (uuid = {}) removed successfully", task.getDescription(), task.getUuid());
    }

    public TaskResponseDTO assignTask(UUID taskUuid, UUID employeeUuid) {
        log.info("Assigning task {} to employee {}", taskUuid, employeeUuid);

        Employee employee = employeeRepository.findByUuid(employeeUuid)
                .orElseThrow(() -> new EmployeeNotFoundException(employeeUuid));

        Task task = findTaskOrThrow(taskUuid);

        task.setEmployee(employee);

        Task assigned = taskRepository.save(task);

        log.info("Task {} (uuid = {}) assigned successfully to {} (uuid = {}).",
                assigned.getDescription(), assigned.getUuid(), employee.getName(), employee.getUuid());

        return taskMapper.toDTO(assigned);
    }

    public TaskResponseDTO unassingTask(UUID uuid) {
        log.info("Unassigning task {}", uuid);
        Task task = findTaskOrThrow(uuid);

        task.setEmployee(null);

        Task unassigned = taskRepository.save(task);

        log.info("Task {} (uuid = {}) unassigned successfully.", unassigned.getDescription(), unassigned.getUuid());

        return taskMapper.toDTO(unassigned);
    }
}
