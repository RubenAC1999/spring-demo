package com.example.demoPersonal.dto.task;

import com.example.demoPersonal.entity.enums.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskRequestDTO {
    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Status is required")
    private Status status;


    // No hace falta poner Status, Status por defecto -> TODO
    public TaskRequestDTO(String description) {
        this.description = description;
    }

}
