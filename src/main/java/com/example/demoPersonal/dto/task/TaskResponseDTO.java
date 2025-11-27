package com.example.demoPersonal.dto.task;

import com.example.demoPersonal.entity.enums.Status;
import lombok.*;
import java.time.LocalDateTime;

public record TaskResponseDTO (
    @Setter(AccessLevel.NONE)
    Long id,

    String description,
    Status status,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    Long employeeId,
    Long projectId
) {}
