package com.cinescope.rating.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "series-service")
public interface SeriesServiceClient {

    @GetMapping("/api/series/{id}")
    SeriesDto getSeriesById(@PathVariable("id") Long id);
}
