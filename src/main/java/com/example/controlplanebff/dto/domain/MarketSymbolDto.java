package com.example.controlplanebff.dto.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarketSymbolDto {
    private Long id;
    private String exchange;
    private String marketType;
    private String assetType;
    private String symbol;
    private String symbolRaw;
    private String baseAsset;
    private String quoteAsset;
    private Boolean enabled;
}



