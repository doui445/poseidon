package com.nnk.poseidon.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record RatingRequest(
        Integer id,

        @NotBlank(message = "Moody's Rating is mandatory")
        String moodysRating,

        @NotBlank(message = "SandP Rating is mandatory")
        String sandPRating,

        @NotBlank(message = "Fitch Rating is mandatory")
        String fitchRating,

        @NotNull(message = "Order Number is mandatory")
        @Positive(message = "Must be a positive number")
        Integer orderNumber
) {

}
