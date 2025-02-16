package com.example.spicep.model.coincap;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CoinCapResponse<T>(T data) {

}
