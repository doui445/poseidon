package com.nnk.poseidon.domain.dto;

import jakarta.validation.constraints.NotBlank;

public record RuleRequest(
        Integer id,

        @NotBlank(message = "Name is mandatory")
        String name,

        @NotBlank(message = "Description is mandatory")
        String description,

        @NotBlank(message = "Json is mandatory")
        String json,

        @NotBlank(message = "Template is mandatory")
        String template,

        @NotBlank(message = "Sql is mandatory")
        String sql,

        @NotBlank(message = "SqlPart is mandatory")
        String sqlPart
) {

}
