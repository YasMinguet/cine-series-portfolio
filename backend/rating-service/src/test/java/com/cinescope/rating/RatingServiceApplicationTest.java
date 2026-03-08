package com.cinescope.rating;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

import static org.junit.jupiter.api.Assertions.assertTrue;

class RatingServiceApplicationTest {

    @Test
    void applicationHasExpectedAnnotations() {
        assertTrue(RatingServiceApplication.class.isAnnotationPresent(SpringBootApplication.class));
        assertTrue(RatingServiceApplication.class.isAnnotationPresent(EnableDiscoveryClient.class));
        assertTrue(RatingServiceApplication.class.isAnnotationPresent(EnableFeignClients.class));
    }
}

