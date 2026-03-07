package com.cinescope.rating.dto.request;

import lombok.Data;

@Data
public class RatingRequest {
    private Long userId;
    private Long seriesId;
    private Integer score;
    private String comment;
}
