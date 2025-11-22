package com.example.controlplanebff.dto.ui;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ForecastHorizonsDictionary {
    private java.util.List<String> items;
    private Map<String, java.util.List<String>> defaultsByAssetType;
}


