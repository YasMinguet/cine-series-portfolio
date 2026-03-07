package com.cinescope.series.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SeriesResponse {
    private Long id;
    private String title;
    private String description;
    private Integer releaseYear;
    private String genreName;
}
