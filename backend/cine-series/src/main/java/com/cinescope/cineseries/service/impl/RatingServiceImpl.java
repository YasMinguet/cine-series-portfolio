package com.cinescope.cineseries.service.impl;

import com.cinescope.cineseries.dto.CurrentUserDto;
import com.cinescope.cineseries.dto.request.RatingRequest;
import com.cinescope.cineseries.dto.response.RatingResponse;
import com.cinescope.cineseries.entity.Rating;
import com.cinescope.cineseries.entity.Series;
import com.cinescope.cineseries.exception.NotFoundException;
import com.cinescope.cineseries.repository.AppUserRepository;
import com.cinescope.cineseries.repository.RatingRepository;
import com.cinescope.cineseries.repository.SeriesRepository;
import com.cinescope.cineseries.service.RatingService;
import com.cinescope.cineseries.util.Constantes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;
    private final SeriesRepository seriesRepository;
    private final AppUserRepository appUserRepository;

    @Override
    @Transactional
    public RatingResponse addOrUpdateRating(CurrentUserDto user, RatingRequest dto) {
        Series series = findSeriesOrThrow(dto.seriesId());

        Rating rating = ratingRepository.findByUserIdAndSeriesId(user.id(), series.getId())
                .orElse(Rating.builder().series(series).build());

        rating.setUser(appUserRepository.getReferenceById(user.id()));
        rating.setScore(dto.score());
        ratingRepository.save(rating);

        updateSeriesAverageRating(series);

        return mapToDto(rating);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RatingResponse> getRatingsByUser(Long userId) {
        return ratingRepository.findByUserId(userId).stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<RatingResponse> getRatingsBySeries(Long seriesId) {
        return ratingRepository.findBySeriesId(seriesId).stream()
                .map(this::mapToDto)
                .toList();
    }

    private Series findSeriesOrThrow(Long seriesId) {
        return seriesRepository.findById(seriesId)
                .orElseThrow(() -> new NotFoundException(Constantes.ERROR_SERIE_NOT_FOUND));
    }

    private void updateSeriesAverageRating(Series series) {
        Double avg = ratingRepository.findAverageRatingBySeriesId(series.getId());
        BigDecimal averageRating = (avg != null)
                ? BigDecimal.valueOf(avg).setScale(1, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        seriesRepository.updateAverageRating(series.getId(), averageRating);
    }

    private RatingResponse mapToDto(Rating rating) {
        return new RatingResponse(
                rating.getId(),
                rating.getUser().getId(),
                rating.getSeries().getId(),
                rating.getScore(),
                rating.getCreatedAt()
        );
    }
}
