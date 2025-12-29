package com.example.demoPersonal.dto.task;

import com.example.demoPersonal.entity.enums.Status;
import java.time.LocalDateTime;
import java.util.UUID;

public record TaskResponseDTO (
    UUID uuid,
    String description,
    Status status,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    UUID employeeUuid,
    UUID projectUuid
) {}
