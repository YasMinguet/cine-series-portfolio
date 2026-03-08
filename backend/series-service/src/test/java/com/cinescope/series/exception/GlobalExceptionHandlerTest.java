package com.cinescope.series.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleRuntimeExceptionReturnsBadRequest() {
        ResponseEntity<String> response = handler.handleRuntimeException(new RuntimeException("bad request"));

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("bad request", response.getBody());
    }

    @Test
    void handleExceptionReturnsInternalServerError() {
        ResponseEntity<String> response = handler.handleException(new Exception("boom"));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error interno del servidor", response.getBody());
    }
}

