package com.example.demoPersonal.service;

import com.example.demoPersonal.dto.task.TaskRequestDTO;
import com.example.demoPersonal.dto.task.TaskResponseDTO;
import com.example.demoPersonal.entity.Employee;
import com.example.demoPersonal.entity.Project;
import com.example.demoPersonal.entity.Task;
import com.example.demoPersonal.entity.enums.Position;
import com.example.demoPersonal.entity.enums.Status;
import com.example.demoPersonal.exception.EmployeeNotFoundException;
import com.example.demoPersonal.exception.ProjectNotFoundException;
import com.example.demoPersonal.exception.TaskNotFoundException;
import com.example.demoPersonal.mapper.task.TaskMapper;
import com.example.demoPersonal.repository.EmployeeRepository;
import com.example.demoPersonal.repository.ProjectRepository;
import com.example.demoPersonal.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {
    @Mock
    private TaskRepository taskRepository;
    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskService taskService;

    @Test
    void createTask_shouldReturnDTO_whenDataIsValid() {
        // GIVEN
        UUID taskUuid = UUID.randomUUID();
        UUID projectUuid = UUID.randomUUID();

        Project project = new Project();
        project.setUuid(taskUuid);
        project.setName("Test");

        when(projectRepository.findByUuid(projectUuid)).thenReturn(Optional.of(project));

        TaskRequestDTO dto = new TaskRequestDTO(
                "Test",
                Status.TODO,
                projectUuid
        );

        Task saved = new Task();
        saved.setUuid(taskUuid);
        saved.setDescription(dto.description());
        saved.setProject(project);

        when(taskRepository.save(any(Task.class))).thenReturn(saved);

        TaskResponseDTO mappedToDTO = new TaskResponseDTO(
                taskUuid,
                dto.description(),
                dto.status(),
                saved.getCreatedAt(),
                saved.getUpdatedAt(),
                null,
                dto.projectUuid()
        );

        when(taskMapper.toDTO(saved)).thenReturn(mappedToDTO);

        // WHEN
        TaskResponseDTO result = taskService.createTask(dto);

        // THEN
        assertNotNull(result);
        assertEquals(taskUuid, result.uuid());
        assertEquals(dto.description(), result.description());
        assertEquals(dto.status(), result.status());
        assertEquals(saved.getCreatedAt(), result.createdAt());
        assertEquals(saved.getUpdatedAt(), result.updatedAt());
        assertEquals(dto.projectUuid(), result.projectUuid());

        verify(projectRepository).findByUuid(projectUuid);
        verify(taskRepository).save(any(Task.class));
        verify(taskMapper).toDTO(saved);
    }

    @Test
    void createTask_shouldReturnException_whenProjectNotExists() {
        // GIVEN
        UUID projectUuid = UUID.randomUUID();
        when(projectRepository.findByUuid(projectUuid)).thenReturn(Optional.empty());

        TaskRequestDTO dto = new TaskRequestDTO(
                "Test",
                Status.TODO,
                projectUuid
        );

        // WHEN - THEN
        assertThrows(ProjectNotFoundException.class, () -> taskService.createTask(dto));

        verify(projectRepository).findByUuid(projectUuid);
        verify(taskRepository, never()).save(any(Task.class));
        verify(taskMapper, never()).toDTO(any(Task.class));
    }

    @Test
    void updateTask_shouldReturnDTO_whenDataIsValid() {
        // GIVEN
        UUID taskUuid = UUID.randomUUID();
        UUID projectUuid = UUID.randomUUID();

        Project project = new Project();
        project.setUuid(projectUuid);
        project.setName("Project test");

        when(projectRepository.findByUuid(projectUuid)).thenReturn(Optional.of(project));


        TaskRequestDTO dto = new TaskRequestDTO(
                "Updated",
                Status.DONE,
                projectUuid
        );

        Task toUpdate = new Task();
        toUpdate.setUuid(projectUuid);
        toUpdate.setDescription(dto.description());
        toUpdate.setStatus(dto.status());
        toUpdate.setProject(project);

        when(taskRepository.findByUuid(taskUuid)).thenReturn(Optional.of(toUpdate));
        when(taskRepository.save(any(Task.class))).thenReturn(toUpdate);

        TaskResponseDTO mappedToDTO = new TaskResponseDTO(
                taskUuid,
                dto.description(),
                dto.status(),
                toUpdate.getCreatedAt(),
                toUpdate.getUpdatedAt(),
                null,
                dto.projectUuid()
        );


        when(taskMapper.toDTO(toUpdate)).thenReturn(mappedToDTO);

        // WHEN
        TaskResponseDTO result = taskService.updateTask(taskUuid, dto);

        // THEN
        assertNotNull(result);
        assertEquals(taskUuid, result.uuid());
        assertEquals(dto.description(), result.description());
        assertEquals(dto.status(), result.status());
        assertEquals(toUpdate.getCreatedAt(), result.createdAt());
        assertEquals(toUpdate.getUpdatedAt(), result.updatedAt());
        assertEquals(dto.projectUuid(), result.projectUuid());

        verify(projectRepository).findByUuid(projectUuid);
        verify(taskRepository).findByUuid(taskUuid);
        verify(taskRepository).save(any(Task.class));
        verify(taskMapper).toDTO(any(Task.class));
    }

    @Test
    void updateTask_shouldThrowException_whenTaskNotExists() {
        // GIVEN
        UUID taskUuid = UUID.randomUUID();
        UUID projectUuid = UUID.randomUUID();

        TaskRequestDTO dto = new TaskRequestDTO(
                "Test",
                Status.TODO,
                projectUuid
        );

        when(taskRepository.findByUuid(taskUuid)).thenReturn(Optional.empty());

        // WHEN - THEN
        assertThrows(TaskNotFoundException.class, () -> taskService.updateTask(taskUuid, dto));

        verify(taskRepository).findByUuid(taskUuid);
        verify(projectRepository, never()).findByUuid(projectUuid);
        verify(taskRepository, never()).save(any(Task.class));
        verify(taskMapper, never()).toDTO(any(Task.class));
    }

    @Test
    void updateTask_shouldThowException_whenProjectNotExists() {
        // GIVEN
        UUID taskUuid = UUID.randomUUID();
        UUID projectUuid = UUID.randomUUID();

        Project project = new Project();
        project.setId(1L);
        project.setName("TestProject");

        TaskRequestDTO dto = new TaskRequestDTO(
                "Test",
                Status.TODO,
                projectUuid
        );

        Task task = new Task();
        task.setUuid(taskUuid);
        task.setDescription(dto.description());
        task.setStatus(dto.status());
        task.setProject(project);



        when(taskRepository.findByUuid(taskUuid)).thenReturn(Optional.of(task));
        when(projectRepository.findByUuid(projectUuid)).thenReturn(Optional.empty());

        // WHEN - THEN
        assertThrows(ProjectNotFoundException.class, () -> taskService.updateTask(taskUuid, dto));

        verify(taskRepository).findByUuid(taskUuid);
        verify(projectRepository).findByUuid(projectUuid);
        verify(taskRepository, never()).save(any(Task.class));
        verify(taskMapper, never()).toDTO(any(Task.class));
    }

    @Test
    void removeTask_shouldNotReturn_whenDataIsValid() {
        // GIVEN
        UUID taskUuid = UUID.randomUUID();
        UUID projectUuid = UUID.randomUUID();

        Project project = new Project();
        project.setUuid(projectUuid);
        project.setName("TestProject");

        Task task = new Task();
        task.setUuid(taskUuid);
        task.setDescription("Test");
        task.setStatus(Status.TODO);
        task.setProject(project);

        when(taskRepository.findByUuid(taskUuid)).thenReturn(Optional.of(task));

        // WHEN
        taskService.removeTask(taskUuid);

        // THEN
        verify(taskRepository).findByUuid(taskUuid);
        verify(taskRepository).delete(task);
    }


    @Test
    void removeTask_shouldThrowException_whenTaskNotExists() {
        // GIVEN
        UUID taskUuid = UUID.randomUUID();

        when(taskRepository.findByUuid(taskUuid)).thenReturn(Optional.empty());

        // WHEN
        assertThrows(TaskNotFoundException.class, () -> taskService.removeTask(taskUuid));

        // THEN
        verify(taskRepository).findByUuid(taskUuid);
        verify(taskRepository, never()).delete(any(Task.class));
    }

    @Test
    void assignTask_shouldReturnDTO_whenDataIsValid() {
        // GIVEN
        UUID taskUuid = UUID.randomUUID();
        UUID employeeUuid = UUID.randomUUID();

        Employee employee = new Employee();
        employee.setUuid(employeeUuid);
        employee.setName("Test");
        employee.setEmail("test@gmail.com");
        employee.setPosition(Position.DEVELOPER);
        employee.setPassword("abc123.");

        when(employeeRepository.findByUuid(employeeUuid)).thenReturn(Optional.of(employee));

        Project project = new Project();
        project.setId(1L);
        project.setName("Test");

        Task task = new Task();
        task.setUuid(taskUuid);
        task.setDescription("Test");
        task.setStatus(Status.TODO);
        task.setProject(project);
        task.setEmployee(employee);

        when(taskRepository.findByUuid(taskUuid)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        TaskResponseDTO mappedToDTO = new TaskResponseDTO(
                taskUuid,
                task.getDescription(),
                task.getStatus(),
                task.getCreatedAt(),
                task.getUpdatedAt(),
                task.getEmployee().getUuid(),
                project.getUuid()
        );

        when(taskMapper.toDTO(task)).thenReturn(mappedToDTO);

        // WHEN
        TaskResponseDTO result = taskService.assignTask(taskUuid, employeeUuid);

        // THEN
        assertNotNull(result);
        assertEquals(taskUuid, result.uuid());
        assertEquals(task.getDescription(), result.description());
        assertEquals(task.getStatus(), result.status());
        assertEquals(employeeUuid, result.employeeUuid());

        verify(employeeRepository).findByUuid(employeeUuid);
        verify(taskRepository).findByUuid(taskUuid);
        verify(taskRepository).save(any(Task.class));
        verify(taskMapper).toDTO(task);
    }

    @Test
    void assignTask_shouldReturnException_whenEmployeeNotExists() {
        // GIVEN
        UUID taskUuid = UUID.randomUUID();
        UUID employeeUuid = UUID.randomUUID();

        when(employeeRepository.findByUuid(employeeUuid)).thenReturn(Optional.empty());

        // WHEN - THEN
        assertThrows(EmployeeNotFoundException.class, () -> taskService.assignTask(taskUuid, employeeUuid));

        verify(employeeRepository).findByUuid(employeeUuid);
        verify(taskRepository, never()).findByUuid(taskUuid);
        verify(taskRepository, never()).save(any(Task.class));
        verify(taskMapper, never()).toDTO(any(Task.class));
    }

    @Test
    void assignTask_shouldReturnException_whenTaskNotExists() {
        // GIVEN
        UUID employeeUuid = UUID.randomUUID();
        UUID taskUuid = UUID.randomUUID();

        Employee employee = new Employee();
        employee.setUuid(employeeUuid);

        when(employeeRepository.findByUuid(employeeUuid)).thenReturn(Optional.of(employee));
        when(taskRepository.findByUuid(taskUuid)).thenReturn(Optional.empty());

        // WHEN - THEN
        assertThrows(TaskNotFoundException.class, () -> taskService.assignTask(taskUuid, employeeUuid));

        verify(employeeRepository).findByUuid(employeeUuid);
        verify(taskRepository).findByUuid(taskUuid);
        verify(taskRepository, never()).save(any(Task.class));
        verify(taskMapper, never()).toDTO(any(Task.class));
    }

    @Test
    void unassignTask_shouldReturnDTO_whenDataIsValid() {
        // GIVEN
        UUID taskUuid = UUID.randomUUID();
        UUID projectUuid = UUID.randomUUID();

        Task task = new Task();
        task.setUuid(taskUuid);
        task.setDescription("Test");

        when(taskRepository.findByUuid(taskUuid)).thenReturn(Optional.of(task));

        TaskResponseDTO mappedToDTO = new TaskResponseDTO(
                taskUuid,
                task.getDescription(),
                Status.TODO,
                task.getCreatedAt(),
                task.getUpdatedAt(),
                null,
                projectUuid
        );

        when(taskRepository.save(any(Task.class))).thenReturn(task);

        when(taskMapper.toDTO(task)).thenReturn(mappedToDTO);

        // WHEN
        TaskResponseDTO result = taskService.unassingTask(taskUuid);

        // THEN
        assertNotNull(result);
        assertEquals(taskUuid, result.uuid());
        assertNull(result.employeeUuid());

        verify(taskRepository).findByUuid(taskUuid);
        verify(taskRepository).save(any(Task.class));
        verify(taskMapper).toDTO(task);
    }

    @Test
    void unassignTask_shouldReturnException_whenTaskNotExists() {
        // GIVEN
        UUID taskUuid = UUID.randomUUID();

        when(taskRepository.findByUuid(taskUuid)).thenReturn(Optional.empty());

        // WHEN - THEN
        assertThrows(TaskNotFoundException.class, () -> taskService.unassingTask(taskUuid));

        // THEN
        verify(taskRepository).findByUuid(taskUuid);
        verify(taskRepository, never()).save(any(Task.class));
        verify(taskMapper, never()).toDTO(any(Task.class));
    }
}
