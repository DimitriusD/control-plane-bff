package com.example.controlplanebff.mapper;

import com.example.controlplanebff.dto.domain.CreateBackfillJobRequest;
import com.example.controlplanebff.dto.domain.UpsertForecastConfigRequest;
import com.example.controlplanebff.dto.domain.UpsertStreamConfigRequest;
import com.example.controlplanebff.dto.ui.CreateBackfillJobUiRequest;
import com.example.controlplanebff.dto.ui.UpsertForecastConfigUiRequest;

public class CommandMapper {

    public static UpsertStreamConfigRequest toUpsertStreamConfigRequest(
            String exchange, String marketType, String symbol, 
            Boolean marketStreamEnabled, Boolean newsStreamEnabled) {
        return UpsertStreamConfigRequest.builder()
                .exchange(exchange)
                .marketType(marketType)
                .symbol(symbol)
                .marketStreamEnabled(marketStreamEnabled)
                .newsStreamEnabled(newsStreamEnabled)
                .build();
    }

    public static UpsertForecastConfigRequest toUpsertForecastConfigRequest(
            UpsertForecastConfigUiRequest uiRequest, String symbol) {
        return UpsertForecastConfigRequest.builder()
                .exchange(uiRequest.getExchange())
                .marketType(uiRequest.getMarketType())
                .symbol(symbol)
                .enabled(uiRequest.getEnabled())
                .enabledHorizons(uiRequest.getEnabledHorizons())
                .lane(uiRequest.getLane())
                .modelProfile(uiRequest.getModelProfile())
                .build();
    }

    public static CreateBackfillJobRequest toCreateBackfillJobRequest(
            CreateBackfillJobUiRequest uiRequest, String symbol) {
        return CreateBackfillJobRequest.builder()
                .exchange(uiRequest.getExchange())
                .marketType(uiRequest.getMarketType())
                .symbol(symbol)
                .fromTs(uiRequest.getFromTs())
                .toTs(uiRequest.getToTs())
                .resolution(uiRequest.getResolution())
                .build();
    }
}

