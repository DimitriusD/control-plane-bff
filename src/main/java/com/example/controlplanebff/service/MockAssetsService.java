package com.example.controlplanebff.service;

import com.example.controlplanebff.dto.AssetsResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class MockAssetsService {

    // Mock data: exchange + marketType -> base assets
    private static final Map<String, List<String>> BASE_ASSETS = Map.of(
            "BINANCE_SPOT", List.of("BTC", "ETH", "XRP", "SOL", "BNB", "ADA", "DOGE", "DOT"),
            "BINANCE_FUTURES_PERP", List.of("BTC", "ETH", "SOL", "BNB"),
            "BYBIT_SPOT", List.of("BTC", "ETH", "XRP", "SOL"),
            "BYBIT_FUTURES_PERP", List.of("BTC", "ETH", "SOL"),
            "CME_FUTURES_DELIVERY", List.of("BTC", "ETH")
    );

    // Mock data: exchange + marketType + baseAsset -> quote assets
    private static final Map<String, List<String>> QUOTE_ASSETS = Map.of(
            "BINANCE_SPOT_BTC", List.of("USDT", "FDUSD", "TRY", "BRL", "EUR", "GBP"),
            "BINANCE_SPOT_ETH", List.of("USDT", "FDUSD", "BTC", "EUR"),
            "BINANCE_SPOT_SOL", List.of("USDT", "FDUSD", "BTC"),
            "BYBIT_SPOT_BTC", List.of("USDT", "USDC", "EUR"),
            "BYBIT_SPOT_ETH", List.of("USDT", "USDC")
    );

    public AssetsResponse getBaseAssets(String exchangeCode, String marketType) {
        log.info("Fetching base assets for exchange={}, marketType={}", exchangeCode, marketType);
        
        String key = exchangeCode + "_" + marketType;
        List<String> assets = BASE_ASSETS.getOrDefault(key, List.of("BTC", "ETH"));
        
        return AssetsResponse.builder()
                .items(assets)
                .build();
    }

    public AssetsResponse getQuoteAssets(String exchangeCode, String marketType, String baseAssetCode) {
        log.info("Fetching quote assets for exchange={}, marketType={}, baseAsset={}", 
                exchangeCode, marketType, baseAssetCode);
        
        String key = exchangeCode + "_" + marketType + "_" + baseAssetCode;
        List<String> quotes = QUOTE_ASSETS.getOrDefault(key, List.of("USDT", "USDC"));
        
        return AssetsResponse.builder()
                .items(quotes)
                .build();
    }
}

