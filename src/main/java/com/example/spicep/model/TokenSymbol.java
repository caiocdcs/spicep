package com.example.spicep.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TokenSymbol {
    BTC("bitcoin"),
    ETH("ethereum"),
    BCH("bitcoin-cash"),
    EOS("eos");

    private final String name;
}
