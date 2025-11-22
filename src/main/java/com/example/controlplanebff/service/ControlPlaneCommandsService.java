package com.example.controlplanebff.service;

import com.example.controlplanebff.client.ControlPlaneClient;
import com.example.controlplanebff.dto.domain.*;
import com.example.controlplanebff.dto.ui.*;
import com.example.controlplanebff.mapper.CommandMapper;
import com.example.controlplanebff.mapper.ForecastConfigMapper;
import com.example.controlplanebff.mapper.StreamConfigMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ControlPlaneCommandsService {

    private final ControlPlaneClient controlPlaneClient;

    public SymbolStreamsState toggleMarketStream(ToggleMarketStreamRequest request) {
        log.info("Toggling market stream: {}", request);

        String symbol = resolveSymbol(request.getExchange(), request.getMarketType(), 
                request.getAssetType(), request.getBaseAsset(), request.getQuoteAsset());

        StreamConfigDto currentConfig = null;
        try {
            currentConfig = controlPlaneClient.getStreamConfig(
                    request.getExchange(),
                    request.getMarketType(),
                    symbol
            );
        } catch (Exception e) {
            log.warn("No existing stream config found, creating new one");
        }

        UpsertStreamConfigRequest upsertRequest = CommandMapper.toUpsertStreamConfigRequest(
                request.getExchange(),
                request.getMarketType(),
                symbol,
                request.getEnabled(),
                currentConfig != null ? currentConfig.getNewsStreamEnabled() : false
        );

        StreamConfigDto updatedConfig = controlPlaneClient.upsertStreamConfig(upsertRequest);

        return SymbolStreamsState.builder()
                .symbol(symbol)
                .baseAsset(request.getBaseAsset())
                .quoteAsset(request.getQuoteAsset())
                .marketStream(StreamConfigMapper.INSTANCE.toMarketStreamState(updatedConfig))
                .newsStream(StreamConfigMapper.INSTANCE.toNewsStreamState(updatedConfig))
                .build();
    }

    public SymbolStreamsState toggleNewsStream(ToggleNewsStreamRequest request) {
        log.info("Toggling news stream: {}", request);

        String symbol = resolveSymbol(request.getExchange(), request.getMarketType(), 
                request.getAssetType(), request.getBaseAsset(), request.getQuoteAsset());

        StreamConfigDto currentConfig = null;
        try {
            currentConfig = controlPlaneClient.getStreamConfig(
                    request.getExchange(),
                    request.getMarketType(),
                    symbol
            );
        } catch (Exception e) {
            log.warn("No existing stream config found, creating new one");
        }

        UpsertStreamConfigRequest upsertRequest = CommandMapper.toUpsertStreamConfigRequest(
                request.getExchange(),
                request.getMarketType(),
                symbol,
                currentConfig != null ? currentConfig.getMarketStreamEnabled() : false,
                request.getEnabled()
        );

        StreamConfigDto updatedConfig = controlPlaneClient.upsertStreamConfig(upsertRequest);

        return SymbolStreamsState.builder()
                .symbol(symbol)
                .baseAsset(request.getBaseAsset())
                .quoteAsset(request.getQuoteAsset())
                .marketStream(StreamConfigMapper.INSTANCE.toMarketStreamState(updatedConfig))
                .newsStream(StreamConfigMapper.INSTANCE.toNewsStreamState(updatedConfig))
                .build();
    }

    public ForecastingState upsertForecastConfig(UpsertForecastConfigUiRequest request) {
        log.info("Upserting forecast config: {}", request);

        String symbol = resolveSymbol(request.getExchange(), request.getMarketType(), 
                request.getAssetType(), request.getBaseAsset(), request.getQuoteAsset());

        UpsertForecastConfigRequest upsertRequest = CommandMapper.toUpsertForecastConfigRequest(request, symbol);

        ForecastConfigDto updatedConfig = controlPlaneClient.upsertForecastConfig(upsertRequest);

        return ForecastConfigMapper.INSTANCE.toForecastingState(updatedConfig);
    }

    public BackfillJobDto createBackfillJob(CreateBackfillJobUiRequest request) {
        log.info("Creating backfill job: {}", request);

        String symbol = resolveSymbol(request.getExchange(), request.getMarketType(), 
                request.getAssetType(), request.getBaseAsset(), request.getQuoteAsset());

        CreateBackfillJobRequest createRequest = CommandMapper.toCreateBackfillJobRequest(request, symbol);

        return controlPlaneClient.createBackfillJob(createRequest);
    }

    private String resolveSymbol(String exchange, String marketType, String assetType, 
                                 String baseAsset, String quoteAsset) {
        List<MarketSymbolDto> symbols = controlPlaneClient.getMarketSymbols(
                exchange, marketType, assetType, null
        );

        return symbols.stream()
                .filter(s -> baseAsset.equals(s.getBaseAsset()) && quoteAsset.equals(s.getQuoteAsset()))
                .findFirst()
                .map(MarketSymbolDto::getSymbol)
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("Symbol not found for exchange=%s, marketType=%s, baseAsset=%s, quoteAsset=%s",
                                exchange, marketType, baseAsset, quoteAsset)));
    }
}

