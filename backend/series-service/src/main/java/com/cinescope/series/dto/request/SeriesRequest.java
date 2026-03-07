package com.cinescope.series.dto.request;

import lombok.Data;

@Data
public class SeriesRequest {
    private String title;
    private String description;
    private Integer releaseYear;
    private Long genreId;
}
