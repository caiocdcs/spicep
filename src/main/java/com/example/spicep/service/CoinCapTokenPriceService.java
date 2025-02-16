package com.example.spicep.service;

import com.example.spicep.model.coincap.CoinCapResponse;
import com.example.spicep.model.coincap.CoinCapToken;
import com.example.spicep.model.TokenPrice;
import com.example.spicep.model.TokenSymbol;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Optional;

@Service
public class CoinCapTokenPriceService implements TokenPriceService {

    private static final String ASSETS_URI = "assets";

    private final RestClient restClient;

    public CoinCapTokenPriceService(@Qualifier("coinCapRestClient") RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public Optional<TokenPrice> getTokenPrice(TokenSymbol symbol) {
        var response = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .pathSegment(ASSETS_URI)
                        .pathSegment(symbol.getName())
                        .build())
                .retrieve()
                .toEntity(new ParameterizedTypeReference<CoinCapResponse<CoinCapToken>>() {
                });

        if (response.getStatusCode() == HttpStatus.OK) {
            var data = response.getBody().data();

            return Optional.of(new TokenPrice(data.symbol(), data.price()));
        }
        return Optional.empty();
    }
}
