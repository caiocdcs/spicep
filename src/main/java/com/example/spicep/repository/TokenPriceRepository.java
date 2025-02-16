package com.example.spicep.repository;

import com.example.spicep.entity.TokenPriceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TokenPriceRepository extends JpaRepository<TokenPriceEntity, UUID> {
}
