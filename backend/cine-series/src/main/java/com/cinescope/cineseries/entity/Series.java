package com.cinescope.cineseries.entity;

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

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "release_year")
    private Integer releaseYear;

    @Column(name = "poster_url")
    private String posterUrl;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "genre_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Genre genre;

    @Column(name = "average_rating", precision = 3, scale = 1)
    private BigDecimal averageRating;

    @OneToMany(mappedBy = "series", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Rating> ratings;
}
