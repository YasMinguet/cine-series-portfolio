package com.cinescope.cineseries.controller;

import com.cinescope.cineseries.dto.request.AuthRequest;
import com.cinescope.cineseries.dto.response.AuthResponse;
import com.cinescope.cineseries.entity.AppUser;
import com.cinescope.cineseries.exception.UserAlreadyExistsException;
import com.cinescope.cineseries.exception.NotFoundException;
import com.cinescope.cineseries.repository.AppUserRepository;
import com.cinescope.cineseries.security.JwtService;
import com.cinescope.cineseries.util.Constantes;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public AuthResponse register(@RequestBody AuthRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException(Constantes.ERROR_USER_EXISTS);
        }

        AppUser user = AppUser.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Constantes.ROLE_USER)
                .build();
        userRepository.save(user);

        String token = jwtService.generateToken(user.getUsername());
        return new AuthResponse(token, user.getUsername(), user.getRole());
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) {
        authManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        AppUser user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new NotFoundException(Constantes.ERROR_USER_NOT_FOUND));

        String token = jwtService.generateToken(user.getUsername());
        return new AuthResponse(token, user.getUsername(), user.getRole());
    }
}
