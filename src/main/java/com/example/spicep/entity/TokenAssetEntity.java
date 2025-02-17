package com.example.spicep.entity;

import com.example.spicep.dto.TokenAssetDTO;
import com.example.spicep.model.model.TokenSymbol;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "token_asset",
        uniqueConstraints = @UniqueConstraint(columnNames = { "wallet", "symbol" }))
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenAssetEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "wallet_id", nullable = false)
    private WalletEntity wallet;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TokenSymbol symbol;

    @Column(nullable = false, precision = 20, scale = 2)
    private BigDecimal quantity;

    @Column(nullable = false, precision = 20, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    @CreationTimestamp
    private Instant createdAt;

    @Column(nullable = false)
    @UpdateTimestamp
    private Instant updatedAt;

    public static TokenAssetDTO toDTO(TokenAssetEntity entity) {
        return new TokenAssetDTO(
                entity.symbol,
                entity.quantity,
                entity.price,
                entity.quantity.multiply(entity.price));
    }
}
