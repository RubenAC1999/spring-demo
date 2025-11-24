package com.example.demoPersonal.mapper.employee;

import com.example.demoPersonal.dto.employee.EmployeeRequestDTO;
import com.example.demoPersonal.dto.employee.EmployeeResponseDTO;
import com.example.demoPersonal.entity.Employee;
import com.example.demoPersonal.entity.Project;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapperImpl implements EmployeeMapper {

    @Override
    public EmployeeResponseDTO toDTO(Employee employee) {
        return new EmployeeResponseDTO(
                employee.getId(),
                employee.getName(),
                employee.getEmail(),
                employee.getPosition(),
                employee.getProjects()
                        .stream()
                        .map(Project::getId)
                        .toList()
        );
    }

    @Override
    public Employee toEntity(EmployeeRequestDTO dto) {
        return new Employee(
                dto.getName(),
                dto.getEmail(),
                dto.getPosition());
    }
}
