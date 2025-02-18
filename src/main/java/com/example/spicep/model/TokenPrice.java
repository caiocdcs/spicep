package com.example.spicep.model;

import com.example.spicep.model.model.TokenSymbol;

import java.math.BigDecimal;

public record TokenPrice(TokenSymbol symbol, BigDecimal price) {
}
