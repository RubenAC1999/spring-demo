package com.example.demoPersonal.dto.project;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public record ProjectRequestDTO(
    @NotBlank(message = "Name is required")
    String name
) {}
