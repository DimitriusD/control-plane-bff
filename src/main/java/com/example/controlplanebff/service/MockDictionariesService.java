package com.example.controlplanebff.service;

import com.example.controlplanebff.dto.DictionariesResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class MockDictionariesService {

    public DictionariesResponse getDictionaries() {
        log.info("Building dictionaries for API v1");

        return DictionariesResponse.builder()
                .environment("local")
                .status("OK")
                .assetTypes(List.of("CRYPTO", "EQUITY", "FUTURES"))
                .exchanges(List.of(
                        DictionariesResponse.ExchangeItem.builder()
                                .code("BINANCE")
                                .name("Binance")
                                .assetTypes(List.of("CRYPTO"))
                                .build(),
                        DictionariesResponse.ExchangeItem.builder()
                                .code("BYBIT")
                                .name("Bybit")
                                .assetTypes(List.of("CRYPTO"))
                                .build(),
                        DictionariesResponse.ExchangeItem.builder()
                                .code("CME")
                                .name("CME")
                                .assetTypes(List.of("INDEX", "FX"))
                                .build()
                ))
                .marketTypes(List.of("SPOT", "FUTURES_PERP", "FUTURES_DELIVERY"))
                .regions(List.of("EU", "US", "ASIA"))
                .windows(List.of("1s", "5s", "1m"))
                .streamStates(List.of("RUNNING", "STOPPED", "FAILED", "STARTING", "STOPPING"))
                .forecastTargets(List.of("REALIZED_VOL", "IMPLIED_VOL"))
                .forecastHorizons(List.of("1m", "5m", "1h", "1d"))
                .modelTypes(List.of("ARIMA", "GARCH", "LSTM", "TRANSFORMER", "HYBRID_NEWS"))
                .build();
    }
}

