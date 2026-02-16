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
                task.getUuid(),
                task.getDescription(),
                task.getStatus(),
                task.getCreatedAt(),
                task.getUpdatedAt(),
                task.getEmployee() == null ? null : task.getEmployee().getUuid(),
                task.getProject().getUuid()
                );
    }
}
