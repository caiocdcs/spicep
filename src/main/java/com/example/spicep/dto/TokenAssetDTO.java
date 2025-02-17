package com.example.spicep.dto;

import com.example.spicep.model.model.TokenSymbol;

import java.math.BigDecimal;

public record TokenAssetDTO(
        TokenSymbol symbol,
        BigDecimal quantity,
        BigDecimal price,
        BigDecimal value) {
}
