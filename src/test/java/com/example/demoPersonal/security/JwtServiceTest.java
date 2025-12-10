package com.example.demoPersonal.security;

import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;

public class JwtServiceTest {
    private final JwtService jwtService = new JwtService();

    private final UserDetails userDetails = User.withUsername("test@gmail.com")
            .password("encoded-password")
            .authorities("ROLE_USER")
            .build();

    @Test
    void generateToken_shouldContainUsernameInSubject() {
        // WHEN
        String token = jwtService.generateToken(userDetails);
        String usernameFromToken = jwtService.extractUsername(token);

        // THEN
        assertNotNull(token);
        assertFalse(token.isBlank());
        assertEquals(userDetails.getUsername(), usernameFromToken);
    }

    @Test
    void isTokenValid_shouldReturnTrue_whenUserAndTokenAreValid() {
        // GIVEN
        String token = jwtService.generateToken(userDetails);

        // WHEN
        boolean isValid = jwtService.isTokenValid(token, userDetails);

        // THEN
        assertTrue(isValid);
    }

    @Test
    void isTokenValid_shouldReturnFalse_whenIsFromDifferentUser() {
        // GIVEN
        String token = jwtService.generateToken(userDetails);

        UserDetails otherUser = User.withUsername("other@gmail.com")
                .password("encoded-password")
                .authorities("ROLE_USER")
                .build();

        // WHEN
        boolean isValid = jwtService.isTokenValid(token, otherUser);

        // THEN
        assertFalse(isValid);
    }

    @Test
    void extractUsername_shouldThrowException_fromInvalidToken() {
        // GIVEN
        String invalidToken = "invalidToken";

        // WHEN - THEN
        assertThrows(JwtException.class, () -> jwtService.extractUsername(invalidToken));
    }
}
