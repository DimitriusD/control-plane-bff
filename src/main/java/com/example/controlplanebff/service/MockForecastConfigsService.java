package com.example.controlplanebff.service;

import com.example.controlplanebff.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MockForecastConfigsService {

    private final Map<String, ForecastConfigDto> configs = new ConcurrentHashMap<>();

    public MockForecastConfigsService() {
        initializeMockData();
    }

    private void initializeMockData() {
        
        ForecastConfigDto config1 = ForecastConfigDto.builder()
                .id("fc-binance-btcusdt-5m")
                .name("BTCUSDT 5m RV hybrid")
                .exchange("BINANCE")
                .assetType("CRYPTO")
                .marketType("SPOT")
                .symbol("BTCUSDT")
                .horizon("5m")
                .targets(List.of("REALIZED_VOL"))
                .modelTypes(List.of("GARCH", "LSTM", "HYBRID_NEWS"))
                .enabled(true)
                .createdAt(Instant.parse("2024-01-10T10:00:00Z"))
                .updatedAt(Instant.parse("2024-01-15T12:00:00Z"))
                .build();

        ForecastConfigDto config2 = ForecastConfigDto.builder()
                .id("fc-bybit-ethusdt-1h")
                .name("ETHUSDT 1h RV")
                .exchange("BYBIT")
                .assetType("CRYPTO")
                .marketType("SPOT")
                .symbol("ETHUSDT")
                .horizon("1h")
                .targets(List.of("REALIZED_VOL"))
                .modelTypes(List.of("GARCH", "ARIMA"))
                .enabled(true)
                .createdAt(Instant.parse("2024-01-12T10:00:00Z"))
                .updatedAt(Instant.parse("2024-01-12T10:00:00Z"))
                .build();

        ForecastConfigDto config3 = ForecastConfigDto.builder()
                .id("fc-binance-solusdt-15m")
                .name("SOLUSDT 15m IV")
                .exchange("BINANCE")
                .assetType("CRYPTO")
                .marketType("SPOT")
                .symbol("SOLUSDT")
                .horizon("15m")
                .targets(List.of("IMPLIED_VOL"))
                .modelTypes(List.of("TRANSFORMER"))
                .enabled(false)
                .createdAt(Instant.parse("2024-01-14T10:00:00Z"))
                .updatedAt(Instant.parse("2024-01-16T14:00:00Z"))
                .build();

        configs.put(config1.getId(), config1);
        configs.put(config2.getId(), config2);
        configs.put(config3.getId(), config3);
    }

    public PageResponse<ForecastConfigDto> listConfigs(String exchange, String marketType, String assetType,
                                                       String symbol, String horizon, Boolean enabled,
                                                       String search, Integer page, Integer size) {
        log.info("Listing forecast configs: exchange={}, marketType={}, assetType={}, symbol={}, horizon={}, enabled={}", 
                exchange, marketType, assetType, symbol, horizon, enabled);

        List<ForecastConfigDto> filtered = new ArrayList<>(configs.values());

        if (exchange != null && !exchange.isBlank()) {
            filtered = filtered.stream()
                    .filter(c -> exchange.equals(c.getExchange()))
                    .collect(Collectors.toList());
        }

        if (marketType != null && !marketType.isBlank()) {
            filtered = filtered.stream()
                    .filter(c -> marketType.equals(c.getMarketType()))
                    .collect(Collectors.toList());
        }

        if (assetType != null && !assetType.isBlank()) {
            filtered = filtered.stream()
                    .filter(c -> assetType.equals(c.getAssetType()))
                    .collect(Collectors.toList());
        }

        if (symbol != null && !symbol.isBlank()) {
            filtered = filtered.stream()
                    .filter(c -> symbol.equals(c.getSymbol()))
                    .collect(Collectors.toList());
        }

        if (horizon != null && !horizon.isBlank()) {
            filtered = filtered.stream()
                    .filter(c -> horizon.equals(c.getHorizon()))
                    .collect(Collectors.toList());
        }

        if (enabled != null) {
            filtered = filtered.stream()
                    .filter(c -> enabled.equals(c.getEnabled()))
                    .collect(Collectors.toList());
        }

        if (search != null && !search.isBlank()) {
            String searchLower = search.toLowerCase();
            filtered = filtered.stream()
                    .filter(c -> (c.getName() != null && c.getName().toLowerCase().contains(searchLower)) ||
                                 (c.getSymbol() != null && c.getSymbol().toLowerCase().contains(searchLower)) ||
                                 (c.getId() != null && c.getId().toLowerCase().contains(searchLower)))
                    .collect(Collectors.toList());
        }

        // Pagination
        int pageNum = page != null && page >= 0 ? page : 0;
        int sizeNum = size != null && size > 0 ? size : 20;
        int start = pageNum * sizeNum;
        int end = Math.min(start + sizeNum, filtered.size());

        List<ForecastConfigDto> items = start < filtered.size() 
                ? filtered.subList(start, end) 
                : List.of();

        return PageResponse.<ForecastConfigDto>builder()
                .items(items)
                .page(pageNum)
                .size(sizeNum)
                .total((long) filtered.size())
                .build();
    }

    public ForecastConfigDto getConfig(String configId) {
        log.info("Getting forecast config: {}", configId);
        return configs.get(configId);
    }

    public ForecastConfigDto createConfig(CreateForecastConfigRequest request) {
        log.info("Creating forecast config: name={}, symbol={}", request.getName(), request.getSymbol());

        String id = String.format("fc-%s-%s-%s", 
                request.getExchange().toLowerCase(),
                request.getSymbol().toLowerCase(),
                request.getHorizon().toLowerCase());

        Instant now = Instant.now();

        ForecastConfigDto config = ForecastConfigDto.builder()
                .id(id)
                .name(request.getName())
                .exchange(request.getExchange())
                .assetType(request.getAssetType())
                .marketType(request.getMarketType())
                .symbol(request.getSymbol())
                .horizon(request.getHorizon())
                .targets(request.getTargets())
                .modelTypes(request.getModelTypes())
                .enabled(request.getEnabled() != null ? request.getEnabled() : true)
                .createdAt(now)
                .updatedAt(now)
                .build();

        configs.put(id, config);
        return config;
    }

    public ForecastConfigDto updateConfig(String configId, UpdateForecastConfigRequest request) {
        log.info("Updating forecast config: configId={}", configId);

        ForecastConfigDto existing = configs.get(configId);
        if (existing == null) {
            return null;
        }

        ForecastConfigDto updated = ForecastConfigDto.builder()
                .id(existing.getId())
                .name(request.getName())
                .exchange(request.getExchange())
                .assetType(request.getAssetType())
                .marketType(request.getMarketType())
                .symbol(request.getSymbol())
                .horizon(request.getHorizon())
                .targets(request.getTargets())
                .modelTypes(request.getModelTypes())
                .enabled(request.getEnabled())
                .createdAt(existing.getCreatedAt())
                .updatedAt(Instant.now())
                .build();

        configs.put(configId, updated);
        return updated;
    }

    public ForecastConfigDto partialUpdateConfig(String configId, PartialUpdateForecastConfigRequest request) {
        log.info("Partially updating forecast config: configId={}, enabled={}", configId, request.getEnabled());

        ForecastConfigDto existing = configs.get(configId);
        if (existing == null) {
            return null;
        }

        ForecastConfigDto updated = ForecastConfigDto.builder()
                .id(existing.getId())
                .name(existing.getName())
                .exchange(existing.getExchange())
                .assetType(existing.getAssetType())
                .marketType(existing.getMarketType())
                .symbol(existing.getSymbol())
                .horizon(existing.getHorizon())
                .targets(existing.getTargets())
                .modelTypes(existing.getModelTypes())
                .enabled(request.getEnabled())
                .createdAt(existing.getCreatedAt())
                .updatedAt(Instant.now())
                .build();

        configs.put(configId, updated);
        return updated;
    }
}

