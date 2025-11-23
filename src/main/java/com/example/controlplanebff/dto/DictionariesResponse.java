package com.example.controlplanebff.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DictionariesResponse {
    private String environment;
    private String status;
    private List<String> assetTypes;
    private List<ExchangeItem> exchanges;
    private List<String> marketTypes;
    private List<String> regions;
    private List<String> windows;
    private List<String> streamStates;
    private List<String> forecastTargets;
    private List<String> forecastHorizons;
    private List<String> modelTypes;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExchangeItem {
        private String code;
        private String name;
        private List<String> assetTypes;
    }
}

