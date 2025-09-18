package com.cinescope.cineseries.repository;

import com.cinescope.cineseries.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findBySeriesId(Long seriesId);
    List<Rating> findByUserId(Long userId);

    @Query("SELECT AVG(r.score) FROM Rating r WHERE r.series.id = :seriesId")
    Double findAverageRatingBySeriesId(@Param("seriesId") Long seriesId);

    Optional<Rating> findByUserIdAndSeriesId(Long userId, Long seriesId);

}
