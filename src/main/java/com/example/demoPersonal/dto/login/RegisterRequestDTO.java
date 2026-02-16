package com.example.demoPersonal.dto.login;

import com.example.demoPersonal.entity.enums.Position;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegisterRequestDTO(
    @NotBlank(message = "Name is required.")
    String name,

    @NotBlank(message = "Email is required.")
    @Email
    String email,

    @NotNull (message = "Position is required.")
    Position position,

    @NotBlank (message = "Password is required.")
    String password
) {}
