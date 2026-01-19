package com.example.demoPersonal.controller;

import com.example.demoPersonal.dto.login.AuthResponseDTO;
import com.example.demoPersonal.dto.login.LoginRequestDTO;
import com.example.demoPersonal.dto.login.RegisterRequestDTO;
import com.example.demoPersonal.security.CustomUserDetailsService;
import com.example.demoPersonal.security.JwtService;
import com.example.demoPersonal.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthController.class)
@AutoConfigureMockMvc(addFilters = false) // Necesario para desactivar filtros security
public class AuthControllerTest {
    @Autowired MockMvc mockMvc;

    @MockitoBean JwtService jwtService;
    @MockitoBean CustomUserDetailsService customUserDetailsService;

    // MockBean está deprecated
    @MockitoBean AuthService authService;

    @Test
    void login_shouldReturnToken() throws Exception {
        when(authService.login(any(LoginRequestDTO.class))).thenReturn(new AuthResponseDTO("jwt"));

        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"email":"test@gmail.com", "password":"abc123."}
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt"));
    }

    @Test
    void register_shouldReturnToken() throws Exception {
        when(authService.register(any(RegisterRequestDTO.class))).thenReturn(new AuthResponseDTO("jwt"));

        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"name":"test", "email":"test@gmail.com", "position":"DEVELOPER", "password":"abc123."}
                        """))
                .andExpect(status().is(201))
                .andExpect(jsonPath("$.token").value("jwt"));
    }

}
