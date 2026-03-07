package com.cinescope.rating.repository;

import com.cinescope.rating.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findBySeriesId(Long seriesId);
    List<Rating> findByUserId(Long userId);
}
