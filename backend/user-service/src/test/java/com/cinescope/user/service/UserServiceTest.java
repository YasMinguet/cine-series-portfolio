package com.cinescope.user.service;

import com.cinescope.user.dto.response.UserResponse;
import com.cinescope.user.entity.AppUser;
import com.cinescope.user.repository.AppUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private AppUserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void registerHashesPasswordAndSaves() {
        AppUser input = AppUser.builder().username("yas").password("plain").role("USER").build();
        AppUser saved = AppUser.builder().id(10L).username("yas").password("hashed").role("USER").build();

        when(passwordEncoder.encode("plain")).thenReturn("hashed");
        when(userRepository.save(any(AppUser.class))).thenReturn(saved);

        AppUser result = userService.register(input);

        assertEquals(10L, result.getId());
        assertEquals("hashed", input.getPassword());
        verify(passwordEncoder).encode("plain");
        verify(userRepository).save(input);
    }

    @Test
    void existsByUsernameReturnsTrueWhenUserExists() {
        when(userRepository.findByUsername("yas")).thenReturn(Optional.of(new AppUser()));

        assertTrue(userService.existsByUsername("yas"));
        verify(userRepository).findByUsername("yas");
    }

    @Test
    void getUserResponseByIdMapsEntityToDto() {
        AppUser user = AppUser.builder().id(1L).username("yas").role("USER").build();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<UserResponse> response = userService.getUserResponseById(1L);

        assertTrue(response.isPresent());
        assertEquals("yas", response.get().getUsername());
        assertEquals("USER", response.get().getRole());
    }
}

