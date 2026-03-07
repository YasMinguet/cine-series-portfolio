package com.cinescope.user.service;

import com.cinescope.user.dto.response.UserResponse;
import com.cinescope.user.entity.AppUser;
import com.cinescope.user.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AppUser register(AppUser user) {
        log.info("Registrando nuevo usuario: {}", user.getUsername());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        AppUser saved = userRepository.save(user);
        log.info("Usuario registrado con ID: {}", saved.getId());
        return saved;
    }

    public Optional<AppUser> findByUsername(String username) {
        log.debug("Buscando usuario por username: {}", username);
        return userRepository.findByUsername(username);
    }

    public boolean existsByUsername(String username) {
        log.debug("Verificando existencia de usuario: {}", username);
        return userRepository.findByUsername(username).isPresent();
    }

    public Optional<AppUser> findById(Long id) {
        log.debug("Buscando usuario por ID: {}", id);
        return userRepository.findById(id);
    }

    public Optional<UserResponse> getUserResponseById(Long id) {
        log.debug("Obteniendo respuesta de usuario por ID: {}", id);
        return findById(id).map(this::convertToResponse);
    }

    private UserResponse convertToResponse(AppUser user) {
        return new UserResponse(user.getId(), user.getUsername(), user.getRole());
    }
}
