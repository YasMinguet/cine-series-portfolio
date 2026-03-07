package com.cinescope.series.controller;

import com.cinescope.series.dto.request.GenreRequest;
import com.cinescope.series.dto.request.SeriesRequest;
import com.cinescope.series.dto.response.GenreResponse;
import com.cinescope.series.dto.response.SeriesResponse;
import com.cinescope.series.service.SeriesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/series")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Series", description = "APIs para gestión de series y géneros")
public class SeriesController {

    private final SeriesService seriesService;

    @GetMapping
    @Operation(summary = "Obtener todas las series", description = "Devuelve una lista de todas las series disponibles")
    public ResponseEntity<List<SeriesResponse>> getAllSeries() {
        return ResponseEntity.ok(seriesService.getAllSeries());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener serie por ID", description = "Devuelve la información de una serie específica")
    public ResponseEntity<SeriesResponse> getSeriesById(@Parameter(description = "ID de la serie") @PathVariable Long id) {
        return seriesService.getSeriesById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Crear nueva serie", description = "Crea una nueva serie en el sistema")
    public ResponseEntity<SeriesResponse> createSeries(@RequestBody SeriesRequest request) {
        return ResponseEntity.ok(seriesService.createSeries(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar serie", description = "Actualiza la información de una serie existente")
    public ResponseEntity<SeriesResponse> updateSeries(@Parameter(description = "ID de la serie") @PathVariable Long id, @RequestBody SeriesRequest request) {
        return ResponseEntity.ok(seriesService.updateSeries(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar serie", description = "Elimina una serie del sistema")
    public ResponseEntity<Void> deleteSeries(@Parameter(description = "ID de la serie") @PathVariable Long id) {
        seriesService.deleteSeries(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/genres")
    @Operation(summary = "Obtener todos los géneros", description = "Devuelve una lista de todos los géneros disponibles")
    public ResponseEntity<List<GenreResponse>> getAllGenres() {
        return ResponseEntity.ok(seriesService.getAllGenres());
    }

    @PostMapping("/genres")
    @Operation(summary = "Crear nuevo género", description = "Crea un nuevo género en el sistema")
    public ResponseEntity<GenreResponse> createGenre(@RequestBody GenreRequest request) {
        return ResponseEntity.ok(seriesService.createGenre(request));
    }
}
