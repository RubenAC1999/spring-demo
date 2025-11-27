package com.example.demoPersonal.dto.task;

import com.example.demoPersonal.entity.enums.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TaskRequestDTO (
    @NotBlank(message = "Description is required")
    String description,

    @NotNull(message = "Status is required")
    Status status,

    @NotNull(message = "Project ID is required")
    Long projectId
) {}
