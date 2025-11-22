package com.example.controlplanebff.service;

import com.example.controlplanebff.client.ControlPlaneClient;
import com.example.controlplanebff.dto.domain.ExchangeDto;
import com.example.controlplanebff.dto.domain.MarketSymbolDto;
import com.example.controlplanebff.dto.ui.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DictionariesService {

    private final ControlPlaneClient controlPlaneClient;

    private static final List<String> FORECAST_LANES = List.of("PRICE_ONLY", "NEWS_AWARE");
    private static final String DEFAULT_FORECAST_LANE = "PRICE_ONLY";
    
    private static final List<String> FORECAST_HORIZONS = List.of("5m", "15m", "1h", "1d");
    private static final Map<String, List<String>> DEFAULT_HORIZONS_BY_ASSET_TYPE = Map.of(
            "CRYPTO", List.of("5m", "15m", "1h"),
            "EQUITY", List.of("1h", "1d")
    );
    
    private static final List<String> BACKFILL_RESOLUTIONS = List.of("TICK", "SEC_1", "MIN_1", "MIN_5", "HOUR_1");
    private static final String DEFAULT_BACKFILL_RESOLUTION = "MIN_1";
    
    private static final String DEFAULT_ASSET_TYPE = "CRYPTO";
    private static final String DEFAULT_MARKET_TYPE = "SPOT";

    public DictionariesResponse getDictionaries() {
        log.info("Building dictionaries for UI");

        List<ExchangeDto> exchanges = controlPlaneClient.getExchanges();
        List<MarketSymbolDto> allSymbols = controlPlaneClient.getMarketSymbols(null, null, null, null);

        List<String> assetTypes = allSymbols.stream()
                .map(MarketSymbolDto::getAssetType)
                .filter(Objects::nonNull)
                .distinct()
                .sorted()
                .toList();

        List<ExchangeDictionaryItem> exchangeItems = new ArrayList<>();
        Map<String, Set<String>> exchangeToAssetTypes = new HashMap<>();
        Map<String, String> exchangeToDefaultMarketType = new HashMap<>();

        for (MarketSymbolDto symbol : allSymbols) {
            String exchangeCode = symbol.getExchange();
            if (exchangeCode == null) continue;

            exchangeToAssetTypes.computeIfAbsent(exchangeCode, k -> new HashSet<>())
                    .add(symbol.getAssetType());

            if (!exchangeToDefaultMarketType.containsKey(exchangeCode)) {
                exchangeToDefaultMarketType.put(exchangeCode, symbol.getMarketType());
            } else {
                // Prefer SPOT for CRYPTO if available
                if ("CRYPTO".equals(symbol.getAssetType()) && "SPOT".equals(symbol.getMarketType())) {
                    exchangeToDefaultMarketType.put(exchangeCode, "SPOT");
                }
            }
        }

        // Build exchange items
        for (ExchangeDto exchange : exchanges) {
            Set<String> supportedAssetTypes = exchangeToAssetTypes.getOrDefault(exchange.getCode(), new HashSet<>());
            String defaultMarketType = exchangeToDefaultMarketType.getOrDefault(exchange.getCode(), "SPOT");

            ExchangeDictionaryItem item = ExchangeDictionaryItem.builder()
                    .code(exchange.getCode())
                    .name(exchange.getName())
                    .supportedAssetTypes(new ArrayList<>(supportedAssetTypes))
                    .defaultMarketType(defaultMarketType)
                    .build();

            exchangeItems.add(item);
        }

        // Build market types from symbols
        List<String> marketTypes = allSymbols.stream()
                .map(MarketSymbolDto::getMarketType)
                .filter(Objects::nonNull).distinct().sorted().collect(Collectors.toList());

        // Determine default exchange (first CRYPTO exchange, or first exchange)
        String defaultExchange = exchangeItems.stream()
                .filter(e -> e.getSupportedAssetTypes().contains(DEFAULT_ASSET_TYPE))
                .map(ExchangeDictionaryItem::getCode)
                .findFirst()
                .orElse(exchangeItems.isEmpty() ? null : exchangeItems.getFirst().getCode());

        log.info("Built dictionaries - assetTypes: {}, exchanges: {}, defaultExchange: {}", 
                assetTypes.size(), exchangeItems.size(), defaultExchange);

        return DictionariesResponse.builder()
                .assetTypes(AssetTypesDictionary.builder()
                        .items(assetTypes)
                        .default_(DEFAULT_ASSET_TYPE)
                        .build())
                .exchanges(ExchangesDictionary.builder()
                        .items(exchangeItems)
                        .build())
                .marketTypes(MarketTypesDictionary.builder()
                        .items(marketTypes)
                        .build())
                .forecastLanes(ForecastLanesDictionary.builder()
                        .items(FORECAST_LANES)
                        .default_(DEFAULT_FORECAST_LANE)
                        .build())
                .forecastHorizons(ForecastHorizonsDictionary.builder()
                        .items(FORECAST_HORIZONS)
                        .defaultsByAssetType(DEFAULT_HORIZONS_BY_ASSET_TYPE)
                        .build())
                .backfillResolutions(BackfillResolutionsDictionary.builder()
                        .items(BACKFILL_RESOLUTIONS)
                        .default_(DEFAULT_BACKFILL_RESOLUTION)
                        .build())
                .defaults(DefaultsDictionary.builder()
                        .assetType(DEFAULT_ASSET_TYPE)
                        .exchange(defaultExchange)
                        .marketType(DEFAULT_MARKET_TYPE)
                        .build())
                .build();
    }
}


