package com.cinescope.series.service;

import com.cinescope.series.dto.request.GenreRequest;
import com.cinescope.series.dto.request.SeriesRequest;
import com.cinescope.series.dto.response.GenreResponse;
import com.cinescope.series.dto.response.SeriesResponse;
import com.cinescope.series.entity.Genre;
import com.cinescope.series.entity.Series;
import com.cinescope.series.repository.GenreRepository;
import com.cinescope.series.repository.SeriesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SeriesService {

    private final SeriesRepository seriesRepository;
    private final GenreRepository genreRepository;

    // Series operations
    public List<SeriesResponse> getAllSeries() {
        log.info("Obteniendo todas las series");
        return seriesRepository.findAll().stream()
                .map(this::convertSeriestoResponse)
                .collect(Collectors.toList());
    }

    public Optional<SeriesResponse> getSeriesById(Long id) {
        log.debug("Obteniendo serie por ID: {}", id);
        return seriesRepository.findById(id).map(this::convertSeriestoResponse);
    }

    public SeriesResponse createSeries(SeriesRequest request) {
        log.info("Creando serie: {}", request.getTitle());
        Series series = Series.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .releaseYear(request.getReleaseYear())
                .genre(request.getGenreId() != null ? getGenreEntity(request.getGenreId()) : null)
                .build();
        Series saved = seriesRepository.save(series);
        log.info("Serie guardada con ID: {}", saved.getId());
        return convertSeriestoResponse(saved);
    }

    public SeriesResponse updateSeries(Long id, SeriesRequest request) {
        log.info("Actualizando serie ID: {}", id);
        Series existing = seriesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Serie no encontrada: " + id));

        existing.setTitle(request.getTitle());
        existing.setDescription(request.getDescription());
        existing.setReleaseYear(request.getReleaseYear());
        existing.setGenre(request.getGenreId() != null ? getGenreEntity(request.getGenreId()) : null);

        Series saved = seriesRepository.save(existing);
        log.info("Serie actualizada con ID: {}", saved.getId());
        return convertSeriestoResponse(saved);
    }

    public void deleteSeries(Long id) {
        log.info("Eliminando serie ID: {}", id);
        if (!seriesRepository.existsById(id)) {
            throw new RuntimeException("Serie no encontrada: " + id);
        }
        seriesRepository.deleteById(id);
    }

    // Genre operations
    public List<GenreResponse> getAllGenres() {
        log.info("Obteniendo todos los géneros");
        return genreRepository.findAll().stream()
                .map(this::convertGenreToResponse)
                .collect(Collectors.toList());
    }

    public GenreResponse createGenre(GenreRequest request) {
        log.info("Creando género: {}", request.getName());
        Genre genre = Genre.builder().name(request.getName()).build();
        Genre saved = genreRepository.save(genre);
        log.info("Género guardado con ID: {}", saved.getId());
        return convertGenreToResponse(saved);
    }

    // Helper methods
    private Genre getGenreEntity(Long genreId) {
        return genreRepository.findById(genreId)
                .orElseThrow(() -> new RuntimeException("Género no encontrado: " + genreId));
    }

    private SeriesResponse convertSeriestoResponse(Series series) {
        return new SeriesResponse(series.getId(), series.getTitle(), series.getDescription(),
                series.getReleaseYear(), series.getGenre() != null ? series.getGenre().getName() : null);
    }

    private GenreResponse convertGenreToResponse(Genre genre) {
        return new GenreResponse(genre.getId(), genre.getName());
    }
}
