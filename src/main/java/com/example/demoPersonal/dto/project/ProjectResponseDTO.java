package com.example.demoPersonal.dto.project;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectResponseDTO {
    @Setter(AccessLevel.NONE)
    private Long id;
    private String name;
}
