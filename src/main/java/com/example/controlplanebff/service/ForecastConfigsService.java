package com.example.controlplanebff.service;

import com.example.controlplanebff.client.ControlPlaneApiClient;
import com.example.controlplanebff.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ForecastConfigsService {

    private final ControlPlaneApiClient apiClient;

    public PageResponse<ForecastConfigDto> listConfigs(String exchange, String marketType, String assetType,
                                                       String symbol, String horizon, Boolean enabled,
                                                       String search, Integer page, Integer size) {
        log.info("Listing forecast configs: exchange={}, marketType={}, assetType={}, symbol={}, horizon={}, enabled={}", 
                exchange, marketType, assetType, symbol, horizon, enabled);
        return apiClient.getForecastConfigs(exchange, marketType, assetType, symbol, horizon, enabled, search, page, size);
    }

    public ForecastConfigDto getConfig(String configId) {
        log.info("Getting forecast config: {}", configId);
        return apiClient.getForecastConfig(configId);
    }

    public ForecastConfigDto createConfig(CreateForecastConfigRequest request) {
        log.info("Creating forecast config: name={}, symbol={}", request.getName(), request.getSymbol());
        return apiClient.createForecastConfig(request);
    }

    public ForecastConfigDto updateConfig(String configId, UpdateForecastConfigRequest request) {
        log.info("Updating forecast config: configId={}", configId);
        return apiClient.updateForecastConfig(configId, request);
    }

    public ForecastConfigDto partialUpdateConfig(String configId, PartialUpdateForecastConfigRequest request) {
        log.info("Partially updating forecast config: configId={}, enabled={}", configId, request.getEnabled());
        return apiClient.patchForecastConfig(configId, request);
    }
}

