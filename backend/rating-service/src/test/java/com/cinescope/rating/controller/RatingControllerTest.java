package com.cinescope.rating.controller;

import com.cinescope.rating.dto.request.RatingRequest;
import com.cinescope.rating.dto.response.RatingResponse;
import com.cinescope.rating.exception.GlobalExceptionHandler;
import com.cinescope.rating.service.RatingService;
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
class RatingControllerTest {

    @Mock
    private RatingService ratingService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        RatingController controller = new RatingController(ratingService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void getAllRatingsReturnsOk() throws Exception {
        when(ratingService.getAllRatings())
                .thenReturn(List.of(new RatingResponse(1L, 1L, 2L, 4, "ok")));

        mockMvc.perform(get("/api/ratings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void getRatingByIdReturnsOkWhenFound() throws Exception {
        when(ratingService.getRatingById(9L))
                .thenReturn(java.util.Optional.of(new RatingResponse(9L, 1L, 2L, 5, "great")));

        mockMvc.perform(get("/api/ratings/9"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.score").value(5));
    }

    @Test
    void getRatingByIdReturnsNotFoundWhenMissing() throws Exception {
        when(ratingService.getRatingById(99L)).thenReturn(java.util.Optional.empty());

        mockMvc.perform(get("/api/ratings/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getRatingsByUserReturnsOk() throws Exception {
        when(ratingService.getRatingsByUserId(5L))
                .thenReturn(List.of(new RatingResponse(3L, 5L, 2L, 3, "meh")));

        mockMvc.perform(get("/api/ratings/user/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value(5));
    }

    @Test
    void createRatingReturnsOkWhenValid() throws Exception {
        RatingRequest request = new RatingRequest();
        request.setUserId(1L);
        request.setSeriesId(2L);
        request.setScore(5);
        request.setComment("excelente");

        when(ratingService.createRating(any(RatingRequest.class)))
                .thenReturn(new RatingResponse(10L, 1L, 2L, 5, "excelente"));

        mockMvc.perform(post("/api/ratings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10));
    }

    @Test
    void updateRatingReturnsOk() throws Exception {
        RatingRequest request = new RatingRequest();
        request.setUserId(1L);
        request.setSeriesId(2L);
        request.setScore(4);
        request.setComment("updated");

        when(ratingService.updateRating(eq(3L), any(RatingRequest.class)))
                .thenReturn(new RatingResponse(3L, 1L, 2L, 4, "updated"));

        mockMvc.perform(put("/api/ratings/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comment").value("updated"));
    }

    @Test
    void deleteRatingReturnsNoContent() throws Exception {
        doNothing().when(ratingService).deleteRating(4L);

        mockMvc.perform(delete("/api/ratings/4"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getRatingsBySeriesReturnsOk() throws Exception {
        when(ratingService.getRatingsBySeriesId(2L))
                .thenReturn(List.of(new RatingResponse(1L, 1L, 2L, 5, "top")));

        mockMvc.perform(get("/api/ratings/series/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].score").value(5));
    }

    @Test
    void createRatingReturnsBadRequestWhenValidationFails() throws Exception {
        RatingRequest request = new RatingRequest();
        request.setUserId(999L);
        request.setSeriesId(2L);
        request.setScore(3);

        when(ratingService.createRating(any(RatingRequest.class)))
                .thenThrow(new RuntimeException("Usuario no encontrado: 999"));

        mockMvc.perform(post("/api/ratings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Usuario no encontrado: 999"));
    }
}
