package com.example.demoPersonal.service;

import com.example.demoPersonal.dto.employee.EmployeeRequestDTO;
import com.example.demoPersonal.dto.employee.EmployeeResponseDTO;
import com.example.demoPersonal.dto.project.ProjectResponseDTO;
import com.example.demoPersonal.entity.Employee;
import com.example.demoPersonal.entity.Project;
import com.example.demoPersonal.entity.enums.Position;
import com.example.demoPersonal.exception.EmployeeExistsException;
import com.example.demoPersonal.exception.EmployeeNotFoundException;
import com.example.demoPersonal.exception.ProjectNotFoundException;
import com.example.demoPersonal.mapper.employee.EmployeeMapper;
import com.example.demoPersonal.mapper.project.ProjectMapper;
import com.example.demoPersonal.repository.EmployeeRepository;
import com.example.demoPersonal.repository.ProjectRepository;
import com.example.demoPersonal.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
 class EmployeeServiceTest {
    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private TaskRepository taskRepository;
    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private EmployeeMapper employeeMapper;
    @Mock
    private ProjectMapper projectMapper;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private EmployeeService employeeService;

    private Employee init() {
        UUID employeeUuid = UUID.randomUUID();

        Employee employee = new Employee();
        employee.setUuid(employeeUuid);
        employee.setName("Test");
        employee.setEmail("test@gmail.com");
        employee.setPassword("abc123.");
        employee.setPosition(Position.DEVELOPER);

        return employee;
    }

    private EmployeeRequestDTO initToDto() {
        return new EmployeeRequestDTO(
                "Test",
                "test@gmail.com",
                Position.DEVELOPER,
                "abc123."
        );
    }

    @Test
    void getEmployeeById_shouldReturnDTO_whenEmployeeExists() {
        // GIVEN
       Employee employee = init();

        when(employeeRepository.findByUuid(employee.getUuid())).thenReturn(Optional.of(employee));

        EmployeeResponseDTO mappedToDto = new EmployeeResponseDTO(
                employee.getUuid(),
                employee.getEmail(),
                employee.getName(),
                employee.getPosition(),
                employee.getProjects().stream().map(projectMapper::toDTO).toList()
        );

        when(employeeMapper.toDTO(employee)).thenReturn(mappedToDto);

        // WHEN
        EmployeeResponseDTO result = employeeService.getEmployeeByUuid(employee.getUuid());

        // THEN
        assertNotNull(result);
        assertEquals(employee.getUuid(), result.uuid());
        assertEquals("Test", result.name());
        assertEquals("test@gmail.com", result.email());

        verify(employeeRepository).findByUuid(employee.getUuid());
        verify(employeeMapper).toDTO(employee);
    }

    @Test
    void getEmployeeById_shouldThrowException_whenEmployeeNotExists() {
        // GIVEN
        UUID employeeUuid = UUID.randomUUID();

        when(employeeRepository.findByUuid(employeeUuid)).thenReturn(Optional.empty());

        // WHEN - THEN
        assertThrows(EmployeeNotFoundException.class, () -> employeeService.getEmployeeByUuid(employeeUuid));

        verify(employeeRepository).findByUuid(employeeUuid);
        verify(employeeMapper, never()).toDTO(any());
    }

    @Test
    void createEmployee_shouldCreate_whenEmailNotExists() {
        // GIVEN
        EmployeeRequestDTO dto = initToDto();

        String normalizedEmail = dto.email().toLowerCase();

        when(employeeRepository.existsByEmail(normalizedEmail)).thenReturn(false);
        when(passwordEncoder.encode(dto.password())).thenReturn("encoded-password");

        Employee saved = new Employee();
        saved.setName(dto.name());
        saved.setEmail(normalizedEmail);
        saved.setPosition(dto.position());
        saved.setPassword("encoded-password");

        when(employeeRepository.save(any(Employee.class))).thenReturn(saved);

        EmployeeResponseDTO mappedDTO = new EmployeeResponseDTO(
                saved.getUuid(),
                "test@gmail.com",
                "Test",
                Position.DEVELOPER,
                saved.getProjects().stream().map(projectMapper::toDTO).toList()
        );

        when(employeeMapper.toDTO(saved)).thenReturn(mappedDTO);

        // WHEN
        EmployeeResponseDTO result = employeeService.createEmployee(dto);

        // THEN
        assertNotNull(result);
        assertEquals(saved.getUuid(), result.uuid());
        assertEquals("Test", result.name());
        assertEquals(normalizedEmail, result.email());

        verify(employeeRepository).existsByEmail(normalizedEmail);
        verify(passwordEncoder).encode("abc123.");
        verify(employeeRepository).save(any(Employee.class));
        verify(employeeMapper).toDTO(saved);
    }

    @Test
    void createEmployee_shouldThrowException_whenEmailAlreadyExists() {
        // GIVEN
        EmployeeRequestDTO dto = initToDto();

        when(employeeRepository.existsByEmail("test@gmail.com")).thenReturn(true);

        // WHEN - THEN
        assertThrows(EmployeeExistsException.class, () -> employeeService.createEmployee(dto));

        verify(employeeRepository).existsByEmail("test@gmail.com");
        verify(employeeRepository, never()).save(any());
    }

    @Test
    void updateEmployee_shouldReturnDTO_whenDataIsValid() {
        // GIVEN
        Employee employee = init();

        EmployeeRequestDTO dto = initToDto();

        String normalizedEmail = dto.email().toLowerCase();

        when(employeeRepository.findByUuid(employee.getUuid())).thenReturn(Optional.of(employee));
        when(employeeRepository.existsByEmail(normalizedEmail)).thenReturn(false);


        Employee saved = new Employee();
        saved.setUuid(employee.getUuid());
        saved.setName(dto.name());
        saved.setEmail(normalizedEmail);
        saved.setPosition(dto.position());
        saved.setPassword(dto.password());

        when(employeeRepository.save(employee)).thenReturn(saved);

        EmployeeResponseDTO mappedToDTO = new EmployeeResponseDTO(
                saved.getUuid(),
                saved.getEmail(),
                saved.getName(),
                saved.getPosition(),
                saved.getProjects().stream().map(projectMapper::toDTO).toList()
        );

        when(employeeMapper.toDTO(saved)).thenReturn(mappedToDTO);

        // WHEN
        EmployeeResponseDTO result = employeeService.updateEmployee(employee.getUuid(), dto);

        // THEN
        assertNotNull(result);
        assertEquals(dto.name(), result.name());
        assertEquals(normalizedEmail, result.email());
        assertEquals(dto.position(), result.position());

        verify(employeeRepository).findByUuid(employee.getUuid());
        verify(employeeRepository).existsByEmail(normalizedEmail);
        verify(employeeRepository).save(any(Employee.class));
        verify(employeeMapper).toDTO(saved);
    }

    @Test
    void updateEmployee_shouldReturnDTO_whenSameEmail() {
        // GIVEN
       Employee employee = init();

       EmployeeRequestDTO dto = initToDto();

       String normalizedEmail = dto.email().toLowerCase();

        when(employeeRepository.findByUuid(employee.getUuid())).thenReturn(Optional.of(employee));
        when(employeeRepository.existsByEmail(normalizedEmail)).thenReturn(true);

        Employee updated = new Employee();
        updated.setUuid(employee.getUuid());
        updated.setName(dto.name());
        updated.setEmail(normalizedEmail);
        updated.setPosition(dto.position());
        updated.setPassword("encoded-password");

        when(employeeRepository.save(employee)).thenReturn(updated);

        EmployeeResponseDTO mappedToDTO = new EmployeeResponseDTO(
                updated.getUuid(),
                normalizedEmail,
                updated.getName(),
                updated.getPosition(),
                List.of()
        );

        when(employeeMapper.toDTO(updated)).thenReturn(mappedToDTO);

        // WHEN
        EmployeeResponseDTO result = employeeService.updateEmployee(employee.getUuid(), dto);

        // THEN
        assertNotNull(result);
        assertEquals(employee.getUuid(), result.uuid());
        assertEquals("Test", result.name());
        assertEquals(normalizedEmail, result.email());
        assertEquals(dto.position(), result.position());

        verify(employeeRepository).existsByEmail(normalizedEmail);
        verify(employeeRepository).save(any(Employee.class));
        verify(employeeMapper).toDTO(updated);
    }

    @Test
    void updateEmployee_shouldReturnException_employeeNotExists() {
        // GIVEN
        UUID employeeUuid = UUID.randomUUID();

        EmployeeRequestDTO dto = initToDto();

        when(employeeRepository.findByUuid(employeeUuid)).thenReturn(Optional.empty());

        // WHEN - THEN
        assertThrows(EmployeeNotFoundException.class, () -> employeeService.updateEmployee(employeeUuid, dto));

        verify(employeeRepository).findByUuid(employeeUuid);
    }

    @Test
    void updateEmployee_shouldReturnException_sameEmailAsOtherEmployee() {
        // GIVEN
        UUID otherEmployeeUuid = UUID.randomUUID();
        Employee toUpdate = init();
        EmployeeRequestDTO dto = new EmployeeRequestDTO(
                "Test",
                "other@gmail.com",
                Position.DEVELOPER,
                "abc123."
        );

        String normalizedEmail = dto.email().toLowerCase();

        Employee employeeAlreadyExists = new Employee();
        employeeAlreadyExists.setUuid(otherEmployeeUuid);
        employeeAlreadyExists.setName("Exists");
        employeeAlreadyExists.setEmail(normalizedEmail);
        employeeAlreadyExists.setPosition(Position.DATA_ENGINEER);

        when(employeeRepository.findByUuid(toUpdate.getUuid())).thenReturn(Optional.of(toUpdate));
        when(employeeRepository.existsByEmail(normalizedEmail)).thenReturn(true);

        // WHEN
        assertThrows(EmployeeExistsException.class, () -> employeeService.updateEmployee(toUpdate.getUuid(), dto));

        // THEN
        verify(employeeRepository).findByUuid(toUpdate.getUuid());
        verify(employeeRepository).existsByEmail(normalizedEmail);
    }

    @Test
    void assignProject_shouldReturnDTO_whenDataIsValid() {
        // GIVEN
        Employee employee = init();

        UUID projectUuid = UUID.randomUUID();

        Project project = new Project();
        project.setUuid(projectUuid);
        project.setName("ProjectTest");

        when(employeeRepository.findByUuid(employee.getUuid())).thenReturn(Optional.of(employee));
        when(projectRepository.findByUuid(projectUuid)).thenReturn(Optional.of(project));

        Employee updated = new Employee();
        updated.setUuid(employee.getUuid());
        updated.setName(employee.getName());
        updated.setEmail(employee.getEmail());
        updated.setPosition(employee.getPosition());
        updated.addProject(project);

        List<ProjectResponseDTO> projects = updated.getProjects().stream().map(projectMapper::toDTO).toList();


        when(employeeRepository.save(employee)).thenReturn(updated);

        EmployeeResponseDTO mappedToDTO = new EmployeeResponseDTO(
                updated.getUuid(),
                updated.getEmail(),
                updated.getName(),
                updated.getPosition(),
                updated.getProjects().stream().map(projectMapper::toDTO).toList()
        );

        when(employeeMapper.toDTO(updated)).thenReturn(mappedToDTO);

        // WHEN
        EmployeeResponseDTO result = employeeService.assignProject(employee.getUuid(), projectUuid);

        // THEN
        assertNotNull(result);
        assertEquals(updated.getUuid(), result.uuid());
        assertEquals("Test", result.name());
        assertEquals("test@gmail.com", result.email());
        assertEquals(updated.getPosition(), result.position());
        assertEquals(projects, result.projects());

        verify(employeeRepository).findByUuid(employee.getUuid());
        verify(projectRepository).findByUuid(projectUuid);
        verify(employeeRepository).save(employee);
        verify(employeeMapper).toDTO(updated);
    }

    @Test
    void assignProject_shouldThrowException_whenEmployeeNotExists() {
        // GIVEN
        UUID employeeUuid = UUID.randomUUID();
        UUID projectUuid = UUID.randomUUID();

        when(employeeRepository.findByUuid(employeeUuid)).thenReturn(Optional.empty());

        // WHEN - THEN
        assertThrows(EmployeeNotFoundException.class, () -> employeeService.assignProject(employeeUuid, projectUuid));

        verify(employeeRepository).findByUuid(employeeUuid);
        verify(projectRepository, never()).findById(any());
        verify(employeeRepository, never()).save(any());
        verify(employeeMapper, never()).toDTO(any());
    }

    @Test
    void assignProject_shouldThrowException_whenProjectNotExists() {
        // GIVEN
        UUID projectUuid = UUID.randomUUID();
        Employee employee = init();

        when(employeeRepository.findByUuid(employee.getUuid())).thenReturn(Optional.of(employee));
        when(projectRepository.findByUuid(projectUuid)).thenReturn(Optional.empty());

        // WHEN - THEN
        assertThrows(ProjectNotFoundException.class, () -> employeeService.assignProject(employee.getUuid(), projectUuid));

        verify(employeeRepository).findByUuid(employee.getUuid());
        verify(projectRepository).findByUuid(projectUuid);
        verify(employeeRepository, never()).save(any());
        verify(employeeMapper, never()).toDTO(any());
    }

    @Test
    void unassignProject_shouldReturnDTO_whenDataIsValid() {
        // GIVEN
        Employee employee = init();

        UUID projectUuid = UUID.randomUUID();

        Project project = new Project();
        project.setUuid(projectUuid);
        project.setName("ProjectTest");

        when(employeeRepository.findByUuid(employee.getUuid())).thenReturn(Optional.of(employee));
        when(projectRepository.findByUuid(projectUuid)).thenReturn(Optional.of(project));

        Employee updated = new Employee();
        updated.setUuid(employee.getUuid());
        updated.setName(employee.getName());
        updated.setEmail(employee.getEmail());
        updated.setPosition(employee.getPosition());
        updated.removeProject(project);

        List<ProjectResponseDTO> projects = updated.getProjects().stream().map(projectMapper::toDTO).toList();


        when(employeeRepository.save(employee)).thenReturn(updated);

        EmployeeResponseDTO mappedToDTO = new EmployeeResponseDTO(
                updated.getUuid(),
                updated.getEmail(),
                updated.getName(),
                updated.getPosition(),
                updated.getProjects().stream().map(projectMapper::toDTO).toList()
        );

        when(employeeMapper.toDTO(updated)).thenReturn(mappedToDTO);

        // WHEN
        EmployeeResponseDTO result = employeeService.unassignProject(employee.getUuid(), projectUuid);

        // THEN
        assertNotNull(result);
        assertEquals(employee.getUuid(), result.uuid());
        assertEquals("Test", result.name());
        assertEquals("test@gmail.com", result.email());
        assertEquals(updated.getPosition(), result.position());
        assertEquals(projects, result.projects());

        verify(employeeRepository).findByUuid(employee.getUuid());
        verify(projectRepository).findByUuid(projectUuid);
        verify(employeeRepository).save(employee);
        verify(employeeMapper).toDTO(updated);
    }

    @Test
    void unassingProject_shouldThrowException_whenEmployeeNotExists() {
        // GIVEN
        UUID employeeUuid = UUID.randomUUID();
        UUID projectUuid = UUID.randomUUID();

        when(employeeRepository.findByUuid(employeeUuid)).thenReturn(Optional.empty());

        // WHEN - THEN
        assertThrows(EmployeeNotFoundException.class, () -> employeeService.unassignProject(employeeUuid, projectUuid));

        verify(employeeRepository).findByUuid(employeeUuid);
        verify(projectRepository, never()).findById(any());
        verify(employeeRepository, never()).save(any());
        verify(employeeMapper, never()).toDTO(any());
    }

    @Test
    void unassignProject_shouldThrowException_whenProjectNotExists() {
        // GIVEN
        UUID projectUuid = UUID.randomUUID();

        Employee employee = init();

        when(employeeRepository.findByUuid(employee.getUuid())).thenReturn(Optional.of(employee));
        when(projectRepository.findByUuid(projectUuid)).thenReturn(Optional.empty());

        // WHEN - THEN
        assertThrows(ProjectNotFoundException.class, () -> employeeService.unassignProject(employee.getUuid(), projectUuid));

        verify(employeeRepository).findByUuid(employee.getUuid());
        verify(projectRepository).findByUuid(projectUuid);
        verify(employeeRepository, never()).save(any());
        verify(employeeMapper, never()).toDTO(any());
    }


}
