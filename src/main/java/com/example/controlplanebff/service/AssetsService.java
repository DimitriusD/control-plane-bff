package com.example.controlplanebff.service;

import com.example.controlplanebff.client.ControlPlaneApiClient;
import com.example.controlplanebff.dto.AssetsResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AssetsService {

    private final ControlPlaneApiClient apiClient;

    public AssetsResponse getBaseAssets(String exchangeCode, String marketType) {
        log.info("Fetching base assets for exchange={}, marketType={}", exchangeCode, marketType);
        var response = apiClient.getBaseAssets(exchangeCode, marketType);
        return AssetsResponse.builder()
                .items(response.getItems() != null ? response.getItems() : List.of())
                .build();
    }

    public AssetsResponse getQuoteAssets(String exchangeCode, String marketType, String baseAssetCode) {
        log.info("Fetching quote assets for exchange={}, marketType={}, baseAsset={}", 
                exchangeCode, marketType, baseAssetCode);
        var response = apiClient.getQuoteAssets(exchangeCode, marketType, baseAssetCode);
        return AssetsResponse.builder()
                .items(response.getItems() != null ? response.getItems() : List.of())
                .build();
    }
}

