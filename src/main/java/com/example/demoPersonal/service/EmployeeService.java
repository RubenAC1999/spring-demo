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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    private Employee findByIdOrThrow(Long id) {
        log.debug("Searching employee with id = {}", id);
        return employeeRepository.findById(id).orElseThrow(() -> new EmployeeNotFoundException(id));
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
    public EmployeeResponseDTO getEmployeeById(Long id) {
       Employee employee = findByIdOrThrow(id);

       return employeeMapper.toDTO(employee);
    }

    @Transactional(readOnly = true)
    public List<EmployeeResponseDTO> getAllEmployees(Pageable pageable) {
        return employeeRepository.findAll(pageable).stream().map(employeeMapper::toDTO).toList();
    }

    public EmployeeResponseDTO updateEmployee(Long id, EmployeeRequestDTO dto) {
        Employee employee = findByIdOrThrow(id);

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

        log.info("Employee updated successfully with email={}", updated.getEmail());

        return employeeMapper.toDTO(updated);
    }

    public void removeEmployee(Long id) {
        Employee employee = findByIdOrThrow(id);

        employeeRepository.delete(employee);
        log.info("Employee with id={} removed", id);
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
    public List<TaskResponseDTO> getEmployeeTasks(Long id) {
        Employee employee = findByIdOrThrow(id);

        return employee.getTasks().stream().map(taskMapper::toDTO).toList();
    }

    public EmployeeResponseDTO assignProject(Long employeeId, Long projectId) {
        log.info("Assigning project {} to employee {}", projectId, employeeId);

        Employee employee = findByIdOrThrow(employeeId);
        Project project = projectRepository.findById(projectId).orElseThrow(()
                -> new ProjectNotFoundException(projectId));

        employee.addProject(project);

        Employee updated = employeeRepository.save(employee);

        log.info("Project {} assigned to employee {}", project.getName(), updated.getId());

        return employeeMapper.toDTO(updated);
    }

    public EmployeeResponseDTO unassignProject(Long employeeId, Long projectId) {
        log.info("Unassigning project {} to employee {}", projectId, employeeId);

        Employee employee = findByIdOrThrow(employeeId);
        Project project = projectRepository.findById(projectId).orElseThrow(() ->
                new ProjectNotFoundException(projectId));

        employee.removeProject(project);

        log.info("Project {} unassigned to employee {}", project.getName(), employeeId);
        return employeeMapper.toDTO(employee);
    }
}
