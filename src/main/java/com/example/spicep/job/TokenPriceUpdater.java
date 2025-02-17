package com.example.spicep.job;

import com.example.spicep.model.TokenSymbol;
import com.example.spicep.service.TokenPriceUpdaterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@Slf4j
@RequiredArgsConstructor
public class TokenPriceUpdater {

    private final TokenPriceUpdaterService tokenPriceUpdaterService;

    @Scheduled(cron = "${job.retrieve-token-price.expression}")
    public void updateTokenPrices() {
        log.info("Running job update token price");
        var tokens = TokenSymbol.values();
        Arrays.stream(tokens)
                .forEach(tokenPriceUpdaterService::updateToken);
    }
}
