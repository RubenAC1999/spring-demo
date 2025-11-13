package com.example.demoPersonal.dto.task;

import com.example.demoPersonal.entity.enums.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponseDTO {
    @Setter(AccessLevel.NONE)
    private Long id;

    @NotBlank
    private String description;

    @NotNull
    private Status status;

}
