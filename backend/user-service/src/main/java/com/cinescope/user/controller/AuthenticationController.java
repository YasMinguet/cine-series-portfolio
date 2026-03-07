package com.cinescope.user.controller;

import com.cinescope.user.dto.request.AuthRequest;
import com.cinescope.user.dto.response.AuthResponse;
import com.cinescope.user.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Authentication", description = "APIs para autenticación de usuarios")
public class AuthenticationController {

    private final AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Registrar nuevo usuario", description = "Crea una nueva cuenta de usuario con username y password")
    public ResponseEntity<AuthResponse> register(@RequestBody AuthRequest request) {
        log.info("Solicitud de registro para usuario: {}", request.getUsername());
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión", description = "Autentica al usuario y devuelve un token JWT")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        log.info("Solicitud de login para usuario: {}", request.getUsername());
        return ResponseEntity.ok(authService.login(request));
    }
}
