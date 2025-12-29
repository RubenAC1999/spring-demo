package com.example.demoPersonal.mapper.employee;

import com.example.demoPersonal.dto.employee.EmployeeRequestDTO;
import com.example.demoPersonal.dto.employee.EmployeeResponseDTO;
import com.example.demoPersonal.entity.Employee;
import com.example.demoPersonal.mapper.project.ProjectMapper;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapperImpl implements EmployeeMapper {
    private final ProjectMapper projectMapper;

    public EmployeeMapperImpl (ProjectMapper projectMapper) {
        this.projectMapper = projectMapper;
    }

    @Override
    public EmployeeResponseDTO toDTO(Employee employee) {
        return new EmployeeResponseDTO(
                employee.getUuid(),
                employee.getName(),
                employee.getEmail(),
                employee.getPosition(),
                employee.getProjects()
                        .stream()
                        .map(projectMapper::toDTO)
                        .toList()
        );
    }

    @Override
    public Employee toEntity(EmployeeRequestDTO dto) {
        Employee employee = new Employee();
        employee.setName(dto.name());
        employee.setEmail(dto.email());
        employee.setPosition(dto.position());

        return employee;
    }
}
