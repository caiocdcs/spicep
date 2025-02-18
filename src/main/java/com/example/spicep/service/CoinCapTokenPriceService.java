package com.example.spicep.service;

import com.example.spicep.model.coincap.CoinCapResponse;
import com.example.spicep.model.coincap.CoinCapToken;
import com.example.spicep.model.TokenPrice;
import com.example.spicep.model.coincap.CoinCapTokenHistory;
import com.example.spicep.model.model.TokenSymbol;
import io.github.resilience4j.bulkhead.BulkheadFullException;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CoinCapTokenPriceService implements TokenPriceService {

    private static final String ASSETS_URI = "assets";
    private static final String HISTORY_URI = "history";

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

    @Override
    public Optional<TokenPrice> getHistoricalTokenPrice(TokenSymbol symbol, LocalDate date) {
        log.info("Getting token price history for symbol {} on {}", symbol, date);

        final long start = date.minusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli();
        final long end = date.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli();

        var response = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .pathSegment(ASSETS_URI)
                        .pathSegment(symbol.getCode())
                        .pathSegment(HISTORY_URI)
                        .queryParam("interval", "d1")
                        .queryParam("start", start)
                        .queryParam("end", end)
                        .build())
                .retrieve()
                .toEntity(new ParameterizedTypeReference<CoinCapResponse<List<CoinCapTokenHistory>>>() {
                });

        if (response.getStatusCode() == HttpStatus.OK) {
            var data = response.getBody().data();

            if (!data.isEmpty()) {
                return Optional.of(new TokenPrice(symbol, data.get(0).price()));
            }
        }
        return Optional.empty();
    }
}
