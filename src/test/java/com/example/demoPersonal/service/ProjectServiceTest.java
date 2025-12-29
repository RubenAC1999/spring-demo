package com.example.demoPersonal.service;

import com.example.demoPersonal.dto.project.ProjectRequestDTO;
import com.example.demoPersonal.dto.project.ProjectResponseDTO;
import com.example.demoPersonal.entity.Project;
import com.example.demoPersonal.exception.ProjectNotFoundException;
import com.example.demoPersonal.mapper.employee.EmployeeMapper;
import com.example.demoPersonal.mapper.project.ProjectMapper;
import com.example.demoPersonal.mapper.task.TaskMapper;
import com.example.demoPersonal.repository.ProjectRepository;
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
class ProjectServiceTest {
    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private ProjectMapper projectMapper;
    @Mock
    private TaskMapper taskMapper;
    @Mock
    private EmployeeMapper employeeMapper;

    @InjectMocks
    private ProjectService projectService;

    @Test
    void createProject_shouldReturnDTO_whenDataIsValid() {
        // GIVEN
        UUID projectUuid = UUID.randomUUID();

        ProjectRequestDTO dto = new ProjectRequestDTO(
                "Test"
        );

        Project saved = new Project();
        saved.setUuid(projectUuid);
        saved.setName("Test");

        ProjectResponseDTO mappedToDTO = new ProjectResponseDTO(
                saved.getUuid(),
                saved.getName()
        );

        when(projectRepository.save(any(Project.class))).thenReturn(saved);
        when(projectMapper.toDTO(saved)).thenReturn(mappedToDTO);

        // WHEN
        ProjectResponseDTO result = projectService.createProject(dto);

        // THEN
        assertNotNull(result);
        assertEquals(saved.getUuid(), result.uuid());
        assertEquals(saved.getName(), result.name());

        verify(projectRepository).save(any(Project.class));
        verify(projectMapper).toDTO(saved);
    }

    @Test
    void updateProject_shouldReturnDTO_whenDataIsValid() {
        // GIVEN
        UUID projectUuid = UUID.randomUUID();

        ProjectRequestDTO dto = new ProjectRequestDTO(
                "Updated"
        );

        Project toUpdate = new Project();
        toUpdate.setUuid(projectUuid);
        toUpdate.setName(dto.name());

        when(projectRepository.findByUuid(projectUuid)).thenReturn(Optional.of(toUpdate));

        when(projectRepository.save(any(Project.class))).thenReturn(toUpdate);

        ProjectResponseDTO mappedToDTO = new ProjectResponseDTO(
                toUpdate.getUuid(),
                toUpdate.getName()
        );

        when(projectMapper.toDTO(toUpdate)).thenReturn(mappedToDTO);

        // WHEN
        ProjectResponseDTO result = projectService.updateProject(projectUuid, dto);

        // THEN
        assertNotNull(result);
        assertEquals(toUpdate.getUuid(), result.uuid());
        assertEquals(dto.name(), result.name());

        verify(projectRepository).findByUuid(projectUuid);
        verify(projectRepository).save(any(Project.class));
        verify(projectMapper).toDTO(toUpdate);
    }

    @Test
    void updateProject_shouldThrowException_whenProjectNotExists() {
        // GIVEN
        UUID projectUuid = UUID.randomUUID();

        when(projectRepository.findByUuid(projectUuid)).thenReturn(Optional.empty());

        ProjectRequestDTO dto = new ProjectRequestDTO("Test");

        // WHEN - THEN
        assertThrows(ProjectNotFoundException.class, () -> projectService.updateProject(projectUuid, dto));

        verify(projectRepository).findByUuid(projectUuid);
        verify(projectRepository, never()).save(any(Project.class));
        verify(projectMapper, never()).toDTO(any(Project.class));
    }
}
