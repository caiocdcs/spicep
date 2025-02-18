package com.example.spicep.controller;

import com.example.spicep.dto.WalletEvaluationDTO;
import com.example.spicep.model.WalletEvaluationRequest;
import com.example.spicep.service.WalletEvaluationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wallets")
@RequiredArgsConstructor
public class WalletEvaluationController {

    private final WalletEvaluationService walletEvaluationService;

    @PostMapping("/evaluate")
    public ResponseEntity<WalletEvaluationDTO> evaluateWallet(
            @Valid @RequestBody WalletEvaluationRequest request) {
        return ResponseEntity
                .ok(walletEvaluationService.evaluate(request));
    }
}
