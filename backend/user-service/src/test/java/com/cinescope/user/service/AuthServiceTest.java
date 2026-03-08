package com.cinescope.user.service;

import com.cinescope.user.dto.request.AuthRequest;
import com.cinescope.user.dto.response.AuthResponse;
import com.cinescope.user.entity.AppUser;
import com.cinescope.user.exception.NotFoundException;
import com.cinescope.user.exception.UserAlreadyExistsException;
import com.cinescope.user.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authManager;

    @InjectMocks
    private AuthService authService;

    @Test
    void registerThrowsWhenUserAlreadyExists() {
        AuthRequest request = new AuthRequest();
        request.setUsername("yas");
        request.setPassword("pwd");

        when(userService.existsByUsername("yas")).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, () -> authService.register(request));
        verify(userService, never()).register(any());
    }

    @Test
    void registerReturnsTokenAndUserData() {
        AuthRequest request = new AuthRequest();
        request.setUsername("yas");
        request.setPassword("pwd");

        AppUser savedUser = AppUser.builder().id(7L).username("yas").role("USER").password("hashed").build();

        when(userService.existsByUsername("yas")).thenReturn(false);
        when(userService.register(any(AppUser.class))).thenReturn(savedUser);
        when(jwtService.generateToken("yas")).thenReturn("jwt-token");

        AuthResponse response = authService.register(request);

        assertEquals(7L, response.getUserId());
        assertEquals("jwt-token", response.getToken());
        assertEquals("yas", response.getUsername());
    }

    @Test
    void loginThrowsNotFoundWhenAuthenticationFails() {
        AuthRequest request = new AuthRequest();
        request.setUsername("yas");
        request.setPassword("bad");

        when(authManager.authenticate(any())).thenThrow(new BadCredentialsException("bad"));

        assertThrows(NotFoundException.class, () -> authService.login(request));
    }

    @Test
    void loginReturnsTokenWhenCredentialsAreValid() {
        AuthRequest request = new AuthRequest();
        request.setUsername("yas");
        request.setPassword("ok");

        AppUser user = AppUser.builder().id(1L).username("yas").role("USER").password("hash").build();

        when(authManager.authenticate(any())).thenReturn(null);
        when(userService.findByUsername("yas")).thenReturn(Optional.of(user));
        when(jwtService.generateToken("yas")).thenReturn("jwt-ok");

        AuthResponse response = authService.login(request);

        assertEquals("jwt-ok", response.getToken());
        assertEquals("yas", response.getUsername());
    }
}

