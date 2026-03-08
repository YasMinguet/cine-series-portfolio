package com.cinescope.user.security;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    @Test
    void generateExtractAndValidateToken() {
        JwtService jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "jwtSecret", "mySecretKey1234567890123456789012345678901234567890");
        ReflectionTestUtils.setField(jwtService, "jwtExpirationMs", 60000L);

        String token = jwtService.generateToken("yas");

        assertNotNull(token);
        assertTrue(jwtService.validateToken(token));
        assertEquals("yas", jwtService.extractUsername(token));
    }

    @Test
    void validateTokenReturnsFalseForInvalidToken() {
        JwtService jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "jwtSecret", "mySecretKey1234567890123456789012345678901234567890");
        ReflectionTestUtils.setField(jwtService, "jwtExpirationMs", 60000L);

        assertFalse(jwtService.validateToken("not-a-jwt"));
    }
}

