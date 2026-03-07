package com.cinescope.rating.controller;

import com.cinescope.rating.dto.request.RatingRequest;
import com.cinescope.rating.dto.response.RatingResponse;
import com.cinescope.rating.service.RatingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ratings")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Ratings", description = "APIs para gestión de calificaciones y reseñas")
public class RatingController {

    private final RatingService ratingService;

    @GetMapping
    @Operation(summary = "Obtener todas las calificaciones", description = "Devuelve una lista de todas las calificaciones disponibles")
    public ResponseEntity<List<RatingResponse>> getAllRatings() {
        return ResponseEntity.ok(ratingService.getAllRatings());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener calificación por ID", description = "Devuelve la información de una calificación específica")
    public ResponseEntity<RatingResponse> getRatingById(@Parameter(description = "ID de la calificación") @PathVariable Long id) {
        return ratingService.getRatingById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/series/{seriesId}")
    @Operation(summary = "Obtener calificaciones por serie", description = "Devuelve todas las calificaciones de una serie específica")
    public ResponseEntity<List<RatingResponse>> getRatingsBySeriesId(@Parameter(description = "ID de la serie") @PathVariable Long seriesId) {
        return ResponseEntity.ok(ratingService.getRatingsBySeriesId(seriesId));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Obtener calificaciones por usuario", description = "Devuelve todas las calificaciones de un usuario específico")
    public ResponseEntity<List<RatingResponse>> getRatingsByUserId(@Parameter(description = "ID del usuario") @PathVariable Long userId) {
        return ResponseEntity.ok(ratingService.getRatingsByUserId(userId));
    }

    @PostMapping
    @Operation(summary = "Crear nueva calificación", description = "Crea una nueva calificación para una serie")
    public ResponseEntity<RatingResponse> createRating(@RequestBody RatingRequest request) {
        return ResponseEntity.ok(ratingService.createRating(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar calificación", description = "Actualiza la información de una calificación existente")
    public ResponseEntity<RatingResponse> updateRating(@Parameter(description = "ID de la calificación") @PathVariable Long id, @RequestBody RatingRequest request) {
        return ResponseEntity.ok(ratingService.updateRating(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar calificación", description = "Elimina una calificación del sistema")
    public ResponseEntity<Void> deleteRating(@Parameter(description = "ID de la calificación") @PathVariable Long id) {
        ratingService.deleteRating(id);
        return ResponseEntity.noContent().build();
    }
}
