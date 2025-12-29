package com.example.demoPersonal.controller;

import com.example.demoPersonal.dto.employee.EmployeeRequestDTO;
import com.example.demoPersonal.dto.employee.EmployeeResponseDTO;
import com.example.demoPersonal.entity.enums.Position;
import com.example.demoPersonal.security.CustomUserDetailsService;
import com.example.demoPersonal.security.JwtService;
import com.example.demoPersonal.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = EmployeeController.class)
@AutoConfigureMockMvc(addFilters = false)
public class EmployeeControllerTest {
    @Autowired MockMvc mockMvc;

    @MockitoBean EmployeeService employeeService;
    @MockitoBean JwtService jwtService;
    @MockitoBean CustomUserDetailsService userDetailsService;



    @Test
    void createEmployee_shouldReturn201() throws Exception {
        when(employeeService.createEmployee(any(EmployeeRequestDTO.class))).thenReturn(new EmployeeResponseDTO(
                UUID.randomUUID(),
                "test@gmail.com",
                "test",
                Position.DEVELOPER,
                List.of()));

        mockMvc.perform(post("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        "name":"test",
                        "email":"test@gmail.com", 
                        "position":"DEVELOPER", 
                        "password":"abc123." 
                        }
                        """))
                .andExpect(jsonPath("$.email").value("test@gmail.com"))
                .andExpect(status().isCreated());

        verify(employeeService).createEmployee(any(EmployeeRequestDTO.class));
    }

   @Test
   void removeEmployee_shouldReturn204() throws Exception {
       UUID uuid = UUID.randomUUID();

       doNothing().when(employeeService).removeEmployee(uuid);

       mockMvc.perform(delete("/employees/{uuid}", uuid)).andExpect(status().isNoContent());

       verify(employeeService).removeEmployee(uuid);
    }
}
