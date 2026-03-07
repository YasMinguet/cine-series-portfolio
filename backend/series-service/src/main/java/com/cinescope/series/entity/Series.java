package com.cinescope.series.entity;

import jakarta.persistence.*;
import lombok.*;

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

    @Column(length = 1000)
    private String description;

    @Column(name = "release_year")
    private Integer releaseYear;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "genre_id")
    private Genre genre;

    // In microservices, ratings are in another service
    // @OneToMany(mappedBy = "series", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    // @ToString.Exclude
    // @EqualsAndHashCode.Exclude
    // private Set<Rating> ratings;
}
