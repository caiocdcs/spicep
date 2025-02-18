package com.example.spicep.model.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TokenSymbol {
    BTC("bitcoin"),
    ETH("ethereum"),
    BCH("bitcoin-cash"),
    BNB("binance-coin"),
    EOS("eos");

    private final String code;
}
