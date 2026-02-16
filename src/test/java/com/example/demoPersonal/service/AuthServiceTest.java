package com.example.demoPersonal.service;

import com.example.demoPersonal.dto.login.AuthResponseDTO;
import com.example.demoPersonal.dto.login.LoginRequestDTO;
import com.example.demoPersonal.dto.login.RegisterRequestDTO;
import com.example.demoPersonal.entity.Employee;
import com.example.demoPersonal.entity.enums.Position;
import com.example.demoPersonal.entity.enums.Role;
import com.example.demoPersonal.repository.EmployeeRepository;
import com.example.demoPersonal.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private JwtService jwtService;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private UserDetailsService userDetailsService;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @Test
    void register_shouldReturnToken() {
        // GIVEN
        RegisterRequestDTO dto = new RegisterRequestDTO("test","test@gmail.com", Position.DEVELOPER,
                "abc123.");

        when(employeeRepository.existsByEmail(dto.email())).thenReturn(false);

        UUID employeeUuid = UUID.randomUUID();

        Employee registered = new Employee();
        registered.setUuid(employeeUuid);
        registered.setName(dto.name());
        registered.setEmail(dto.email());
        registered.setPosition(dto.position());
        registered.setPassword("encoded");
        registered.setRole(Role.ROLE_USER);

        when(passwordEncoder.encode(dto.password())).thenReturn("encoded");
        when(employeeRepository.save(any(Employee.class))).thenReturn(registered);

        UserDetails userDetails = User.withUsername(registered.getEmail())
                .password(dto.password())
                .authorities(String.valueOf(registered.getRole()))
                .build();

        when(userDetailsService.loadUserByUsername(registered.getEmail())).thenReturn(userDetails);
        when(jwtService.generateToken(any(UserDetails.class))).thenReturn("token");

        // WHEN
        AuthResponseDTO result = authService.register(dto);

        // THEN
        assertNotNull(result);
        assertNotNull(result.token());

        verify(employeeRepository).existsByEmail(dto.email());
        verify(passwordEncoder).encode(dto.password());
        verify(employeeRepository).save(any(Employee.class));
        verify(userDetailsService).loadUserByUsername(registered.getEmail());
        verify(jwtService).generateToken(any(UserDetails.class));
    }

    @Test
    void login_shouldReturnDTO() {
        // GIVEN
        LoginRequestDTO dto = new LoginRequestDTO("test@gmail.com", "abc123.");
        UserDetails userDetails = User.withUsername("test@gmail.com")
                .password("encoded")
                .authorities("ROLE_USER")
                .build();

        when(userDetailsService.loadUserByUsername("test@gmail.com")).thenReturn(userDetails);
        when(jwtService.generateToken(any(UserDetails.class))).thenReturn("token");

        // WHEN
        AuthResponseDTO result = authService.login(dto);

        // THEN
        assertNotNull(result);
        assertNotNull(result.token());

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userDetailsService).loadUserByUsername("test@gmail.com");
        verify(jwtService).generateToken(userDetails);
    }
}
