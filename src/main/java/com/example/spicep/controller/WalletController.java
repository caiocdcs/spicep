package com.example.spicep.controller;

import com.example.spicep.dto.WalletDTO;
import com.example.spicep.model.Asset;
import com.example.spicep.model.Wallet;
import com.example.spicep.service.WalletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/wallets")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;

    @PostMapping
    public ResponseEntity<WalletDTO> createNewWallet(@Valid @RequestBody Wallet wallet) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(walletService.createWallet(wallet));
    }

    @GetMapping("/{walletId}")
    public ResponseEntity<WalletDTO> getWallet(@PathVariable UUID walletId) {
        return ResponseEntity.of(walletService.getWallet(walletId));
    }

    @PostMapping("/{walletId}/assets")
    public ResponseEntity<Void> addAsset(@PathVariable UUID walletId,
            @Valid @RequestBody Asset asset) {
        walletService.addAsset(walletId, asset);
        return ResponseEntity
                .ok()
                .build();
    }
}
