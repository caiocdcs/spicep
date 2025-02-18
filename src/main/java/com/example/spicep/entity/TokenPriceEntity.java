package com.example.spicep.entity;

import com.example.spicep.model.model.TokenSymbol;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "token_price", indexes = { @Index(columnList = "tokenSymbol"), @Index(columnList = "timestamp")})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenPriceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private TokenSymbol tokenSymbol;

    @Column(nullable = false, precision = 20, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    @UpdateTimestamp
    private Instant timestamp;
}
