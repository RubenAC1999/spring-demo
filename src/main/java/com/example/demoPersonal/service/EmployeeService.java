package com.example.demoPersonal.service;

import com.example.demoPersonal.dto.employee.EmployeeRequestDTO;
import com.example.demoPersonal.dto.employee.EmployeeResponseDTO;
import com.example.demoPersonal.dto.task.TaskResponseDTO;
import com.example.demoPersonal.entity.Employee;
import com.example.demoPersonal.entity.Project;
import com.example.demoPersonal.entity.enums.Position;
import com.example.demoPersonal.exception.EmployeeExistsException;
import com.example.demoPersonal.exception.EmployeeNotFoundException;
import com.example.demoPersonal.exception.ProjectNotFoundException;
import com.example.demoPersonal.mapper.employee.EmployeeMapper;
import com.example.demoPersonal.mapper.task.TaskMapper;
import com.example.demoPersonal.repository.EmployeeRepository;
import com.example.demoPersonal.repository.ProjectRepository;
import com.example.demoPersonal.repository.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;

    private final EmployeeMapper employeeMapper;
    private final TaskMapper taskMapper;

    private static final Logger log = LoggerFactory.getLogger(EmployeeService.class);

    private final PasswordEncoder passwordEncoder;

    public EmployeeService(EmployeeRepository employeeRepository, TaskRepository taskRepository,
                           ProjectRepository projectRepository, EmployeeMapper employeeMapper, TaskMapper taskMapper,
                           PasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.employeeMapper = employeeMapper;
        this.taskMapper = taskMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    private Employee findByUuidOrThrow(UUID uuid) {
        log.debug("Searching employee with uuid = {}", uuid);
        return employeeRepository.findByUuid(uuid).orElseThrow(() -> new EmployeeNotFoundException(uuid));
    }

    public EmployeeResponseDTO createEmployee(EmployeeRequestDTO dto) {
        String email = dto.email().toLowerCase();

        log.info("Creating employee with email = {}", email);

        if (employeeRepository.existsByEmail(email)) {
            log.warn("Employee with email={} already exists", email);

            throw new EmployeeExistsException(email);
        }

        Employee employee = new Employee();
        employee.setName(dto.name());
        employee.setEmail(email);
        employee.setPassword(passwordEncoder.encode(dto.password()));
        employee.setPosition(dto.position());

        Employee saved = employeeRepository.save(employee);

        log.info("New employee created with email = {}", saved.getEmail());

        return employeeMapper.toDTO(saved);
    }

    @Transactional(readOnly = true)
    public EmployeeResponseDTO getEmployeeByUuid(UUID uuid) {
       Employee employee = findByUuidOrThrow(uuid);

       return employeeMapper.toDTO(employee);
    }

    @Transactional(readOnly = true)
    public EmployeeResponseDTO getCurrentEmployee(String email) {
        String normalizedEmail = email.toLowerCase();

        Employee currentEmployee = employeeRepository.findByEmail(normalizedEmail)
                .orElseThrow(() -> new EmployeeNotFoundException(email));

        return employeeMapper.toDTO(currentEmployee);
    }

    @Transactional(readOnly = true)
    public List<EmployeeResponseDTO> getAllEmployees(Pageable pageable) {
        return employeeRepository.findAll(pageable).stream().map(employeeMapper::toDTO).toList();
    }

    public EmployeeResponseDTO updateEmployee(UUID uuid, EmployeeRequestDTO dto) {
        Employee employee = findByUuidOrThrow(uuid);

        String email = dto.email().toLowerCase();

        log.info("Updating employee with email = {}", dto.email());
        if (employeeRepository.existsByEmail(email) && !email.equalsIgnoreCase(employee.getEmail())) {
            log.warn("Employee with email = {} already exists", email);

            throw new EmployeeExistsException(email);
        }

        employee.setName(dto.name());
        employee.setEmail(email);
        employee.setPosition(dto.position());

        Employee updated = employeeRepository.save(employee);

        log.info("Employee updated successfully with email={}", email);

        return employeeMapper.toDTO(updated);
    }

    public void removeEmployee(UUID uuid) {
        Employee employee = findByUuidOrThrow(uuid);

        employeeRepository.delete(employee);
        log.info("Employee with id={} removed", uuid);
    }

    @Transactional(readOnly = true)
    public EmployeeResponseDTO getEmployeeByEmail(String email) {
        String emailNormalized = email.toLowerCase();

        Employee employee = employeeRepository.findByEmail(emailNormalized)
                .orElseThrow(() -> new EmployeeNotFoundException(emailNormalized));

        return employeeMapper.toDTO(employee);
    }

    @Transactional(readOnly = true)
    public List<EmployeeResponseDTO> getEmployeeByName(String name) {
        return employeeRepository.findByNameIgnoreCase(name).stream().map(employeeMapper::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<EmployeeResponseDTO> getEmployeesByPosition(Position position) {
        return employeeRepository.findByPosition(position).stream().map(employeeMapper::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<TaskResponseDTO> getEmployeeTasks(UUID uuid) {
        Employee employee = findByUuidOrThrow(uuid);

        return employee.getTasks().stream().map(taskMapper::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<TaskResponseDTO> getCurrentEmployeeTasks(String email) {
        String emailNormalized = email.toLowerCase();

        Employee currentEmployee = employeeRepository.findByEmail(emailNormalized)
                .orElseThrow(() -> new EmployeeNotFoundException(emailNormalized));

        return currentEmployee.getTasks().stream().map(taskMapper::toDTO).toList();
    }


    public EmployeeResponseDTO assignProject(UUID employeeUuid, UUID projectUuid) {
        log.info("Assigning project {} to employee {}", projectUuid, employeeUuid);

        Employee employee = findByUuidOrThrow(employeeUuid);
        Project project = projectRepository.findByUuid(projectUuid).orElseThrow(()
                -> new ProjectNotFoundException(projectUuid));

        employee.addProject(project);

        Employee updated = employeeRepository.save(employee);

        log.info("Project {} assigned to employee {}", project.getName(), updated.getUuid());

        return employeeMapper.toDTO(updated);
    }

    public EmployeeResponseDTO unassignProject(UUID employeeUuid, UUID projectUuid) {
        log.info("Unassigning project {} to employee {}", employeeUuid, projectUuid);

        Employee employee = findByUuidOrThrow(employeeUuid);
        Project project = projectRepository.findByUuid(projectUuid).orElseThrow(() ->
                new ProjectNotFoundException(projectUuid));

        employee.removeProject(project);

        Employee updated = employeeRepository.save(employee);

        log.info("Project {} unassigned to employee {}", project.getName(), employee.getName());
        return employeeMapper.toDTO(updated);
    }
}
