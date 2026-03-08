package com.cinescope.rating.service;

import com.cinescope.rating.client.SeriesDto;
import com.cinescope.rating.client.SeriesServiceClient;
import com.cinescope.rating.client.UserDto;
import com.cinescope.rating.client.UserServiceClient;
import com.cinescope.rating.dto.request.RatingRequest;
import com.cinescope.rating.dto.response.RatingResponse;
import com.cinescope.rating.entity.Rating;
import com.cinescope.rating.repository.RatingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RatingServiceTest {

    @Mock
    private RatingRepository ratingRepository;

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private SeriesServiceClient seriesServiceClient;

    @InjectMocks
    private RatingService ratingService;

    @Test
    void createRatingValidatesDependenciesAndSaves() {
        RatingRequest request = new RatingRequest();
        request.setUserId(1L);
        request.setSeriesId(2L);
        request.setScore(5);
        request.setComment("Top");

        when(userServiceClient.getUserById(1L)).thenReturn(new UserDto(1L, "yas", "USER"));
        when(seriesServiceClient.getSeriesById(2L)).thenReturn(new SeriesDto(2L, "BB", "desc", 2008, "Drama"));
        when(ratingRepository.save(any(Rating.class))).thenAnswer(invocation -> {
            Rating r = invocation.getArgument(0);
            r.setId(15L);
            return r;
        });

        RatingResponse response = ratingService.createRating(request);

        assertEquals(15L, response.getId());
        assertEquals(5, response.getScore());
        verify(userServiceClient).getUserById(1L);
        verify(seriesServiceClient).getSeriesById(2L);
    }

    @Test
    void createRatingThrowsWhenUserValidationFails() {
        RatingRequest request = new RatingRequest();
        request.setUserId(90L);
        request.setSeriesId(2L);

        when(userServiceClient.getUserById(90L)).thenThrow(new RuntimeException("404"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> ratingService.createRating(request));
        assertTrue(ex.getMessage().contains("Usuario no encontrado"));
        verify(ratingRepository, never()).save(any());
    }

    @Test
    void getRatingsBySeriesIdMapsResponses() {
        Rating r = Rating.builder().id(1L).userId(1L).seriesId(7L).score(4).comment("ok").build();
        when(ratingRepository.findBySeriesId(7L)).thenReturn(List.of(r));

        List<RatingResponse> result = ratingService.getRatingsBySeriesId(7L);

        assertEquals(1, result.size());
        assertEquals(4, result.get(0).getScore());
    }

    @Test
    void deleteRatingThrowsWhenMissing() {
        when(ratingRepository.existsById(123L)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> ratingService.deleteRating(123L));
        assertTrue(ex.getMessage().contains("Calificación no encontrada"));
    }

    @Test
    void getAllRatingsMapsResponses() {
        Rating r = Rating.builder().id(2L).userId(1L).seriesId(3L).score(5).comment("excelente").build();
        when(ratingRepository.findAll()).thenReturn(List.of(r));

        List<RatingResponse> result = ratingService.getAllRatings();

        assertEquals(1, result.size());
        assertEquals("excelente", result.get(0).getComment());
    }

    @Test
    void getRatingByIdReturnsOptionalWhenFound() {
        Rating r = Rating.builder().id(11L).userId(1L).seriesId(2L).score(4).comment("ok").build();
        when(ratingRepository.findById(11L)).thenReturn(Optional.of(r));

        Optional<RatingResponse> result = ratingService.getRatingById(11L);

        assertTrue(result.isPresent());
        assertEquals(4, result.get().getScore());
    }

    @Test
    void getRatingsByUserIdMapsResponses() {
        Rating r = Rating.builder().id(12L).userId(8L).seriesId(2L).score(3).comment("meh").build();
        when(ratingRepository.findByUserId(8L)).thenReturn(List.of(r));

        List<RatingResponse> result = ratingService.getRatingsByUserId(8L);

        assertEquals(1, result.size());
        assertEquals(8L, result.get(0).getUserId());
    }

    @Test
    void updateRatingSucceedsWhenEntityExistsAndValidationPasses() {
        RatingRequest request = new RatingRequest();
        request.setUserId(1L);
        request.setSeriesId(2L);
        request.setScore(4);
        request.setComment("updated");

        Rating existing = Rating.builder().id(5L).userId(1L).seriesId(2L).score(2).comment("old").build();

        when(ratingRepository.findById(5L)).thenReturn(Optional.of(existing));
        when(userServiceClient.getUserById(1L)).thenReturn(new UserDto(1L, "yas", "USER"));
        when(seriesServiceClient.getSeriesById(2L)).thenReturn(new SeriesDto(2L, "BB", "desc", 2008, "Drama"));
        when(ratingRepository.save(any(Rating.class))).thenAnswer(invocation -> invocation.getArgument(0));

        RatingResponse response = ratingService.updateRating(5L, request);

        assertEquals(4, response.getScore());
        assertEquals("updated", response.getComment());
    }

    @Test
    void updateRatingThrowsWhenSeriesValidationFails() {
        RatingRequest request = new RatingRequest();
        request.setUserId(1L);
        request.setSeriesId(77L);

        Rating existing = Rating.builder().id(5L).userId(1L).seriesId(2L).score(2).comment("old").build();

        when(ratingRepository.findById(5L)).thenReturn(Optional.of(existing));
        when(userServiceClient.getUserById(1L)).thenReturn(new UserDto(1L, "yas", "USER"));
        when(seriesServiceClient.getSeriesById(77L)).thenThrow(new RuntimeException("404"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> ratingService.updateRating(5L, request));
        assertTrue(ex.getMessage().contains("Serie no encontrada"));
    }

    @Test
    void deleteRatingSucceedsWhenExists() {
        when(ratingRepository.existsById(9L)).thenReturn(true);

        ratingService.deleteRating(9L);

        verify(ratingRepository).deleteById(9L);
    }
}
