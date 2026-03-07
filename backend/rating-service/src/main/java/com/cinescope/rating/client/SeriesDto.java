package com.cinescope.rating.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeriesDto {
    private Long id;
    private String title;
    private String description;
    private Integer releaseYear;
    private String genreName;
}
