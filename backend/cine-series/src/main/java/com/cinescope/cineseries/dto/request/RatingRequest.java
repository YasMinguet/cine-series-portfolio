package com.cinescope.cineseries.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record RatingRequest(
        @NotNull(message = "El ID de la serie es obligatorio")
        Long seriesId,
        @NotNull(message = "La puntuación es obligatoria")
        @DecimalMin(value = "0.0", message = "La puntuación mínima es 0.0")
        @DecimalMax(value = "10.0", message = "La puntuación máxima es 10.0")
        BigDecimal score
) {}
