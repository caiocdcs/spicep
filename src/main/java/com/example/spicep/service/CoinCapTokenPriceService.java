package com.example.spicep.service;

import com.example.spicep.model.coincap.CoinCapResponse;
import com.example.spicep.model.coincap.CoinCapToken;
import com.example.spicep.model.TokenPrice;
import com.example.spicep.model.TokenSymbol;
import io.github.resilience4j.bulkhead.BulkheadFullException;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Optional;

@Service
@Slf4j
public class CoinCapTokenPriceService implements TokenPriceService {

    private static final String ASSETS_URI = "assets";

    private final RestClient restClient;

    public CoinCapTokenPriceService(@Qualifier("coinCapRestClient") RestClient restClient) {
        this.restClient = restClient;
    }

    @Bulkhead(name = "coinCapTokenPriceBulkhead", type = Bulkhead.Type.SEMAPHORE, fallbackMethod = "getTokenPriceFallback")
    @Override
    public Optional<TokenPrice> getTokenPrice(TokenSymbol symbol) {
        log.info("Getting token price for symbol {}", symbol);
        var response = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .pathSegment(ASSETS_URI)
                        .pathSegment(symbol.getCode())
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

    public Optional<TokenPrice> getTokenPriceFallback(TokenSymbol tokenSymbol, BulkheadFullException ex) {
        log.error("error getting token price for {}", tokenSymbol, ex);

        return Optional.empty();
    }
}
