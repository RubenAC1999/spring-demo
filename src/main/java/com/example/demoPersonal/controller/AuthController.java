package com.example.demoPersonal.controller;

import com.example.demoPersonal.dto.login.RegisterRequestDTO;
import com.example.demoPersonal.security.CustomUserDetailsService;
import com.example.demoPersonal.security.JwtService;
import com.example.demoPersonal.dto.login.AuthResponseDTO;
import com.example.demoPersonal.dto.login.LoginRequestDTO;
import com.example.demoPersonal.service.AuthService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtService jwtService;

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    public AuthController(AuthService authService, AuthenticationManager authenticationManager, CustomUserDetailsService userDetailsService,
                          JwtService jwtService) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@RequestBody @Valid RegisterRequestDTO dto) {
        return ResponseEntity.status(201).body(authService.register(dto));
    }


    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginRequestDTO dto) {
        return ResponseEntity.ok(authService.login(dto));
    }


}
