package com.example.controlplanebff.dto.ui;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DictionariesResponse {
    private AssetTypesDictionary assetTypes;
    private ExchangesDictionary exchanges;
    private MarketTypesDictionary marketTypes;
    private ForecastLanesDictionary forecastLanes;
    private ForecastHorizonsDictionary forecastHorizons;
    private BackfillResolutionsDictionary backfillResolutions;
    private DefaultsDictionary defaults;
}


