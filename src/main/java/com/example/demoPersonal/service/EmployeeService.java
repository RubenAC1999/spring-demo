package com.example.demoPersonal.service;

import com.example.demoPersonal.dto.employee.EmployeeRequestDTO;
import com.example.demoPersonal.dto.employee.EmployeeResponseDTO;
import com.example.demoPersonal.dto.task.TaskResponseDTO;
import com.example.demoPersonal.entity.Employee;
import com.example.demoPersonal.entity.Task;
import com.example.demoPersonal.entity.enums.Position;
import com.example.demoPersonal.exception.EmployeeExistsException;
import com.example.demoPersonal.exception.EmployeeNotFoundException;
import com.example.demoPersonal.mapper.employee.EmployeeMapper;
import com.example.demoPersonal.mapper.task.TaskMapper;
import com.example.demoPersonal.repository.EmployeeRepository;
import com.example.demoPersonal.repository.ProjectRepository;
import com.example.demoPersonal.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;

    private final EmployeeMapper employeeMapper;
    private final TaskMapper taskMapper;

    public EmployeeService(EmployeeRepository employeeRepository, TaskRepository taskRepository,
                           ProjectRepository projectRepository, EmployeeMapper employeeMapper, TaskMapper taskMapper) {
        this.employeeRepository = employeeRepository;
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.employeeMapper = employeeMapper;
        this.taskMapper = taskMapper;
    }


    private Employee findByIdOrThrow(Long id) {
        return employeeRepository.findById(id).orElseThrow(() -> new EmployeeNotFoundException(id));
    }

    public EmployeeResponseDTO createEmployee(EmployeeRequestDTO dto) {
        String email = dto.getEmail().toLowerCase();

        if (employeeRepository.existsByEmail(email)) {
            throw new EmployeeExistsException(email);
        }

        Employee employee = new Employee();
        employee.setName(dto.getName());
        employee.setEmail(email);
        employee.setPosition(dto.getPosition());

        Employee saved = employeeRepository.save(employee);

        return employeeMapper.toDTO(saved);
    }

    public EmployeeResponseDTO getEmployeeById(Long id) {
       Employee employee = findByIdOrThrow(id);

       return employeeMapper.toDTO(employee);
    }

    public List<EmployeeResponseDTO> getAllEmployees() {
        return employeeRepository.findAll().stream().map(employeeMapper::toDTO).toList();
    }

    public EmployeeResponseDTO updateEmployee(Long id, EmployeeRequestDTO dto) {
        Employee employee = findByIdOrThrow(id);

        String email = dto.getEmail().toLowerCase();

        if (employeeRepository.existsByEmail(email) && !email.equalsIgnoreCase(employee.getEmail())) {
            throw new EmployeeExistsException(email);
        }

        employee.setName(dto.getName());
        employee.setEmail(email);
        employee.setPosition(dto.getPosition());

        Employee updated = employeeRepository.save(employee);

        return employeeMapper.toDTO(updated);
    }

    public void removeEmployee(Long id) {
        Employee employee = findByIdOrThrow(id);

        employeeRepository.delete(employee);
    }

    public EmployeeResponseDTO getEmployeeByEmail(String email) {
        String emailNormalized = email.toLowerCase();

        Employee employee = employeeRepository.findByEmail(emailNormalized)
                .orElseThrow(() -> new EmployeeNotFoundException(emailNormalized));

        return employeeMapper.toDTO(employee);
    }

    public List<EmployeeResponseDTO> getEmployeeByName(String name) {
        return employeeRepository.findByNameIgnoreCase(name).stream().map(employeeMapper::toDTO).toList();
    }

    public List<EmployeeResponseDTO> getEmployeesByPosition(Position position) {
        return employeeRepository.findByPosition(position).stream().map(employeeMapper::toDTO).toList();
    }

    public List<TaskResponseDTO> listEmployeeTasks(Long id) {
        Employee employee = findByIdOrThrow(id);

        return employee.getTasks().stream().map(taskMapper::toDTO).toList();
    }

}
