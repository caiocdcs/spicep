package com.example.spicep.model;

import com.example.spicep.model.model.TokenSymbol;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Asset(
        @NotNull TokenSymbol symbol,
        @NotNull BigDecimal price,
        @NotNull BigDecimal quantity) {
}
