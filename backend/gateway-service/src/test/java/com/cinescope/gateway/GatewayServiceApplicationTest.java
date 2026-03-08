package com.cinescope.gateway;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import static org.junit.jupiter.api.Assertions.assertTrue;

class GatewayServiceApplicationTest {

    @Test
    void applicationHasExpectedAnnotations() {
        assertTrue(GatewayServiceApplication.class.isAnnotationPresent(SpringBootApplication.class));
        assertTrue(GatewayServiceApplication.class.isAnnotationPresent(EnableDiscoveryClient.class));
    }
}

