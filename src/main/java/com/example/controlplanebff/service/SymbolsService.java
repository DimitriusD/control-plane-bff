package com.example.controlplanebff.service;

import com.example.controlplanebff.client.ControlPlaneApiClient;
import com.example.controlplanebff.dto.PageResponse;
import com.example.controlplanebff.dto.SymbolItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SymbolsService {

    private final ControlPlaneApiClient apiClient;

    public PageResponse<SymbolItem> searchSymbols(String exchangeCode, String marketType,
                                                   String search, String base,
                                                   Integer page, Integer size) {
        log.info("Searching symbols: exchange={}, marketType={}, search={}, base={}", 
                exchangeCode, marketType, search, base);
        return apiClient.suggestSymbols(exchangeCode, marketType, search, base, page, size);
    }
}

