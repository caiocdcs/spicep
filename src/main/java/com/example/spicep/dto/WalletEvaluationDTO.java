package com.example.spicep.dto;

import com.example.spicep.model.AssetPerformance;
import com.example.spicep.model.model.TokenSymbol;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record WalletEvaluationDTO(
        BigDecimal total,
        @JsonProperty("best_asset") TokenSymbol bestAsset,
        @JsonProperty("best_performance") Double bestPerformance,
        @JsonProperty("worst_asset") TokenSymbol worstAsset,
        @JsonProperty("worst_performance") Double worstPerformance
        ) {

        public static WalletEvaluationDTO of(
                BigDecimal total,
                AssetPerformance bestAsset,
                AssetPerformance worstAsset) {
                return new WalletEvaluationDTO(total, bestAsset.symbol(), bestAsset.performance(), worstAsset.symbol(), worstAsset.performance());
        }
}
