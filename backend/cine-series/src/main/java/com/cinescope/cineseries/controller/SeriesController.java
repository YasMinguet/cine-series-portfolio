package com.cinescope.cineseries.controller;

import com.cinescope.cineseries.dto.request.SeriesRequest;
import com.cinescope.cineseries.dto.response.SeriesResponse;
import com.cinescope.cineseries.service.SeriesService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/series")
@CrossOrigin(origins = "${app.cors.allowed-origins}")
@RequiredArgsConstructor
public class SeriesController {

    private final SeriesService seriesService;

    @GetMapping
    public List<SeriesResponse> getAll() {
        return seriesService.getAll();
    }

    @GetMapping("/{id}")
    public SeriesResponse getById(@PathVariable Long id) {
        return seriesService.getById(id);
    }

    @GetMapping("/genre/{genreId}")
    public List<SeriesResponse> getByGenre(@PathVariable Long genreId) {
        return seriesService.getByGenre(genreId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public SeriesResponse create(@RequestBody SeriesRequest dto) {
        return seriesService.create(dto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public SeriesResponse update(@PathVariable Long id, @RequestBody SeriesRequest dto) {
        return seriesService.update(id, dto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        seriesService.delete(id);
    }

    @GetMapping("/top")
    public List<SeriesResponse> getTop10() {
        return seriesService.getTop10();
    }
}
