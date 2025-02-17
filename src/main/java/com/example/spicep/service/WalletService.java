package com.example.spicep.service;

import com.example.spicep.dto.WalletDTO;
import com.example.spicep.entity.TokenAssetEntity;
import com.example.spicep.entity.WalletEntity;
import com.example.spicep.exception.BusinessException;
import com.example.spicep.model.Asset;
import com.example.spicep.model.model.ErrorCode;
import com.example.spicep.model.Wallet;
import com.example.spicep.repository.TokenAssetRepository;
import com.example.spicep.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;
    private final TokenAssetRepository tokenAssetRepository;
    private final TokenPriceService tokenPriceService;

    public WalletDTO createWallet(Wallet wallet) {
        var currentWallet = walletRepository.findByUserEmail(wallet.userEmail());

        if (currentWallet.isPresent()) {
            throw new BusinessException(HttpStatus.CONFLICT, ErrorCode.WALLET_ALREADY_EXISTS);
        }

        var newWallet = createNewWallet(wallet);
        return WalletEntity.toDTO(newWallet);
    }

    private WalletEntity createNewWallet(Wallet wallet) {
        var walletEntity = new WalletEntity();
        walletEntity.setUserEmail(wallet.userEmail());

        return walletRepository.save(walletEntity);
    }

    public Optional<WalletDTO> getWallet(UUID walletId) {
        return walletRepository.findById(walletId).map(WalletEntity::toDTO);
    }

    public void addAsset(UUID walletId, Asset asset) {
        var tokenPrice = tokenPriceService.getTokenPrice(asset.symbol());
        if (tokenPrice.isEmpty()) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, ErrorCode.TOKEN_PRICE_DOES_NOT_EXIST);
        }

        var wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, ErrorCode.WALLET_DOES_NOT_EXIST));

        insertAsset(wallet, asset);
    }

    private void insertAsset(WalletEntity wallet, Asset asset) {
        var assetEntity = new TokenAssetEntity();
        assetEntity.setPrice(asset.price());
        assetEntity.setQuantity(asset.quantity());
        assetEntity.setSymbol(asset.symbol());
        assetEntity.setWallet(wallet);
        tokenAssetRepository.save(assetEntity);
    }
}
