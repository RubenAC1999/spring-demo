package com.example.demoPersonal.mapper.employee;

import com.example.demoPersonal.dto.employee.EmployeeRequestDTO;
import com.example.demoPersonal.dto.employee.EmployeeResponseDTO;
import com.example.demoPersonal.entity.Employee;

public class EmployeeMapperImpl implements EmployeeMapper {

    @Override
    public EmployeeResponseDTO toDTO(Employee employee) {
        return new EmployeeResponseDTO(
                employee.getId(),
                employee.getName(),
                employee.getEmail(),
                employee.getPosition()
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
