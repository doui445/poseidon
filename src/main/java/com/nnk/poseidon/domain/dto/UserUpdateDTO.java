package com.nnk.poseidon.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserUpdateDTO(

        @NotNull(message = "Id is mandatory")
        Integer id,

        @NotBlank(message = "Username is required")
        String username,

        String password,

        @NotBlank(message = "FullName is required")
        String fullname,

        @NotBlank(message = "Role is required")
        String role
) {}
