package com.example.demoPersonal.service;

import com.example.demoPersonal.dto.employee.EmployeeRequestDTO;
import com.example.demoPersonal.dto.employee.EmployeeResponseDTO;
import com.example.demoPersonal.entity.Employee;
import com.example.demoPersonal.exception.EmployeeExistsException;
import com.example.demoPersonal.repository.EmployeeRepository;
import com.example.demoPersonal.repository.ProjectRepository;
import com.example.demoPersonal.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;

    public EmployeeService(EmployeeRepository employeeRepository, TaskRepository taskRepository,
                           ProjectRepository projectRepository) {
        this.employeeRepository = employeeRepository;
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
    }

    private EmployeeResponseDTO mapToResponse(Employee employee) {
        return new EmployeeResponseDTO(
                employee.getId(),
                employee.getEmail(),
                employee.getName(),
                employee.getPosition()
        );
    }

    public EmployeeResponseDTO createEmployee(EmployeeRequestDTO dto) {
        if (employeeRepository.existsByEmail(dto.getEmail())) {
            throw new EmployeeExistsException(dto.getEmail());
        }

        Employee employee = new Employee();
        employee.setName(dto.getEmail());
        employee.setEmail(dto.getEmail());
        employee.setPosition(dto.getPosition());
        employee.setProjects(new ArrayList<>());
        employee.setTasks(new ArrayList<>());

        Employee saved = employeeRepository.save(employee);

        return mapToResponse(saved);
    }


}
