package com.nnk.poseidon.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserUpdateDTO(

        @NotNull(message = "Id is mandatory")
        Integer id,

        @NotBlank(message = "Username is required")
        String username,

        @Size(min = 8, message = "Password must be at least 8 characters")
        @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
                message = "Password must be stronger")
        String password,

        @NotBlank(message = "FullName is required")
        String fullname,

        @NotBlank(message = "Role is required")
        String role
) {
}
