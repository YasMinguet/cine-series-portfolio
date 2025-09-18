package com.cinescope.cineseries.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeriesRequest {
    private String title;
    private String description;
    private Integer releaseYear;
    private String posterUrl;
    private Long genreId;
}
