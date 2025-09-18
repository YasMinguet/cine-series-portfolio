package com.cinescope.cineseries.service;

import com.cinescope.cineseries.dto.request.SeriesRequest;
import com.cinescope.cineseries.dto.response.SeriesResponse;

import java.util.List;

public interface SeriesService {
    List<SeriesResponse> getAll();
    SeriesResponse getById(Long id);
    SeriesResponse create(SeriesRequest request);
    SeriesResponse update(Long id, SeriesRequest request);
    void delete(Long id);
    List<SeriesResponse> getByGenre(Long genreId);
    List<SeriesResponse>getTop10();
}
