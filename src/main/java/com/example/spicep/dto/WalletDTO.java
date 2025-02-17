package com.example.spicep.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record WalletDTO(UUID id, BigDecimal total, List<TokenAssetDTO> assets) {

}
