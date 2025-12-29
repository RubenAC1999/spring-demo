package com.example.demoPersonal.dto.employee;

import com.example.demoPersonal.dto.project.ProjectResponseDTO;
import com.example.demoPersonal.entity.enums.Position;

import java.util.List;
import java.util.UUID;

public record EmployeeResponseDTO (
     UUID uuid,
     String email,
     String name,
     Position position,
     List<ProjectResponseDTO> projects
) {}
