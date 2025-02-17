package com.example.spicep.service;

import com.example.spicep.dto.WalletDTO;
import com.example.spicep.entity.WalletEntity;
import com.example.spicep.exception.WalletAlreadyExistsException;
import com.example.spicep.model.Wallet;
import com.example.spicep.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;

    public WalletDTO createWallet(Wallet wallet) {
        var currentWallet = walletRepository.findByUserEmail(wallet.userEmail());

        if (currentWallet.isPresent()) {
            throw new WalletAlreadyExistsException("Email already has a wallet");
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
}
