package com.example.spicep.repository;

import com.example.spicep.entity.TokenAssetEntity;
import com.example.spicep.model.model.TokenSymbol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Transactional(propagation = Propagation.REQUIRED)
public interface TokenAssetRepository extends JpaRepository<TokenAssetEntity, UUID> {

    @Modifying
    @Query(value = "UPDATE token_asset SET price = :newPrice WHERE symbol = :#{#symbol.name()}", nativeQuery = true)
    void updatePrice(@Param("newPrice") BigDecimal newPrice, @Param("symbol") TokenSymbol tokenSymbol);
}
