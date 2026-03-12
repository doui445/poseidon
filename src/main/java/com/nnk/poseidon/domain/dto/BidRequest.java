package com.nnk.poseidon.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record BidRequest(
        Integer id,

        @NotBlank(message = "Account is mandatory")
        String account,

        @NotBlank(message = "Type is mandatory")
        String type,

        @Positive(message = "Bid Quantity must be positive")
        Double bidQuantity
) {

}
