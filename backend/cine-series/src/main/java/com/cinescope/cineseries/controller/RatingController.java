package com.cinescope.cineseries.controller;

import com.cinescope.cineseries.dto.CurrentUserDto;
import com.cinescope.cineseries.dto.request.RatingRequest;
import com.cinescope.cineseries.dto.response.RatingResponse;
import com.cinescope.cineseries.security.CurrentUser;
import com.cinescope.cineseries.service.RatingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ratings")
@RequiredArgsConstructor
@CrossOrigin(origins = "${app.cors.allowed-origins}")
public class RatingController {

    private final RatingService ratingService;

    @PostMapping
    public ResponseEntity<RatingResponse> addOrUpdate(@CurrentUser CurrentUserDto user,
            @Valid @RequestBody RatingRequest dto) {
        return ResponseEntity.ok(ratingService.addOrUpdateRating(user, dto));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<RatingResponse>> getByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(ratingService.getRatingsByUser(userId));
    }

    @GetMapping("/series/{seriesId}")
    public ResponseEntity<List<RatingResponse>> getBySeries(@PathVariable Long seriesId) {
        return ResponseEntity.ok(ratingService.getRatingsBySeries(seriesId));
    }
}
