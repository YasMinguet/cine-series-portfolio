package com.cinescope.series;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import static org.junit.jupiter.api.Assertions.assertTrue;

class SeriesServiceApplicationTest {

    @Test
    void applicationHasExpectedAnnotations() {
        assertTrue(SeriesServiceApplication.class.isAnnotationPresent(SpringBootApplication.class));
        assertTrue(SeriesServiceApplication.class.isAnnotationPresent(EnableDiscoveryClient.class));
    }
}

