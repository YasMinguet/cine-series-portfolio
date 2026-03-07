package com.cinescope.rating.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RatingResponse {
    private Long id;
    private Long userId;
    private Long seriesId;
    private Integer score;
    private String comment;
}
