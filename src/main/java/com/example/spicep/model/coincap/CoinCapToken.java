package com.example.spicep.model.coincap;

import com.example.spicep.model.model.TokenSymbol;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CoinCapToken(
        TokenSymbol symbol,
        String id,
        @JsonProperty("priceUsd") BigDecimal price
) {
}
