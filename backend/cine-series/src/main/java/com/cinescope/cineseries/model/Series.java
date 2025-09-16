package com.cinescope.cineseries.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name = "Series")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Series {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private Integer release_year;

    private String poster_url;

    @ManyToOne
    @JoinColumn(name = "genre_id")
    private Genre genre;

    private BigDecimal average_rating;

    @OneToMany(mappedBy = "series", cascade = CascadeType.ALL)
    private Set<Rating> ratings;
}
