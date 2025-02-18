package com.example.spicep.service;

import com.example.spicep.dto.WalletEvaluationDTO;
import com.example.spicep.model.AssetEvaluation;
import com.example.spicep.model.AssetPerformance;
import com.example.spicep.model.WalletEvaluationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WalletEvaluationService {

    private final TokenPriceService tokenPriceService;

    public WalletEvaluationDTO evaluate(WalletEvaluationRequest request) {
        final var dateToEvaluate = request.evaluationDate() != null ? request.evaluationDate() : LocalDate.now();

        var assetsPerformance = request.assets().stream()
                .map(asset -> evaluateAssetPerformance(asset, dateToEvaluate))
                .flatMap(Optional::stream)
                .toList();

        var total = assetsPerformance.stream().map(AssetPerformance::total).reduce(BigDecimal.ZERO, BigDecimal::add);
        var bestAsset = assetsPerformance.stream().max(Comparator.comparing(AssetPerformance::performance)).orElseThrow();
        var worstAsset = assetsPerformance.stream().min(Comparator.comparing(AssetPerformance::performance)).orElseThrow();

        return WalletEvaluationDTO.of(total, bestAsset, worstAsset);
    }

    private Optional<AssetPerformance> evaluateAssetPerformance(AssetEvaluation asset, LocalDate dateToEvaluate) {
        var tokenHistory = tokenPriceService.getHistoricalTokenPrice(asset.symbol(), dateToEvaluate);

        if (tokenHistory.isEmpty()) {
            return Optional.empty();
        }

        var historicalPrice = tokenHistory.get().price();
        var total = asset.quantity().multiply(historicalPrice).setScale(2, RoundingMode.HALF_UP);

        var currentPrice = asset.value().divide(asset.quantity(), RoundingMode.HALF_UP);

        var performance = historicalPrice.subtract(currentPrice)
                .divide(historicalPrice, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));

        var assetPerformance = new AssetPerformance(asset.symbol(), total, performance.doubleValue());
        return Optional.of(assetPerformance);
    }
}
