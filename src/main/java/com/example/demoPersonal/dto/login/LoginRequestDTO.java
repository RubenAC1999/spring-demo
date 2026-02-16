package com.example.demoPersonal.dto.login;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LoginRequestDTO(
        @NotBlank(message = "Email is required.")
        @Email
        @NotNull
        String email,

        @NotBlank(message = "Password is required.")
        @NotNull
        String password
) { }
