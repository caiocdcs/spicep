package com.example.spicep.service;

import com.example.spicep.entity.WalletEntity;
import com.example.spicep.exception.WalletAlreadyExistsException;
import com.example.spicep.model.Wallet;
import com.example.spicep.repository.WalletRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
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

    @InjectMocks
    private WalletService walletService;

    @Test
    public void testCreateNewWallet() {
        var wallet = new Wallet(USER_EMAIL);
        var walletEntity = new WalletEntity(UUID.randomUUID(), USER_EMAIL, Instant.now());

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
        var walletEntity = new WalletEntity(UUID.randomUUID(), USER_EMAIL, Instant.now());

        when(walletRepository.findByUserEmail(USER_EMAIL)).thenReturn(Optional.of(walletEntity));

        var exception = catchThrowableOfType(() -> walletService.createWallet(wallet), WalletAlreadyExistsException.class);
        assertThat(exception).isNotNull();

        verify(walletRepository, times(1)).findByUserEmail(USER_EMAIL);
        verify(walletRepository, times(0)).save(any(WalletEntity.class));
    }
}
