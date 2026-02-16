package com.example.demoPersonal.mapper.project;

import com.example.demoPersonal.dto.project.ProjectResponseDTO;
import com.example.demoPersonal.entity.Project;

public interface ProjectMapper {
    ProjectResponseDTO toDTO(Project project);
}
