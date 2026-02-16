package com.example.demoPersonal.controller;

import com.example.demoPersonal.dto.task.TaskRequestDTO;
import com.example.demoPersonal.dto.task.TaskResponseDTO;
import com.example.demoPersonal.entity.enums.Status;
import com.example.demoPersonal.security.CustomUserDetailsService;
import com.example.demoPersonal.security.JwtService;
import com.example.demoPersonal.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TaskController.class)
@AutoConfigureMockMvc(addFilters = false)
class TaskControllerTest {
    @Autowired MockMvc mockMvc;

    @MockitoBean TaskService taskService;
    @MockitoBean JwtService jwtService;
    @MockitoBean CustomUserDetailsService userDetailsService;

    @Test
    void createTask_shouldReturn201() throws Exception {
        UUID projectUuid = UUID.fromString("11111111-1111-1111-1111-111111111111");

        when(taskService.createTask(any(TaskRequestDTO.class))).thenReturn(new TaskResponseDTO(
                UUID.randomUUID(),
                "Test",
                Status.TODO,
                LocalDateTime.now(),
                LocalDateTime.now(),
                UUID.randomUUID(),
                projectUuid
        ));

        mockMvc.perform(post("/api/v1/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                       {
                       "description":"Test", "status":"TODO", "projectUuid":"11111111-1111-1111-1111-111111111111"
                       }
                       """))
                .andExpect(jsonPath("$.description").value("Test"))
                .andExpect(status().isCreated());

        verify(taskService).createTask(any(TaskRequestDTO.class));
    }

    @Test
    void removeTask_shouldReturn204() throws Exception {
        UUID uuid = UUID.randomUUID();

        doNothing().when(taskService).removeTask(uuid);

        mockMvc.perform(delete("/api/v1/tasks/{uuid}", uuid)).andExpect(status().isNoContent());

        verify(taskService).removeTask(uuid);
    }
}
