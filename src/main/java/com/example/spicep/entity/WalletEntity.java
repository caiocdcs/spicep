package com.example.spicep.entity;

import com.example.spicep.dto.TokenAssetDTO;
import com.example.spicep.dto.WalletDTO;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "wallet", uniqueConstraints = @UniqueConstraint(columnNames = "userEmail"))
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WalletEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String userEmail;

    @Column(nullable = false)
    @CreationTimestamp
    private Instant createdAt;

    @OneToMany(mappedBy = "wallet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TokenAssetEntity> tokenAssets = new ArrayList<>();

    public static WalletDTO toDTO(WalletEntity entity) {
        var assets = entity.tokenAssets.stream()
                .map(TokenAssetEntity::toDTO).toList();

        var total = assets.stream()
                .map(TokenAssetDTO::value)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new WalletDTO(entity.getId(), total, assets);
    }
}
