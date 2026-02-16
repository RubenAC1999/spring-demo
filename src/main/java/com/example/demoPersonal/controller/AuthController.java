package com.example.demoPersonal.controller;

import com.example.demoPersonal.dto.login.AuthResponseDTO;
import com.example.demoPersonal.dto.login.LoginRequestDTO;
import com.example.demoPersonal.dto.login.RegisterRequestDTO;
import com.example.demoPersonal.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authorization", description = "Authorization operations")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(
            summary = "Register an user",
            description = "Create a new user in the system"
    )
    @ApiResponse(responseCode = "200", description = "User registered successfully")
    @ApiResponse(responseCode = "400", description = "Invalid data or bad request")
    @ApiResponse(responseCode = "409", description = "An user already exists with the same email")
    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(
            @RequestBody @Valid RegisterRequestDTO dto) {

        return ResponseEntity
                .status(201)
                .body(authService.register(dto));
    }

    @Operation(
            summary = "Log in",
            description = "Log in in the system"
    )
    @ApiResponse(responseCode = "200", description = "User logged successfully")
    @ApiResponse(responseCode = "400", description = "Invalid data or bad request")
    @ApiResponse(responseCode = "401", description = "Invalid credentials")
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(
            @RequestBody @Valid LoginRequestDTO dto) {

        return ResponseEntity.ok(authService.login(dto));
    }
}
