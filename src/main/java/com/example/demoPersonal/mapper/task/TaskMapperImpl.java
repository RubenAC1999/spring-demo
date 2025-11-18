package com.example.demoPersonal.mapper.task;

import com.example.demoPersonal.dto.task.TaskRequestDTO;
import com.example.demoPersonal.dto.task.TaskResponseDTO;
import com.example.demoPersonal.entity.Task;

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
    public Task toEntity(TaskRequestDTO dto) {
        return new Task(
                dto.getDescription(),
                dto.getProjectId()
        );
    }
}
