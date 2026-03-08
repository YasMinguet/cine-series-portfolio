package com.cinescope.discovery;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

import static org.junit.jupiter.api.Assertions.assertTrue;

class DiscoveryServiceApplicationTest {

    @Test
    void applicationHasExpectedAnnotations() {
        assertTrue(DiscoveryServiceApplication.class.isAnnotationPresent(SpringBootApplication.class));
        assertTrue(DiscoveryServiceApplication.class.isAnnotationPresent(EnableEurekaServer.class));
    }
}

