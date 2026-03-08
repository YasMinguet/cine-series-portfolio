package com.cinescope.user.controller;

import com.cinescope.user.dto.request.AuthRequest;
import com.cinescope.user.dto.response.AuthResponse;
import com.cinescope.user.exception.GlobalExceptionHandler;
import com.cinescope.user.exception.NotFoundException;
import com.cinescope.user.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {

    @Mock
    private AuthService authService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        AuthenticationController controller = new AuthenticationController(authService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void registerReturnsOkAndPayload() throws Exception {
        AuthRequest request = new AuthRequest();
        request.setUsername("yas");
        request.setPassword("pass");

        when(authService.register(any(AuthRequest.class)))
                .thenReturn(new AuthResponse(1L, "jwt-token", "yas", "USER"));

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.username").value("yas"));
    }

    @Test
    void loginReturnsNotFoundWhenServiceThrows() throws Exception {
        AuthRequest request = new AuthRequest();
        request.setUsername("yas");
        request.setPassword("bad");

        when(authService.login(any(AuthRequest.class)))
                .thenThrow(new NotFoundException("Credenciales inválidas"));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("Credenciales inválidas"));
    }

    @Test
    void loginReturnsOkAndPayload() throws Exception {
        AuthRequest request = new AuthRequest();
        request.setUsername("yas");
        request.setPassword("ok");

        when(authService.login(any(AuthRequest.class)))
                .thenReturn(new AuthResponse(1L, "jwt-ok", "yas", "USER"));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-ok"));
    }
}
