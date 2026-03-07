package com.cinescope.user.controller;

import com.cinescope.user.dto.response.UserResponse;
import com.cinescope.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Users", description = "APIs para gestión de usuarios")
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    @Operation(summary = "Obtener usuario por ID", description = "Devuelve la información de un usuario específico")
    public ResponseEntity<UserResponse> getUserById(@Parameter(description = "ID del usuario") @PathVariable Long id) {
        return userService.getUserResponseById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
