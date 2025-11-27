package com.example.demoPersonal.dto.employee;

import com.example.demoPersonal.entity.enums.Position;
import lombok.*;

import java.util.List;

public record EmployeeResponseDTO (
    @Setter(AccessLevel.NONE) Long id,
     String email,
     String name,
     Position position,
     List<Long> projects_id
) {}
