package com.example.spicep.service;

import com.example.spicep.model.TokenPrice;
import com.example.spicep.model.model.TokenSymbol;
import com.example.spicep.repository.TokenPriceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TokenPriceUpdaterServiceTest {

    @Mock
    private TokenPriceRepository tokenPriceRepository;

    @Mock
    private TokenPriceService tokenPriceService;

    @InjectMocks
    private TokenPriceUpdaterService tokenPriceUpdaterService;

    @Test
    public void testUpdateTokenPrice() {
        var symbol = "BTC";
        var tokenSymbol = TokenSymbol.BTC;
        var tokenPrice = new TokenPrice(symbol, BigDecimal.ONE);

        when(tokenPriceService.getTokenPrice(tokenSymbol)).thenReturn(Optional.of(tokenPrice));
        when(tokenPriceRepository.findByTokenSymbol(symbol)).thenReturn(Optional.empty());

        tokenPriceUpdaterService.updateToken(tokenSymbol);

        verify(tokenPriceService, times(1)).getTokenPrice(tokenSymbol);
    }

}
