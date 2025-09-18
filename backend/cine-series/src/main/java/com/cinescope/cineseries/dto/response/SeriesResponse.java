package com.cinescope.cineseries.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeriesResponse {
    private Long id;
    private String title;
    private String description;
    private Integer releaseYear;
    private String posterUrl;
    private String genreName;       // Nombre del género
    private BigDecimal averageRating;
}

