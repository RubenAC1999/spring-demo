package com.example.demoPersonal.service;

import com.example.demoPersonal.dto.login.AuthResponseDTO;
import com.example.demoPersonal.dto.login.LoginRequestDTO;
import com.example.demoPersonal.dto.login.RegisterRequestDTO;
import com.example.demoPersonal.entity.Employee;
import com.example.demoPersonal.entity.enums.Role;
import com.example.demoPersonal.exception.EmployeeExistsException;
import com.example.demoPersonal.repository.EmployeeRepository;
import com.example.demoPersonal.security.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthService {
    private final EmployeeRepository employeeRepository;
    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    public AuthService(EmployeeRepository employeeRepository, JwtService jwtService,
                       AuthenticationManager authenticationManager, UserDetailsService userDetailsService,
                       PasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponseDTO register(RegisterRequestDTO dto) {
        String email = dto.email().toLowerCase();

        log.info("Register attempt for {}", email);

        if (employeeRepository.existsByEmail(email)) {
            throw new EmployeeExistsException(email);
        }

        Employee employee = new Employee();
        employee.setName(dto.name());
        employee.setEmail(email);
        employee.setPassword(passwordEncoder.encode(dto.password()));
        employee.setPosition(dto.position());
        employee.setRole(Role.ROLE_USER);

        Employee saved = employeeRepository.save(employee);

        Authentication authToken = new UsernamePasswordAuthenticationToken(
                saved.getEmail(),
                dto.password()
        );

        authenticationManager.authenticate(authToken);

        UserDetails userDetails = userDetailsService.loadUserByUsername(saved.getEmail());
        String token = jwtService.generateToken(userDetails);
        log.info("Registration successfull for {}", saved.getEmail());

        return new AuthResponseDTO(token);
    }

    public AuthResponseDTO login(LoginRequestDTO dto) {
        String email = dto.email().toLowerCase();

        log.info("Login attempt for {}", email);

        Authentication authToken = new UsernamePasswordAuthenticationToken(
                email,
                dto.password());

        authenticationManager.authenticate(authToken);

        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        String token = jwtService.generateToken(userDetails);

        log.info("Login successfull for {}", email);

        return new AuthResponseDTO(token);
    }
}
