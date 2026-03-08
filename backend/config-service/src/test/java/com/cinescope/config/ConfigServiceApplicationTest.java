package com.cinescope.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ConfigServiceApplicationTest {

    @Test
    void applicationHasExpectedAnnotations() {
        assertTrue(ConfigServiceApplication.class.isAnnotationPresent(SpringBootApplication.class));
        assertTrue(ConfigServiceApplication.class.isAnnotationPresent(EnableConfigServer.class));
    }
}

