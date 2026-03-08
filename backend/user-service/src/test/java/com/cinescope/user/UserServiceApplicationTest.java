package com.cinescope.user;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import static org.junit.jupiter.api.Assertions.assertTrue;

class UserServiceApplicationTest {

    @Test
    void applicationHasExpectedAnnotations() {
        assertTrue(UserServiceApplication.class.isAnnotationPresent(SpringBootApplication.class));
        assertTrue(UserServiceApplication.class.isAnnotationPresent(EnableDiscoveryClient.class));
    }
}

