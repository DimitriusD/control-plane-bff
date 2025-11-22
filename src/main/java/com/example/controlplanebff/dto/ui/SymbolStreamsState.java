package com.example.controlplanebff.dto.ui;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SymbolStreamsState {
    private String symbol;
    private String baseAsset;
    private String quoteAsset;
    private MarketStreamState marketStream;
    private NewsStreamState newsStream;
    private ForecastingState forecasting;
    private BackfillState backfill;
}



