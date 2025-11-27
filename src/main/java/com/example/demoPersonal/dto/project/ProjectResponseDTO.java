package com.example.demoPersonal.dto.project;

import lombok.*;

public record ProjectResponseDTO (
    @Setter(AccessLevel.NONE)
    Long id,

    String name
) {}
