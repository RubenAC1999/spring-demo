package com.example.demoPersonal.mapper.task;

import com.example.demoPersonal.dto.task.TaskRequestDTO;
import com.example.demoPersonal.dto.task.TaskResponseDTO;
import com.example.demoPersonal.entity.Project;
import com.example.demoPersonal.entity.Task;
import org.springframework.stereotype.Component;


@Component
public class TaskMapperImpl implements TaskMapper {

    @Override
    public TaskResponseDTO toDTO(Task task) {
        return new TaskResponseDTO(
                task.getId(),
                task.getDescription(),
                task.getStatus(),
                task.getCreatedAt(),
                task.getUpdatedAt(),
                task.getEmployee().getId(),
                task.getProject().getId()
                );
    }

    @Override
    public Task toEntity(TaskRequestDTO dto, Project project) {
        return new Task(
                dto.getDescription(),
                project
        );
    }
}
