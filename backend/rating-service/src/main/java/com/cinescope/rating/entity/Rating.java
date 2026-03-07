package com.cinescope.rating.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Rating")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long seriesId;

    @Column(nullable = false)
    private Integer score; // e.g., 1-5 or 1-10

    @Column(length = 500)
    private String comment;
}
