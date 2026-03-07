package com.cinescope.rating.service;

import com.cinescope.rating.client.SeriesServiceClient;
import com.cinescope.rating.client.UserServiceClient;
import com.cinescope.rating.dto.request.RatingRequest;
import com.cinescope.rating.dto.response.RatingResponse;
import com.cinescope.rating.entity.Rating;
import com.cinescope.rating.repository.RatingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RatingService {

    private final RatingRepository ratingRepository;
    private final UserServiceClient userServiceClient;
    private final SeriesServiceClient seriesServiceClient;

    public List<RatingResponse> getAllRatings() {
        log.info("Obteniendo todas las calificaciones");
        return ratingRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public Optional<RatingResponse> getRatingById(Long id) {
        log.info("Obteniendo calificación por ID: {}", id);
        return ratingRepository.findById(id).map(this::convertToResponse);
    }

    public List<RatingResponse> getRatingsBySeriesId(Long seriesId) {
        log.info("Obteniendo calificaciones para serie ID: {}", seriesId);
        return ratingRepository.findBySeriesId(seriesId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<RatingResponse> getRatingsByUserId(Long userId) {
        log.info("Obteniendo calificaciones para usuario ID: {}", userId);
        return ratingRepository.findByUserId(userId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public RatingResponse createRating(RatingRequest request) {
        log.info("Creando calificación para usuario {} y serie {}", request.getUserId(), request.getSeriesId());

        // Validar usuario y serie
        validateUserAndSeries(request.getUserId(), request.getSeriesId());

        Rating rating = Rating.builder()
                .userId(request.getUserId())
                .seriesId(request.getSeriesId())
                .score(request.getScore())
                .comment(request.getComment())
                .build();

        Rating saved = ratingRepository.save(rating);
        log.info("Calificación guardada con ID: {}", saved.getId());
        return convertToResponse(saved);
    }

    public RatingResponse updateRating(Long id, RatingRequest request) {
        log.info("Actualizando calificación ID: {}", id);

        Rating existing = ratingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Calificación no encontrada: " + id));

        // Validar usuario y serie si cambiaron
        validateUserAndSeries(request.getUserId(), request.getSeriesId());

        existing.setUserId(request.getUserId());
        existing.setSeriesId(request.getSeriesId());
        existing.setScore(request.getScore());
        existing.setComment(request.getComment());

        Rating saved = ratingRepository.save(existing);
        log.info("Calificación actualizada con ID: {}", saved.getId());
        return convertToResponse(saved);
    }

    public void deleteRating(Long id) {
        log.info("Eliminando calificación ID: {}", id);
        if (!ratingRepository.existsById(id)) {
            throw new RuntimeException("Calificación no encontrada: " + id);
        }
        ratingRepository.deleteById(id);
    }

    private void validateUserAndSeries(Long userId, Long seriesId) {
        try {
            userServiceClient.getUserById(userId);
            log.debug("Usuario {} validado", userId);
        } catch (Exception e) {
            log.error("Usuario no encontrado: {}", userId);
            throw new RuntimeException("Usuario no encontrado: " + userId);
        }

        try {
            seriesServiceClient.getSeriesById(seriesId);
            log.debug("Serie {} validada", seriesId);
        } catch (Exception e) {
            log.error("Serie no encontrada: {}", seriesId);
            throw new RuntimeException("Serie no encontrada: " + seriesId);
        }
    }

    private RatingResponse convertToResponse(Rating rating) {
        return new RatingResponse(rating.getId(), rating.getUserId(), rating.getSeriesId(),
                rating.getScore(), rating.getComment());
    }
}
