package com.cinescope.cineseries.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record RatingResponse(
        Long id,
        Long userId,
        Long seriesId,
        BigDecimal score,
        LocalDateTime createdAt
) {}
