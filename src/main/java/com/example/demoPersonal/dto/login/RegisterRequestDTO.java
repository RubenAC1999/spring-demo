package com.example.demoPersonal.dto.login;

import com.example.demoPersonal.entity.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegisterRequestDTO(
    @NotBlank String name,
    @NotBlank @Email String email,
    @NotBlank String password
            )
{}
