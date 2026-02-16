package com.example.demoPersonal.mapper.project;

import com.example.demoPersonal.dto.project.ProjectRequestDTO;
import com.example.demoPersonal.dto.project.ProjectResponseDTO;
import com.example.demoPersonal.entity.Project;
import org.springframework.stereotype.Component;

@Component
public class ProjectMapperImpl implements ProjectMapper {
    @Override
    public ProjectResponseDTO toDTO(Project project) {
        return new ProjectResponseDTO(
                project.getUuid(),
                project.getName()
        );
    }
}
