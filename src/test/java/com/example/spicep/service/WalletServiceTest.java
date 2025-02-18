package com.example.spicep.service;

import com.example.spicep.entity.TokenAssetEntity;
import com.example.spicep.entity.WalletEntity;
import com.example.spicep.exception.BusinessException;
import com.example.spicep.model.Asset;
import com.example.spicep.model.TokenPrice;
import com.example.spicep.model.Wallet;
import com.example.spicep.model.model.ErrorCode;
import com.example.spicep.model.model.TokenSymbol;
import com.example.spicep.repository.TokenAssetRepository;
import com.example.spicep.repository.WalletRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WalletServiceTest {

    private static final String USER_EMAIL = "email@email.com";

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private TokenAssetRepository tokenAssetRepository;

    @Mock
    private TokenPriceService tokenPriceService;

    @InjectMocks
    private WalletService walletService;

    @Test
    public void testCreateNewWallet() {
        var wallet = new Wallet(USER_EMAIL);
        var walletEntity = new WalletEntity(UUID.randomUUID(), USER_EMAIL, Instant.now(), List.of());

        when(walletRepository.findByUserEmail(USER_EMAIL)).thenReturn(Optional.empty());
        when(walletRepository.save(any(WalletEntity.class))).thenReturn(walletEntity);

        var newWallet = walletService.createWallet(wallet);
        assertThat(newWallet).isNotNull();
        assertThat(newWallet.id()).isNotNull();

        verify(walletRepository, times(1)).findByUserEmail(USER_EMAIL);
        verify(walletRepository, times(1)).save(any(WalletEntity.class));
    }

    @Test
    public void testCreateExistentWalletThrowException() {
        var wallet = new Wallet(USER_EMAIL);
        var walletEntity = new WalletEntity(UUID.randomUUID(), USER_EMAIL, Instant.now(), List.of());

        when(walletRepository.findByUserEmail(USER_EMAIL)).thenReturn(Optional.of(walletEntity));

        var exception = catchThrowableOfType(() -> walletService.createWallet(wallet), BusinessException.class);
        assertThat(exception).isNotNull();
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.WALLET_ALREADY_EXISTS);

        verify(walletRepository, times(1)).findByUserEmail(USER_EMAIL);
        verify(walletRepository, times(0)).save(any(WalletEntity.class));
    }

    @Test
    public void testAddAssetToWallet() {
        var walletId = UUID.randomUUID();
        var walletEntity = new WalletEntity(walletId, USER_EMAIL, Instant.now(), List.of());
        var tokenPrice = new TokenPrice(TokenSymbol.BTC, BigDecimal.ONE);

        var asset = new Asset(TokenSymbol.BTC, BigDecimal.ONE, BigDecimal.ONE);

        when(tokenPriceService.getTokenPrice(TokenSymbol.BTC)).thenReturn(Optional.of(tokenPrice));
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(walletEntity));

        walletService.addAsset(walletId, asset);

        verify(tokenPriceService, times(1)).getTokenPrice(TokenSymbol.BTC);
        verify(walletRepository, times(1)).findById(walletId);
        verify(tokenAssetRepository, times(1)).save(any(TokenAssetEntity.class));
    }

    @Test
    public void testAddAssetToWalletNotFound() {
        var walletId = UUID.randomUUID();
        var tokenPrice = new TokenPrice(TokenSymbol.BTC, BigDecimal.ONE);

        var asset = new Asset(TokenSymbol.BTC, BigDecimal.ONE, BigDecimal.ONE);

        when(tokenPriceService.getTokenPrice(TokenSymbol.BTC)).thenReturn(Optional.of(tokenPrice));
        when(walletRepository.findById(walletId)).thenReturn(Optional.empty());

        var exception = catchThrowableOfType(() -> walletService.addAsset(walletId, asset), BusinessException.class);
        assertThat(exception).isNotNull();
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.WALLET_DOES_NOT_EXIST);

        verify(tokenPriceService, times(1)).getTokenPrice(TokenSymbol.BTC);
        verify(walletRepository, times(1)).findById(walletId);
        verify(tokenAssetRepository, times(0)).save(any(TokenAssetEntity.class));
    }

    @Test
    public void testAddAssetToWalletTokenPriceNotFound() {
        var walletId = UUID.randomUUID();
        var asset = new Asset(TokenSymbol.BTC, BigDecimal.ONE, BigDecimal.ONE);

        when(tokenPriceService.getTokenPrice(TokenSymbol.BTC)).thenReturn(Optional.empty());

        var exception = catchThrowableOfType(() -> walletService.addAsset(walletId, asset), BusinessException.class);
        assertThat(exception).isNotNull();
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.TOKEN_PRICE_DOES_NOT_EXIST);

        verify(tokenPriceService, times(1)).getTokenPrice(TokenSymbol.BTC);
        verify(walletRepository, times(0)).findById(walletId);
        verify(tokenAssetRepository, times(0)).save(any(TokenAssetEntity.class));
    }
}
