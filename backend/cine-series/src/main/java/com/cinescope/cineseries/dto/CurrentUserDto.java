package com.cinescope.cineseries.dto;

public record CurrentUserDto(
        Long id,
        String username,
        String role
) {}
