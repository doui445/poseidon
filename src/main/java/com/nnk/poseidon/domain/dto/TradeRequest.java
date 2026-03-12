package com.nnk.poseidon.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record TradeRequest(
        Integer id,

        @NotBlank(message = "Account is mandatory")
        String account,

        @NotBlank(message = "Type is mandatory")
        String type,

        @NotNull(message = "Buy Quantity is mandatory")
        @Positive(message = "Must be positive")
        Double buyQuantity
) {
}
