package com.cinescope.user.service;

import com.cinescope.user.dto.request.AuthRequest;
import com.cinescope.user.dto.response.AuthResponse;
import com.cinescope.user.entity.AppUser;
import com.cinescope.user.exception.NotFoundException;
import com.cinescope.user.exception.UserAlreadyExistsException;
import com.cinescope.user.security.JwtService;
import com.cinescope.user.util.Constantes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authManager;

    public AuthResponse register(AuthRequest request) {
        log.info("Registrando nuevo usuario: {}", request.getUsername());

        if (userService.existsByUsername(request.getUsername())) {
            log.warn("Intento de registro con usuario existente: {}", request.getUsername());
            throw new UserAlreadyExistsException(Constantes.ERROR_USER_EXISTS);
        }

        AppUser user = AppUser.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .role(Constantes.ROLE_USER)
                .build();

        user = userService.register(user);
        String token = jwtService.generateToken(user.getUsername());

        log.info("Usuario registrado exitosamente: {}", user.getUsername());
        return new AuthResponse(user.getId(), token, user.getUsername(), user.getRole());
    }

    public AuthResponse login(AuthRequest request) {
        log.info("Autenticando usuario: {}", request.getUsername());

        try {
            authManager.authenticate(new UsernamePasswordAuthenticationToken(
                    request.getUsername(),
                    request.getPassword()
            ));
        } catch (Exception e) {
            log.error("Fallo en autenticación para usuario: {}", request.getUsername());
            throw new NotFoundException(Constantes.ERROR_INVALID_CREDENTIALS);
        }

        AppUser user = userService.findByUsername(request.getUsername())
                .orElseThrow(() -> new NotFoundException(Constantes.ERROR_USER_NOT_FOUND));

        String token = jwtService.generateToken(user.getUsername());

        log.info("Usuario autenticado exitosamente: {}", user.getUsername());
        return new AuthResponse(user.getId(), token, user.getUsername(), user.getRole());
    }
}
