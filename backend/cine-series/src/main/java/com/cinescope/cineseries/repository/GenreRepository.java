package com.cinescope.cineseries.repository;

import com.cinescope.cineseries.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {
    // Puedes añadir métodos personalizados si quieres
}
