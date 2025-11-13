package com.example.demoPersonal.dto.employee;

import com.example.demoPersonal.entity.enums.Position;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeResponseDTO {
    @Setter(AccessLevel.NONE)
    private Long id;

    @NotBlank
    private String email;

    @NotBlank
    private String name;

    @NotNull
    private Position position;
}
