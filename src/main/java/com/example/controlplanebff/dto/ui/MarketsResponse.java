package com.example.controlplanebff.dto.ui;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarketsResponse {
    private String assetType;
    private String exchange;
    private String marketType;
    private Set<String> baseAssets;
    private Set<String> quoteAssets;
    private List<MarketPairUiDto> pairs;
}


