package com.example.demoPersonal.dto.employee;

import com.example.demoPersonal.entity.enums.Position;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeResponseDTO {
    @Setter(AccessLevel.NONE)
    private Long id;

    private String email;
    private String name;
    private Position position;
}
