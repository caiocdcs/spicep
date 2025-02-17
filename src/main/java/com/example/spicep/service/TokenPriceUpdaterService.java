package com.example.spicep.service;

import com.example.spicep.entity.TokenPriceEntity;
import com.example.spicep.model.TokenPrice;
import com.example.spicep.model.TokenSymbol;
import com.example.spicep.repository.TokenPriceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Slf4j
@RequiredArgsConstructor
public class TokenPriceUpdaterService {

    private final TokenPriceRepository tokenPriceRepository;
    private final TokenPriceService tokenPriceService;

    @Async
    public void updateToken(TokenSymbol tokenSymbol) {
        try {
            var tokenPrice = tokenPriceService.getTokenPrice(tokenSymbol);

            tokenPrice.ifPresent(this::updateDatabase);
        } catch (Exception e) {
            log.error("Error updating token symbol: {}", tokenSymbol, e);
        }
    }

    private void updateDatabase(TokenPrice tokenPrice) {
        var oldToken = tokenPriceRepository.findByTokenSymbol(tokenPrice.symbol());

        final var tokenPriceEntity = oldToken.map((tpe) -> {
            tpe.setPrice(tokenPrice.price());
            tpe.setTimestamp(Instant.now());
            return tpe;
        }).orElseGet(() -> {
            var tpe = new TokenPriceEntity();
            tpe.setTokenSymbol(tokenPrice.symbol());
            tpe.setPrice(tokenPrice.price());
            tpe.setTimestamp(Instant.now());
            return tpe;
        });

        tokenPriceRepository.save(tokenPriceEntity);
    }
}
