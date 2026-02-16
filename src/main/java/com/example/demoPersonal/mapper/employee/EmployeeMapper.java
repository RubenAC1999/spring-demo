package com.example.demoPersonal.mapper.employee;

import com.example.demoPersonal.dto.employee.EmployeeResponseDTO;
import com.example.demoPersonal.entity.Employee;

public interface EmployeeMapper {
    EmployeeResponseDTO toDTO(Employee employee);
}
