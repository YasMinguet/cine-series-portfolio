package com.cinescope.series.service;

import com.cinescope.series.dto.request.GenreRequest;
import com.cinescope.series.dto.request.SeriesRequest;
import com.cinescope.series.dto.response.GenreResponse;
import com.cinescope.series.dto.response.SeriesResponse;
import com.cinescope.series.entity.Genre;
import com.cinescope.series.entity.Series;
import com.cinescope.series.repository.GenreRepository;
import com.cinescope.series.repository.SeriesRepository;
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
class SeriesServiceTest {

    @Mock
    private SeriesRepository seriesRepository;

    @Mock
    private GenreRepository genreRepository;

    @InjectMocks
    private SeriesService seriesService;

    @Test
    void getAllSeriesMapsGenreName() {
        Genre genre = Genre.builder().id(5L).name("Drama").build();
        Series series = Series.builder().id(1L).title("Breaking Bad").description("desc").releaseYear(2008).genre(genre).build();
        when(seriesRepository.findAll()).thenReturn(List.of(series));

        List<SeriesResponse> result = seriesService.getAllSeries();

        assertEquals(1, result.size());
        assertEquals("Drama", result.get(0).getGenreName());
    }

    @Test
    void createSeriesUsesGenreIdWhenProvided() {
        SeriesRequest request = new SeriesRequest();
        request.setTitle("Better Call Saul");
        request.setDescription("desc");
        request.setReleaseYear(2015);
        request.setGenreId(3L);

        Genre genre = Genre.builder().id(3L).name("Crime").build();
        when(genreRepository.findById(3L)).thenReturn(Optional.of(genre));
        when(seriesRepository.save(any(Series.class))).thenAnswer(invocation -> {
            Series s = invocation.getArgument(0);
            s.setId(22L);
            return s;
        });

        SeriesResponse response = seriesService.createSeries(request);

        assertEquals(22L, response.getId());
        assertEquals("Crime", response.getGenreName());
    }

    @Test
    void createSeriesWithoutGenreMapsNullGenreName() {
        SeriesRequest request = new SeriesRequest();
        request.setTitle("No Genre");
        request.setDescription("desc");
        request.setReleaseYear(2024);
        request.setGenreId(null);

        when(seriesRepository.save(any(Series.class))).thenAnswer(invocation -> {
            Series s = invocation.getArgument(0);
            s.setId(30L);
            return s;
        });

        SeriesResponse response = seriesService.createSeries(request);

        assertEquals(30L, response.getId());
        assertNull(response.getGenreName());
    }

    @Test
    void getSeriesByIdReturnsMappedResponse() {
        Genre genre = Genre.builder().id(1L).name("Drama").build();
        Series series = Series.builder().id(2L).title("Dark").description("desc").releaseYear(2017).genre(genre).build();
        when(seriesRepository.findById(2L)).thenReturn(Optional.of(series));

        Optional<SeriesResponse> response = seriesService.getSeriesById(2L);

        assertTrue(response.isPresent());
        assertEquals("Dark", response.get().getTitle());
        assertEquals("Drama", response.get().getGenreName());
    }

    @Test
    void updateSeriesSucceedsWhenSeriesExists() {
        SeriesRequest request = new SeriesRequest();
        request.setTitle("Updated");
        request.setDescription("new");
        request.setReleaseYear(2020);
        request.setGenreId(3L);

        Series existing = Series.builder().id(8L).title("old").build();
        Genre genre = Genre.builder().id(3L).name("Crime").build();

        when(seriesRepository.findById(8L)).thenReturn(Optional.of(existing));
        when(genreRepository.findById(3L)).thenReturn(Optional.of(genre));
        when(seriesRepository.save(any(Series.class))).thenAnswer(invocation -> invocation.getArgument(0));

        SeriesResponse response = seriesService.updateSeries(8L, request);

        assertEquals("Updated", response.getTitle());
        assertEquals("Crime", response.getGenreName());
    }

    @Test
    void deleteSeriesSucceedsWhenExists() {
        when(seriesRepository.existsById(6L)).thenReturn(true);

        seriesService.deleteSeries(6L);

        verify(seriesRepository).deleteById(6L);
    }

    @Test
    void getAllGenresReturnsMappedList() {
        when(genreRepository.findAll()).thenReturn(List.of(Genre.builder().id(1L).name("Drama").build()));

        List<GenreResponse> result = seriesService.getAllGenres();

        assertEquals(1, result.size());
        assertEquals("Drama", result.get(0).getName());
    }

    @Test
    void createGenreSavesAndReturnsResponse() {
        GenreRequest request = new GenreRequest();
        request.setName("Thriller");

        when(genreRepository.save(any(Genre.class))).thenAnswer(invocation -> {
            Genre g = invocation.getArgument(0);
            g.setId(10L);
            return g;
        });

        GenreResponse result = seriesService.createGenre(request);

        assertEquals(10L, result.getId());
        assertEquals("Thriller", result.getName());
    }
}
