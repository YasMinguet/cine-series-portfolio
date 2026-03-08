package com.cinescope.series.controller;

import com.cinescope.series.dto.request.GenreRequest;
import com.cinescope.series.dto.request.SeriesRequest;
import com.cinescope.series.dto.response.GenreResponse;
import com.cinescope.series.dto.response.SeriesResponse;
import com.cinescope.series.exception.GlobalExceptionHandler;
import com.cinescope.series.service.SeriesService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class SeriesControllerTest {

    @Mock
    private SeriesService seriesService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        SeriesController controller = new SeriesController(seriesService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void getAllSeriesReturnsOk() throws Exception {
        when(seriesService.getAllSeries())
                .thenReturn(List.of(new SeriesResponse(1L, "BB", "desc", 2008, "Drama")));

        mockMvc.perform(get("/api/series"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("BB"));
    }

    @Test
    void getSeriesByIdReturnsNotFoundWhenMissing() throws Exception {
        when(seriesService.getSeriesById(77L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/series/77"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createSeriesReturnsBadRequestOnRuntimeException() throws Exception {
        SeriesRequest request = new SeriesRequest();
        request.setTitle("new");

        when(seriesService.createSeries(any(SeriesRequest.class)))
                .thenThrow(new RuntimeException("Género no encontrado"));

        mockMvc.perform(post("/api/series")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Género no encontrado"));
    }

    @Test
    void getSeriesByIdReturnsOkWhenFound() throws Exception {
        when(seriesService.getSeriesById(2L))
                .thenReturn(Optional.of(new SeriesResponse(2L, "Dark", "desc", 2017, "Sci-Fi")));

        mockMvc.perform(get("/api/series/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Dark"));
    }

    @Test
    void updateSeriesReturnsOk() throws Exception {
        SeriesRequest request = new SeriesRequest();
        request.setTitle("Updated");

        when(seriesService.updateSeries(eq(3L), any(SeriesRequest.class)))
                .thenReturn(new SeriesResponse(3L, "Updated", "desc", 2020, "Drama"));

        mockMvc.perform(put("/api/series/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated"));
    }

    @Test
    void deleteSeriesReturnsNoContent() throws Exception {
        doNothing().when(seriesService).deleteSeries(4L);

        mockMvc.perform(delete("/api/series/4"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getAllGenresReturnsOk() throws Exception {
        when(seriesService.getAllGenres())
                .thenReturn(List.of(new GenreResponse(1L, "Drama")));

        mockMvc.perform(get("/api/series/genres"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Drama"));
    }

    @Test
    void createGenreReturnsOk() throws Exception {
        GenreRequest request = new GenreRequest();
        request.setName("Thriller");

        when(seriesService.createGenre(any(GenreRequest.class)))
                .thenReturn(new GenreResponse(10L, "Thriller"));

        mockMvc.perform(post("/api/series/genres")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Thriller"));
    }
}
