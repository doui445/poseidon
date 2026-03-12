package com.nnk.poseidon.domain.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CurvePointRequest(
        Integer id,

        @NotNull(message = "Curve Id is mandatory")
        @Positive(message = "Must be a positive number")
        Integer curveId,

        @NotNull(message = "Term is mandatory")
        @Positive(message = "Term must be positive")
        Double term,

        @NotNull(message = "Value is mandatory")
        @Positive(message = "Value must be positive")
        Double value
) {

}
