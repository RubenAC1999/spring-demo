package com.example.demoPersonal.service;

import com.example.demoPersonal.dto.task.TaskRequestDTO;
import com.example.demoPersonal.dto.task.TaskResponseDTO;
import com.example.demoPersonal.entity.Project;
import com.example.demoPersonal.entity.Task;
import com.example.demoPersonal.entity.enums.Status;
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
}
