package com.example.controlplanebff.service;

import com.example.controlplanebff.client.ControlPlaneClient;
import com.example.controlplanebff.dto.domain.*;
import com.example.controlplanebff.dto.ui.*;
import com.example.controlplanebff.mapper.BackfillJobMapper;
import com.example.controlplanebff.mapper.ForecastConfigMapper;
import com.example.controlplanebff.mapper.StreamConfigMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ControlPlaneStreamsService {

    private final ControlPlaneClient controlPlaneClient;

    public ControlPlaneStreamsStateResponse getStreamsState(ControlPlaneStreamsStateQuery query) {
        log.info("Fetching streams state for query: assetType={}, exchange={}, marketType={}", 
                query.getAssetType(), query.getExchange(), query.getMarketType());

        List<ExchangeDto> exchanges = controlPlaneClient.getExchanges();
        Map<String, ExchangeDto> exchangeMap = exchanges.stream()
                .collect(Collectors.toMap(ExchangeDto::getCode, e -> e));

        List<MarketSymbolDto> symbols = controlPlaneClient.getMarketSymbols(
                query.getExchange(),
                query.getMarketType(),
                query.getAssetType(),
                null
        );

        List<MarketSymbolDto> filteredSymbols = symbols.stream()
                .filter(s -> query.getAssetType().equals(s.getAssetType()))
                .filter(s -> query.getExchange().equals(s.getExchange()))
                .filter(s -> query.getMarketType().equals(s.getMarketType()))
                .toList();

        List<ExchangeStreamsState> exchangeStates = new ArrayList<>();

        if (!filteredSymbols.isEmpty()) {
            ExchangeDto exchange = exchangeMap.get(query.getExchange());
            if (exchange == null) {
                exchange = ExchangeDto.builder()
                        .code(query.getExchange())
                        .name(query.getExchange())
                        .build();
            }

            List<MarketSymbolDto> marketSymbols = filteredSymbols;
            List<SymbolStreamsState> pairs = new ArrayList<>();
                Set<String> baseAssets = new HashSet<>();
                Set<String> quoteAssets = new HashSet<>();

                for (MarketSymbolDto symbol : marketSymbols) {
                    baseAssets.add(symbol.getBaseAsset());
                    quoteAssets.add(symbol.getQuoteAsset());

                    StreamConfigDto streamConfig = null;
                    try {
                        streamConfig = controlPlaneClient.getStreamConfig(
                                symbol.getExchange(),
                                symbol.getMarketType(),
                                symbol.getSymbol()
                        );
                    } catch (Exception e) {
                        log.warn("Failed to fetch stream config for {}/{}/{}: {}", 
                                symbol.getExchange(), symbol.getMarketType(), symbol.getSymbol(), e.getMessage());
                    }

                    ForecastConfigDto forecastConfig = null;
                    try {
                        forecastConfig = controlPlaneClient.getForecastConfig(
                                symbol.getExchange(),
                                symbol.getMarketType(),
                                symbol.getSymbol()
                        );
                    } catch (Exception e) {
                        log.warn("Failed to fetch forecast config for {}/{}/{}: {}", 
                                symbol.getExchange(), symbol.getMarketType(), symbol.getSymbol(), e.getMessage());
                    }

                    BackfillState backfillState = null;
                    try {
                        PageDto<BackfillJobDto> backfillJobs = controlPlaneClient.getBackfillJobs(
                                symbol.getExchange(),
                                symbol.getMarketType(),
                                symbol.getSymbol(),
                                0,
                                1
                        );
                        if (backfillJobs.getContent() != null && !backfillJobs.getContent().isEmpty()) {
                            BackfillJobDto lastJob = backfillJobs.getContent().get(0);
                            backfillState = BackfillJobMapper.INSTANCE.toBackfillState(lastJob);
                        }
                    } catch (Exception e) {
                        log.warn("Failed to fetch backfill jobs for {}/{}/{}: {}", 
                                symbol.getExchange(), symbol.getMarketType(), symbol.getSymbol(), e.getMessage());
                    }

                    SymbolStreamsState symbolState = SymbolStreamsState.builder()
                            .symbol(symbol.getSymbol())
                            .baseAsset(symbol.getBaseAsset())
                            .quoteAsset(symbol.getQuoteAsset())
                            .marketStream(streamConfig != null 
                                    ? StreamConfigMapper.INSTANCE.toMarketStreamState(streamConfig)
                                    : MarketStreamState.builder().enabled(false).status("UNKNOWN").build())
                            .newsStream(streamConfig != null 
                                    ? StreamConfigMapper.INSTANCE.toNewsStreamState(streamConfig)
                                    : NewsStreamState.builder().enabled(false).status("UNKNOWN").build())
                            .forecasting(forecastConfig != null 
                                    ? ForecastConfigMapper.INSTANCE.toForecastingState(forecastConfig)
                                    : ForecastingState.builder().enabled(false).build())
                            .backfill(backfillState)
                            .build();

                    pairs.add(symbolState);
                }

            ExchangeStreamsState exchangeState = ExchangeStreamsState.builder()
                    .code(exchange.getCode())
                    .name(exchange.getName())
                    .marketType(query.getMarketType())
                    .baseAssets(baseAssets)
                    .quoteAssets(quoteAssets)
                    .pairs(pairs)
                    .build();

            exchangeStates.add(exchangeState);
        }

        return ControlPlaneStreamsStateResponse.builder()
                .assetType(query.getAssetType())
                .exchanges(exchangeStates)
                .build();
    }
}

