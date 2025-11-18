package com.example.demoPersonal.dto.task;

import com.example.demoPersonal.entity.enums.Status;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponseDTO {
    @Setter(AccessLevel.NONE)
    private Long id;

    private String description;
    private Status status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long employeeId;
    private Long projectId;
}
