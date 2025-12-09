package com.example.demoPersonal.dto.project;

import jakarta.validation.constraints.NotBlank;

public record ProjectRequestDTO(
    @NotBlank(message = "Name is required")
    String name
) {}
