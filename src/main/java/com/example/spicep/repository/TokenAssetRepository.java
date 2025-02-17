package com.example.spicep.repository;

import com.example.spicep.entity.TokenAssetEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TokenAssetRepository extends JpaRepository<TokenAssetEntity, UUID> {
}
