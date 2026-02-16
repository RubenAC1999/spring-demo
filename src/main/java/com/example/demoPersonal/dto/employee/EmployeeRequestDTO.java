package com.example.demoPersonal.dto.employee;

import com.example.demoPersonal.entity.enums.Position;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EmployeeRequestDTO(

        @NotBlank(message = "Name is required")
        String name,

        @NotBlank(message = "Email is required")
        @Email
        String email,

        @NotNull(message = "Position cannot be null")
        Position position,

        @NotNull(message = "Password is required")
        String password
) {}
