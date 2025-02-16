package com.example.spicep.service;

import com.example.spicep.model.TokenPrice;
import com.example.spicep.model.TokenSymbol;

import java.util.Optional;

public interface TokenPriceService {

    Optional<TokenPrice> getTokenPrice(TokenSymbol symbol);
}
