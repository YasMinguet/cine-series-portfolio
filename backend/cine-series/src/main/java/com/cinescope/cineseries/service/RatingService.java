package com.cinescope.cineseries.service;

import com.cinescope.cineseries.dto.CurrentUserDto;
import com.cinescope.cineseries.dto.request.RatingRequest;
import com.cinescope.cineseries.dto.response.RatingResponse;

import java.util.List;

public interface RatingService {
    RatingResponse addOrUpdateRating(CurrentUserDto user, RatingRequest dto);
    List<RatingResponse> getRatingsByUser(Long userId);
    List<RatingResponse> getRatingsBySeries(Long seriesId);
}
