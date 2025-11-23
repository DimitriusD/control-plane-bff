package com.example.controlplanebff.dto.upstream;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarketSymbolApiDto {
    private Long id;
    private String exchangeCode;
    private String marketType;
    private String assetType;
    private String symbol;
    private String symbolRaw;
    private String baseAsset;
    private String quoteAsset;
    private Boolean enabled;
    private Instant createdAt;
    private Instant updatedAt;
}

