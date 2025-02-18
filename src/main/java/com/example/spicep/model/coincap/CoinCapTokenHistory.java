package com.example.spicep.model.coincap;

import com.example.spicep.model.model.TokenSymbol;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.Instant;

public record CoinCapTokenHistory(
        TokenSymbol symbol,
        Instant time,
        @JsonProperty("priceUsd") BigDecimal price
) {
}
