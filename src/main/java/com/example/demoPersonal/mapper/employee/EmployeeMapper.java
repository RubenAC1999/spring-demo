package com.example.demoPersonal.mapper.employee;

import com.example.demoPersonal.dto.employee.EmployeeRequestDTO;
import com.example.demoPersonal.dto.employee.EmployeeResponseDTO;
import com.example.demoPersonal.entity.Employee;

public interface EmployeeMapper {
    EmployeeResponseDTO toDTO(Employee employee);
    Employee toEntity(EmployeeRequestDTO dto);
}
