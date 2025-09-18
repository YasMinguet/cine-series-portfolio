package com.cinescope.cineseries.service.impl;

import com.cinescope.cineseries.dto.request.SeriesRequest;
import com.cinescope.cineseries.dto.response.SeriesResponse;
import com.cinescope.cineseries.entity.Genre;
import com.cinescope.cineseries.entity.Rating;
import com.cinescope.cineseries.entity.Series;
import com.cinescope.cineseries.exception.NotFoundException;
import com.cinescope.cineseries.repository.GenreRepository;
import com.cinescope.cineseries.repository.SeriesRepository;
import com.cinescope.cineseries.service.SeriesService;
import com.cinescope.cineseries.util.Constantes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class SeriesServiceImpl implements SeriesService {

    private final SeriesRepository seriesRepository;
    private final GenreRepository genreRepository;

    @Override
    @Transactional(readOnly = true)
    public List<SeriesResponse> getAll() {
        return seriesRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public SeriesResponse getById(Long id) {
        Series series = findSeriesOrThrow(id);
        return mapToDto(series);
    }

    @Override
    public SeriesResponse create(SeriesRequest dto) {
        Genre genre = findGenreOrThrow(dto.getGenreId());

        Series series = Series.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .releaseYear(dto.getReleaseYear())
                .posterUrl(dto.getPosterUrl())
                .genre(genre)
                .averageRating(BigDecimal.ZERO)
                .build();

        return mapToDto(seriesRepository.save(series));
    }

    @Override
    public SeriesResponse update(Long id, SeriesRequest dto) {
        Series series = findSeriesOrThrow(id);

        Genre genre = findGenreOrThrow(dto.getGenreId());

        series.setTitle(dto.getTitle());
        series.setDescription(dto.getDescription());
        series.setReleaseYear(dto.getReleaseYear());
        series.setPosterUrl(dto.getPosterUrl());
        series.setGenre(genre);

        return mapToDto(seriesRepository.save(series));
    }

    @Override
    public void delete(Long id) {
        if (!seriesRepository.existsById(id)) {
            throw new NotFoundException(Constantes.ERROR_SERIE_NOT_FOUND);
        }
        seriesRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SeriesResponse> getByGenre(Long genreId) {
        return seriesRepository.findByGenreId(genreId)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SeriesResponse> getTop10() {
        return seriesRepository.findTop10ByOrderByAverageRatingDesc()
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    // Mapeo entity -> DTO con averageRating dinamico
    private SeriesResponse mapToDto(Series series) {
        BigDecimal averageRating = (series.getRatings() == null || series.getRatings().isEmpty())
                ? BigDecimal.ZERO
                : series.getRatings().stream()
                .map(Rating::getScore)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(series.getRatings().size()), 1, RoundingMode.HALF_UP);

        return new SeriesResponse(
                series.getId(),
                series.getTitle(),
                series.getDescription(),
                series.getReleaseYear(),
                series.getPosterUrl(),
                series.getGenre() != null ? series.getGenre().getName() : null,
                averageRating
        );
    }

    private Series findSeriesOrThrow(Long id) {
        return seriesRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Constantes.ERROR_SERIE_NOT_FOUND));
    }

    private Genre findGenreOrThrow(Long id) {
        return genreRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Constantes.ERROR_GENRE_NOT_FOUND));
    }
}
