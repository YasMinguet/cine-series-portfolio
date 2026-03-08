package com.cinescope.user.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleUserAlreadyExistsReturnsConflict() {
        ResponseEntity<String> response = handler.handleUserAlreadyExists(new UserAlreadyExistsException("exists"));

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("exists", response.getBody());
    }

    @Test
    void handleNotFoundReturnsNotFound() {
        ResponseEntity<String> response = handler.handleNotFound(new NotFoundException("not found"));

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("not found", response.getBody());
    }

    @Test
    void handleExceptionReturnsInternalServerError() {
        ResponseEntity<String> response = handler.handleException(new RuntimeException("boom"));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error interno del servidor", response.getBody());
    }
}

