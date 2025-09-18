package com.cinescope.cineseries.repository;

import com.cinescope.cineseries.entity.Series;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeriesRepository extends JpaRepository<Series, Long> {
    // Ejemplo: buscar series por género
    List<Series> findByGenreId(Long genreId);
    List<Series> findTop10ByOrderByAverageRatingDesc();

    @Modifying
    @Query("UPDATE Series s SET s.averageRating = :average WHERE s.id = :seriesId")
    void updateAverageRating(@Param("seriesId") Long seriesId, @Param("average") Double average);
}
