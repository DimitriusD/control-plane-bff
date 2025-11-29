package com.example.controlplanebff.service;

import com.example.controlplanebff.client.ControlPlaneApiClient;
import com.example.controlplanebff.dto.DictionariesResponse;
import com.example.controlplanebff.dto.PageResponse;
import com.example.controlplanebff.dto.upstream.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DictionariesService {

    private final ControlPlaneApiClient apiClient;

    @Value("${spring.application.name:control-plane-bff}")
    private String applicationName;

    @Value("${app.environment:local}")
    private String environment;

    public DictionariesResponse getDictionaries() {
        log.info("Building dictionaries from control-plane-service");

        ItemsResponse<String> assetTypesResponse = apiClient.getAssetTypes();
        ItemsResponse<String> marketTypesResponse = apiClient.getMarketTypes();
        ItemsResponse<String> streamStatusesResponse = apiClient.getStreamStatuses();
        ItemsResponse<RegionDto> regionsResponse = apiClient.getRegions();
        ItemsResponse<WindowDto> windowsResponse = apiClient.getWindows();
        ItemsResponse<ForecastTargetDto> forecastTargetsResponse = apiClient.getForecastTargets();
        ItemsResponse<String> forecastHorizonsResponse = apiClient.getForecastHorizons();
        ItemsResponse<ModelTypeDto> modelTypesResponse = apiClient.getModelTypes();

        PageResponse<ExchangeApiDto> exchangesPage = apiClient.getExchanges(0, 1000);
        List<ExchangeApiDto> exchanges = exchangesPage.getItems() != null ? exchangesPage.getItems() : List.of();

        PageResponse<MarketSymbolApiDto> symbolsPage = apiClient.getMarketSymbols(
                null, null, null, null, null, 0, 10000);
        List<MarketSymbolApiDto> allSymbols = symbolsPage.getItems() != null ? symbolsPage.getItems() : List.of();

        Map<String, Set<String>> exchangeToAssetTypes = new HashMap<>();
        for (MarketSymbolApiDto symbol : allSymbols) {
            if (symbol.getExchangeCode() != null && symbol.getAssetType() != null) {
                exchangeToAssetTypes.computeIfAbsent(symbol.getExchangeCode(), k -> new HashSet<>())
                        .add(symbol.getAssetType());
            }
        }

        List<DictionariesResponse.ExchangeItem> exchangeItems = exchanges.stream()
                .map(exchange -> DictionariesResponse.ExchangeItem.builder()
                        .code(exchange.getCode())
                        .name(exchange.getName())
                        .assetTypes(new ArrayList<>(exchangeToAssetTypes.getOrDefault(
                                exchange.getCode(), new HashSet<>())))
                        .build())
                .collect(Collectors.toList());

        List<String> regions = regionsResponse.getItems() != null
                ? regionsResponse.getItems().stream()
                        .map(RegionDto::getCode)
                        .collect(Collectors.toList())
                : List.of();

        List<String> windows = windowsResponse.getItems() != null
                ? windowsResponse.getItems().stream()
                        .filter(w -> w.getEnabled() != null && w.getEnabled())
                        .map(WindowDto::getCode)
                        .collect(Collectors.toList())
                : List.of();

        List<String> forecastTargets = forecastTargetsResponse.getItems() != null
                ? forecastTargetsResponse.getItems().stream()
                        .map(ForecastTargetDto::getCode)
                        .collect(Collectors.toList())
                : List.of();

        List<String> modelTypes = modelTypesResponse.getItems() != null
                ? modelTypesResponse.getItems().stream()
                        .map(ModelTypeDto::getCode)
                        .collect(Collectors.toList())
                : List.of();

        return DictionariesResponse.builder()
                .environment(environment)
                .status("OK")
                .assetTypes(assetTypesResponse.getItems() != null ? assetTypesResponse.getItems() : List.of())
                .exchanges(exchangeItems)
                .marketTypes(marketTypesResponse.getItems() != null ? marketTypesResponse.getItems() : List.of())
                .regions(regions)
                .windows(windows)
                .streamStates(streamStatusesResponse.getItems() != null ? streamStatusesResponse.getItems() : List.of())
                .forecastTargets(forecastTargets)
                .forecastHorizons(forecastHorizonsResponse.getItems() != null ? forecastHorizonsResponse.getItems() : List.of())
                .modelTypes(modelTypes)
                .build();
    }
}

