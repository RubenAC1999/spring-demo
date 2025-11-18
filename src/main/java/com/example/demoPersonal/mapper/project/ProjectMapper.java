package com.example.demoPersonal.mapper.project;

import com.example.demoPersonal.dto.project.ProjectRequestDTO;
import com.example.demoPersonal.dto.project.ProjectResponseDTO;
import com.example.demoPersonal.entity.Project;

public interface ProjectMapper {
    ProjectResponseDTO toDTO(Project project);
    Project toEntity(ProjectRequestDTO dto);
}
