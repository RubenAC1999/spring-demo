package com.example.demoPersonal.mapper.task;

import com.example.demoPersonal.dto.task.TaskRequestDTO;
import com.example.demoPersonal.dto.task.TaskResponseDTO;
import com.example.demoPersonal.entity.Project;
import com.example.demoPersonal.entity.Task;

public interface TaskMapper {
    TaskResponseDTO toDTO(Task task);
    Task toEntity(TaskRequestDTO dto, Project project);
}
