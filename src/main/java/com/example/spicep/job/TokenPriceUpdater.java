package com.example.spicep.job;

import com.example.spicep.entity.TokenPriceEntity;
import com.example.spicep.model.TokenPrice;
import com.example.spicep.model.TokenSymbol;
import com.example.spicep.repository.TokenPriceRepository;
import com.example.spicep.service.TokenPriceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Arrays;

@Service
@Slf4j
@RequiredArgsConstructor
public class TokenPriceUpdater {

    private final TokenPriceRepository tokenPriceRepository;
    private final TokenPriceService tokenPriceService;

    @Scheduled(cron = "${job.retrieve-token-price.expression}")
    public void updateTokenPrices() {
        var tokens = TokenSymbol.values();
        Arrays.stream(tokens).forEach(this::updateToken);
    }

    private void updateToken(TokenSymbol tokenSymbol) {
        try {
            var tokenPrice = tokenPriceService.getTokenPrice(tokenSymbol);

            tokenPrice.ifPresent(this::updateDatabase);
        } catch (Exception e) {
            log.error("Error updating token symbol: {}", tokenSymbol, e);
        }
    }

    private void updateDatabase(TokenPrice tokenPrice) {
        var tpe = new TokenPriceEntity();
        tpe.setTokenSymbol(tokenPrice.symbol());
        tpe.setPrice(tokenPrice.price());
        tpe.setTimestamp(Instant.now());
        tokenPriceRepository.save(tpe);
    }
}
