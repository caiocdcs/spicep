package com.example.spicep.service;

import com.example.spicep.model.AssetEvaluation;
import com.example.spicep.model.TokenPrice;
import com.example.spicep.model.WalletEvaluationRequest;
import com.example.spicep.model.model.TokenSymbol;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WalletEvaluationServiceTest {

    @Mock
    private TokenPriceService tokenPriceService;

    @InjectMocks
    private WalletEvaluationService walletEvaluationService;

    @Test
    public void testEvaluationAppreciation() {
        // BTC: value = 50, qty = 0.5, price = 100
        // Historical price: 102, performance: 2%, Total: 51
        var evaluationDate = LocalDate.now();
        var assetEvaluation = new AssetEvaluation(TokenSymbol.BTC, BigDecimal.valueOf(50), BigDecimal.valueOf(0.5));
        var walletEvaluationRequest = new WalletEvaluationRequest(evaluationDate, List.of(assetEvaluation));

        var historicalTokenPrice = new TokenPrice(TokenSymbol.BTC, BigDecimal.valueOf(102));

        when(tokenPriceService.getHistoricalTokenPrice(TokenSymbol.BTC, evaluationDate))
                .thenReturn(Optional.of(historicalTokenPrice));

        var walletEvaluation = walletEvaluationService.evaluate(walletEvaluationRequest);
        assertThat(walletEvaluation).isNotNull();
        assertThat(walletEvaluation.bestAsset()).isEqualTo(TokenSymbol.BTC);
        assertThat(walletEvaluation.bestPerformance()).isEqualTo(2);
        assertThat(walletEvaluation.total().setScale(2, RoundingMode.HALF_UP))
                .isEqualTo(BigDecimal.valueOf(51).setScale(2, RoundingMode.HALF_UP));

        verify(tokenPriceService, times(1))
                .getHistoricalTokenPrice(TokenSymbol.BTC, evaluationDate);
    }

    @Test
    public void testEvaluationDepreciation() {
        // BTC: value = 50, qty = 0.5, price = 100
        // Historical price: 95, performance: -5%, Total: 47.5
        var evaluationDate = LocalDate.now();
        var assetEvaluation = new AssetEvaluation(TokenSymbol.BTC, BigDecimal.valueOf(50), BigDecimal.valueOf(0.5));
        var walletEvaluationRequest = new WalletEvaluationRequest(evaluationDate, List.of(assetEvaluation));

        var historicalTokenPrice = new TokenPrice(TokenSymbol.BTC, BigDecimal.valueOf(95));

        when(tokenPriceService.getHistoricalTokenPrice(TokenSymbol.BTC, evaluationDate))
                .thenReturn(Optional.of(historicalTokenPrice));

        var walletEvaluation = walletEvaluationService.evaluate(walletEvaluationRequest);
        assertThat(walletEvaluation).isNotNull();
        assertThat(walletEvaluation.worstAsset()).isEqualTo(TokenSymbol.BTC);
        assertThat(walletEvaluation.worstPerformance()).isEqualTo(-5);
        assertThat(walletEvaluation.total().setScale(2, RoundingMode.HALF_UP))
                .isEqualTo(BigDecimal.valueOf(47.5).setScale(2, RoundingMode.HALF_UP));

        verify(tokenPriceService, times(1))
                .getHistoricalTokenPrice(TokenSymbol.BTC, evaluationDate);
    }

    @Test
    public void testEvaluationTwoTokens() {
        // BTC: value = 50, qty = 0.5, price = 100
        // Historical price: 102, performance: 2%, Total: 51
        // ETH: value = 50, qty = 0.5, price = 100
        // Historical price: 102, performance: -5%, Total: 47.5
        // Total: 98.5
        var evaluationDate = LocalDate.now();
        var assetEvaluationBTC = new AssetEvaluation(TokenSymbol.BTC, BigDecimal.valueOf(50), BigDecimal.valueOf(0.5));
        var assetEvaluationETH = new AssetEvaluation(TokenSymbol.ETH, BigDecimal.valueOf(50), BigDecimal.valueOf(0.5));
        var walletEvaluationRequest = new WalletEvaluationRequest(evaluationDate, List.of(assetEvaluationBTC, assetEvaluationETH));

        var historicalTokenPriceBTC = new TokenPrice(TokenSymbol.BTC, BigDecimal.valueOf(102));
        var historicalTokenPriceETH = new TokenPrice(TokenSymbol.BTC, BigDecimal.valueOf(95));

        when(tokenPriceService.getHistoricalTokenPrice(TokenSymbol.BTC, evaluationDate))
                .thenReturn(Optional.of(historicalTokenPriceBTC));
        when(tokenPriceService.getHistoricalTokenPrice(TokenSymbol.ETH, evaluationDate))
                .thenReturn(Optional.of(historicalTokenPriceETH));

        var walletEvaluation = walletEvaluationService.evaluate(walletEvaluationRequest);
        assertThat(walletEvaluation).isNotNull();
        assertThat(walletEvaluation.bestAsset()).isEqualTo(TokenSymbol.BTC);
        assertThat(walletEvaluation.bestPerformance()).isEqualTo(2);
        assertThat(walletEvaluation.worstAsset()).isEqualTo(TokenSymbol.ETH);
        assertThat(walletEvaluation.worstPerformance()).isEqualTo(-5);
        assertThat(walletEvaluation.total().setScale(2, RoundingMode.HALF_UP))
                .isEqualTo(BigDecimal.valueOf(98.5).setScale(2, RoundingMode.HALF_UP));

        verify(tokenPriceService, times(1))
                .getHistoricalTokenPrice(TokenSymbol.BTC, evaluationDate);
    }
}
