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

import java.util.List;
import java.util.Optional;

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
        Long taskId = 1L;
        Long projectId = 1L;

        Project project = new Project();
        project.setId(projectId);
        project.setName("Test");

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));

        TaskRequestDTO dto = new TaskRequestDTO(
                "Test",
                Status.TODO,
                1L
        );

        Task saved = new Task();
        saved.setId(taskId);
        saved.setDescription(dto.description());
        saved.setProject(project);

        when(taskRepository.save(any(Task.class))).thenReturn(saved);

        TaskResponseDTO mappedToDTO = new TaskResponseDTO(
                taskId,
                dto.description(),
                dto.status(),
                saved.getCreatedAt(),
                saved.getUpdatedAt(),
                null,
                dto.projectId()
        );

        when(taskMapper.toDTO(saved)).thenReturn(mappedToDTO);

        // WHEN
        TaskResponseDTO result = taskService.createTask(dto);

        // THEN
        assertNotNull(result);
        assertEquals(taskId, result.id());
        assertEquals(dto.description(), result.description());
        assertEquals(dto.status(), result.status());
        assertEquals(saved.getCreatedAt(), result.createdAt());
        assertEquals(saved.getUpdatedAt(), result.updatedAt());
        assertEquals(dto.projectId(), result.projectId());

        verify(projectRepository).findById(projectId);
        verify(taskRepository).save(any(Task.class));
        verify(taskMapper).toDTO(saved);
    }

    @Test
    void createTask_shouldReturnException_whenProjectNotExists() {
        // GIVEN
        Long projectId = 99L;
        when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

        TaskRequestDTO dto = new TaskRequestDTO(
                "Test",
                Status.TODO,
                projectId
        );

        // WHEN - THEN
        assertThrows(ProjectNotFoundException.class, () -> taskService.createTask(dto));

        verify(projectRepository).findById(projectId);
        verify(taskRepository, never()).save(any(Task.class));
        verify(taskMapper, never()).toDTO(any(Task.class));
    }

    @Test
    void updateTask_shouldReturnDTO_whenDataIsValid() {
        // GIVEN
        Long taskId = 1L;
        Long projectId = 1L;

        Project project = new Project();
        project.setId(projectId);
        project.setName("Project test");

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));


        TaskRequestDTO dto = new TaskRequestDTO(
                "Updated",
                Status.DONE,
                projectId
        );

        Task toUpdate = new Task();
        toUpdate.setId(taskId);
        toUpdate.setDescription(dto.description());
        toUpdate.setStatus(dto.status());
        toUpdate.setProject(project);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(toUpdate));
        when(taskRepository.save(any(Task.class))).thenReturn(toUpdate);

        TaskResponseDTO mappedToDTO = new TaskResponseDTO(
                taskId,
                dto.description(),
                dto.status(),
                toUpdate.getCreatedAt(),
                toUpdate.getUpdatedAt(),
                null,
                dto.projectId()
        );


        when(taskMapper.toDTO(toUpdate)).thenReturn(mappedToDTO);

        // WHEN
        TaskResponseDTO result = taskService.updateTask(taskId, dto);

        // THEN
        assertNotNull(result);
        assertEquals(taskId, result.id());
        assertEquals(dto.description(), result.description());
        assertEquals(dto.status(), result.status());
        assertEquals(toUpdate.getCreatedAt(), result.createdAt());
        assertEquals(toUpdate.getUpdatedAt(), result.updatedAt());
        assertEquals(dto.projectId(), result.projectId());

        verify(projectRepository).findById(projectId);
        verify(taskRepository).findById(taskId);
        verify(taskRepository).save(any(Task.class));
        verify(taskMapper).toDTO(any(Task.class));
    }

    @Test
    void updateTask_shouldThrowException_whenTaskNotExists() {
        // GIVEN
        Long taskId = 99L;
        Long projectId = 1L;
        TaskRequestDTO dto = new TaskRequestDTO(
                "Test",
                Status.TODO,
                projectId
        );

        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        // WHEN - THEN
        assertThrows(TaskNotFoundException.class, () -> taskService.updateTask(taskId, dto));

        verify(taskRepository).findById(taskId);
        verify(projectRepository, never()).findById(projectId);
        verify(taskRepository, never()).save(any(Task.class));
        verify(taskMapper, never()).toDTO(any(Task.class));
    }

    @Test
    void updateTask_shouldThowException_whenProjectNotExists() {
        // GIVEN
        Long taskId = 1L;
        Long projectId = 99L;

        Project project = new Project();
        project.setId(1L);
        project.setName("TestProject");

        TaskRequestDTO dto = new TaskRequestDTO(
                "Test",
                Status.TODO,
                99L
        );

        Task task = new Task();
        task.setId(taskId);
        task.setDescription(dto.description());
        task.setStatus(dto.status());
        task.setProject(project);



        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

        // WHEN - THEN
        assertThrows(ProjectNotFoundException.class, () -> taskService.updateTask(taskId, dto));

        verify(taskRepository).findById(taskId);
        verify(projectRepository).findById(projectId);
        verify(taskRepository, never()).save(any(Task.class));
        verify(taskMapper, never()).toDTO(any(Task.class));
    }

    @Test
    void removeTask_shouldNotReturn_whenDataIsValid() {
        // GIVEN
        Long taskId = 1L;
        Long projectId = 99L;

        Project project = new Project();
        project.setId(projectId);
        project.setName("TestProject");

        Task task = new Task();
        task.setId(taskId);
        task.setDescription("Test");
        task.setStatus(Status.TODO);
        task.setProject(project);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        // WHEN
        taskService.removeTask(taskId);

        // THEN
        verify(taskRepository).findById(taskId);
        verify(taskRepository).delete(task);
    }


    @Test
    void removeTask_shouldThrowException_whenTaskNotExists() {
        // GIVEN
        Long taskId = 1L;

        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        // WHEN
        assertThrows(TaskNotFoundException.class, () -> taskService.removeTask(taskId));

        // THEN
        verify(taskRepository).findById(taskId);
        verify(taskRepository, never()).delete(any(Task.class));
    }

    @Test
    void assignTask_shouldReturnDTO_whenDataIsValid() {
        // GIVEN
        Long taskId = 1L;
        Long employeeId = 1L;

        Employee employee = new Employee();
        employee.setId(employeeId);
        employee.setName("Test");
        employee.setEmail("test@gmail.com");
        employee.setPosition(Position.DEVELOPER);
        employee.setPassword("abc123.");

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));

        Project project = new Project();
        project.setId(1L);
        project.setName("Test");

        Task task = new Task();
        task.setId(taskId);
        task.setDescription("Test");
        task.setStatus(Status.TODO);
        task.setProject(project);
        task.setEmployee(employee);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        TaskResponseDTO mappedToDTO = new TaskResponseDTO(
                taskId,
                task.getDescription(),
                task.getStatus(),
                task.getCreatedAt(),
                task.getUpdatedAt(),
                task.getEmployee().getId(),
                project.getId()
        );

        when(taskMapper.toDTO(task)).thenReturn(mappedToDTO);

        // WHEN
        TaskResponseDTO result = taskService.assignTask(taskId, employeeId);

        // THEN
        assertNotNull(result);
        assertEquals(taskId, result.id());
        assertEquals(task.getDescription(), result.description());
        assertEquals(task.getStatus(), result.status());
        assertEquals(employeeId, result.employeeId());

        verify(employeeRepository).findById(employeeId);
        verify(taskRepository).findById(taskId);
        verify(taskRepository).save(any(Task.class));
        verify(taskMapper).toDTO(task);
    }

    @Test
    void assignTask_shouldReturnException_whenEmployeeNotExists() {
        // GIVEN
        Long employeeId = 99L;
        Long taskId = 1L;

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

        // WHEN - THEN
        assertThrows(EmployeeNotFoundException.class, () -> taskService.assignTask(taskId, employeeId));

        verify(employeeRepository).findById(employeeId);
        verify(taskRepository, never()).findById(taskId);
        verify(taskRepository, never()).save(any(Task.class));
        verify(taskMapper, never()).toDTO(any(Task.class));
    }

    @Test
    void assignTask_shouldReturnException_whenTaskNotExists() {
        // GIVEN
        Long employeeId = 99L;
        Long taskId = 1L;

        Employee employee = new Employee();
        employee.setId(employeeId);

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        // WHEN - THEN
        assertThrows(TaskNotFoundException.class, () -> taskService.assignTask(taskId, employeeId));

        verify(employeeRepository).findById(employeeId);
        verify(taskRepository).findById(taskId);
        verify(taskRepository, never()).save(any(Task.class));
        verify(taskMapper, never()).toDTO(any(Task.class));
    }

    @Test
    void unassignTask_shouldReturnDTO_whenDataIsValid() {
        // GIVEN
        Long taskId = 1L;
        Long projectId = 1L;

        Task task = new Task();
        task.setId(taskId);
        task.setDescription("Test");

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        TaskResponseDTO mappedToDTO = new TaskResponseDTO(
                taskId,
                task.getDescription(),
                Status.TODO,
                task.getCreatedAt(),
                task.getUpdatedAt(),
                null,
                projectId
        );

        when(taskRepository.save(any(Task.class))).thenReturn(task);

        when(taskMapper.toDTO(task)).thenReturn(mappedToDTO);

        // WHEN
        TaskResponseDTO result = taskService.unassingTask(taskId);

        // THEN
        assertNotNull(result);
        assertEquals(taskId, result.id());
        assertNull(result.employeeId());

        verify(taskRepository).findById(taskId);
        verify(taskRepository).save(any(Task.class));
        verify(taskMapper).toDTO(task);
    }

    @Test
    void unassignTask_shouldReturnException_whenTaskNotExists() {
        // GIVEN
        Long taskId = 1L;

        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        // WHEN - THEN
        assertThrows(TaskNotFoundException.class, () -> taskService.unassingTask(taskId));

        // THEN
        verify(taskRepository).findById(taskId);
        verify(taskRepository, never()).save(any(Task.class));
        verify(taskMapper, never()).toDTO(any(Task.class));
    }
}
