package com.example.controlplanebff.service;

import com.example.controlplanebff.client.ControlPlaneClient;
import com.example.controlplanebff.dto.domain.MarketSymbolDto;
import com.example.controlplanebff.dto.ui.MarketPairUiDto;
import com.example.controlplanebff.dto.ui.MarketsResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MarketsService {

    private final ControlPlaneClient controlPlaneClient;

    public MarketsResponse getMarkets(String assetType, String exchange, String marketType) {
        log.info("Fetching markets for assetType={}, exchange={}, marketType={}", assetType, exchange, marketType);

        List<MarketSymbolDto> symbols = controlPlaneClient.getMarketSymbols(
                exchange,
                marketType,
                assetType,
                null // enabled filter not applied
        );

        Set<String> baseAssets = symbols.stream()
                .map(MarketSymbolDto::getBaseAsset)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Set<String> quoteAssets = symbols.stream()
                .map(MarketSymbolDto::getQuoteAsset)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());


        List<MarketPairUiDto> pairs = symbols.stream()
                .map(symbol -> MarketPairUiDto.builder()
                        .symbol(symbol.getSymbol())
                        .baseAsset(symbol.getBaseAsset())
                        .quoteAsset(symbol.getQuoteAsset())
                        .build())
                .collect(Collectors.toList());

        log.info("Found {} pairs for assetType={}, exchange={}, marketType={}", 
                pairs.size(), assetType, exchange, marketType);

        return MarketsResponse.builder()
                .assetType(assetType)
                .exchange(exchange)
                .marketType(marketType)
                .baseAssets(baseAssets)
                .quoteAssets(quoteAssets)
                .pairs(pairs)
                .build();
    }
}


