package com.example.demoPersonal.service;

import com.example.demoPersonal.dto.employee.EmployeeRequestDTO;
import com.example.demoPersonal.dto.employee.EmployeeResponseDTO;
import com.example.demoPersonal.entity.Employee;
import com.example.demoPersonal.entity.Project;
import com.example.demoPersonal.entity.enums.Position;
import com.example.demoPersonal.exception.EmployeeExistsException;
import com.example.demoPersonal.exception.EmployeeNotFoundException;
import com.example.demoPersonal.mapper.employee.EmployeeMapper;
import com.example.demoPersonal.mapper.task.TaskMapper;
import com.example.demoPersonal.repository.EmployeeRepository;
import com.example.demoPersonal.repository.ProjectRepository;
import com.example.demoPersonal.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.postgresql.hostchooser.HostRequirement.any;

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
    private TaskMapper taskMapper;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private EmployeeService employeeService;

    @Test
    void getEmployeeById_shouldReturnDTO_whenEmployeeExists() {
        // GIVEN
        Long employeeId = 1L;

        Employee employee = new Employee();
        employee.setId(employeeId);
        employee.setName("Test");
        employee.setEmail("test@gmail.com");
        employee.setPassword("abc123.");
        employee.setPosition(Position.DEVELOPER);

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));

        EmployeeResponseDTO mappedToDto = new EmployeeResponseDTO(
                employee.getId(),
                employee.getEmail(),
                employee.getName(),
                employee.getPosition(),
                employee.getProjects().stream().map(Project::getId).toList()
        );

        when(employeeMapper.toDTO(employee)).thenReturn(mappedToDto);

        // WHEN
        EmployeeResponseDTO result = employeeService.getEmployeeById(employeeId);

        // THEN
        assertNotNull(result);
        assertEquals(employeeId, result.id());
        assertEquals("Test", result.name());
        assertEquals("test@gmail.com", result.email());

        verify(employeeRepository).findById(employeeId);
        verify(employeeMapper).toDTO(employee);
    }

    @Test
    void getEmployeeById_shouldThrowException_whenEmployeeNotExists() {
        // GIVEN
        Long employeeId = 99L;

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

        // WHEN - THEN
        assertThrows(EmployeeNotFoundException.class, () -> employeeService.getEmployeeById(employeeId));

        verify(employeeRepository).findById(employeeId);
        verify(employeeMapper, never()).toDTO(any());
    }


    @Test
    void createEmployee_shouldCreate_whenEmailNotExists() {
        // GIVEN
        EmployeeRequestDTO dto = new EmployeeRequestDTO(
                "Test",
                "test@gmail.com",
                Position.DEVELOPER,
                "abc123."
        );

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
                saved.getId(),
                "test@gmail.com",
                "Test",
                Position.DEVELOPER,
                saved.getProjects().stream().map(Project::getId).toList()
        );

        when(employeeMapper.toDTO(saved)).thenReturn(mappedDTO);

        // WHEN
        EmployeeResponseDTO result = employeeService.createEmployee(dto);

        // THEN
        assertNotNull(result);
        assertEquals(saved.getId(), result.id());
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
        EmployeeRequestDTO dto = new EmployeeRequestDTO(
                "Test",
                "test@gmail.com",
                Position.DEVELOPER,
                "abc123,"
        );

        when(employeeRepository.existsByEmail("test@gmail.com")).thenReturn(true);

        // WHEN - THEN
        assertThrows(EmployeeExistsException.class, () -> employeeService.createEmployee(dto));

        verify(employeeRepository).existsByEmail("test@gmail.com");
        verify(employeeRepository, never()).save(any());
    }

    @Test
    void updateEmployee_shouldReturnDTO_whenDataIsValid() {
        // GIVEN
        Long employeeId = 1L;

        Employee employee = new Employee();
        employee.setId(employeeId);
        employee.setName("Test");
        employee.setEmail("test@gmail.com");
        employee.setPosition(Position.DEVELOPER);
        employee.setPassword("abc123.");



        EmployeeRequestDTO dto = new EmployeeRequestDTO(
                "Updated",
                "updated@gmail.com",
                Position.DATA_ENGINEER,
                "updated123."
        );

        String normalizedEmail = dto.email().toLowerCase();

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
        when(employeeRepository.existsByEmail(normalizedEmail)).thenReturn(false);


        Employee saved = new Employee();
        saved.setId(employeeId);
        saved.setName(dto.name());
        saved.setEmail(normalizedEmail);
        saved.setPosition(dto.position());
        saved.setPassword(dto.password());

        when(employeeRepository.save(employee)).thenReturn(saved);

        EmployeeResponseDTO mappedToDTO = new EmployeeResponseDTO(
                saved.getId(),
                saved.getEmail(),
                saved.getName(),
                saved.getPosition(),
                saved.getProjects().stream().map(Project::getId).toList()
        );

        when(employeeMapper.toDTO(saved)).thenReturn(mappedToDTO);

        // WHEN
        EmployeeResponseDTO result = employeeService.updateEmployee(employeeId, dto);

        // THEN
        assertNotNull(result);
        assertEquals(dto.name(), result.name());
        assertEquals(normalizedEmail, result.email());
        assertEquals(dto.position(), result.position());

        verify(employeeRepository).findById(employeeId);
        verify(employeeRepository).existsByEmail(normalizedEmail);
        verify(employeeRepository).save(any(Employee.class));
        verify(employeeMapper).toDTO(saved);
    }

    @Test
    void updateEmployee_shouldReturnDTO_whenSameEmail() {
        // GIVEN

        Long employeeId = 1L;

        Employee employee = new Employee();
        employee.setId(employeeId);
        employee.setName("Old");
        employee.setEmail("oldEmail@gmail.com");
        employee.setPosition(Position.DEVELOPER);
        employee.setPassword("test123.");

        EmployeeRequestDTO dto = new EmployeeRequestDTO(
                "Updated",
                "oldEmail@gmail.com",
                Position.DATA_ENGINEER,
                "test123."
        );

        String normalizedEmail = dto.email().toLowerCase();

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
        when(employeeRepository.existsByEmail(normalizedEmail)).thenReturn(true);

        Employee updated = new Employee();
        updated.setId(employeeId);
        updated.setName(dto.name());
        updated.setEmail(normalizedEmail);
        updated.setPosition(dto.position());
        updated.setPassword("encoded-password");

        when(employeeRepository.save(employee)).thenReturn(updated);

        EmployeeResponseDTO mappedToDTO = new EmployeeResponseDTO(
                employeeId,
                normalizedEmail,
                updated.getName(),
                updated.getPosition(),
                List.of()
        );

        when(employeeMapper.toDTO(updated)).thenReturn(mappedToDTO);

        // WHEN
        EmployeeResponseDTO result = employeeService.updateEmployee(employeeId, dto);

        // THEN
        assertNotNull(result);
        assertEquals(employeeId, result.id());
        assertEquals("Updated", result.name());
        assertEquals(normalizedEmail, result.email());
        assertEquals(dto.position(), result.position());

        verify(employeeRepository).existsByEmail(normalizedEmail);
        verify(employeeRepository).save(any(Employee.class));
        verify(employeeMapper).toDTO(updated);
    }
}
