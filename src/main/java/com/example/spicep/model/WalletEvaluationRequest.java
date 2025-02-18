package com.example.spicep.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDate;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record WalletEvaluationRequest(
        @JsonProperty("evaluation_date") LocalDate evaluationDate,
        @NotEmpty List<AssetEvaluation> assets) {
}
