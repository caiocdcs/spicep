package com.example.spicep.repository;

import com.example.spicep.entity.TokenPriceEntity;
import com.example.spicep.model.model.TokenSymbol;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TokenPriceRepository extends JpaRepository<TokenPriceEntity, UUID> {
    Optional<TokenPriceEntity> findByTokenSymbol(TokenSymbol tokenSymbol);
}
