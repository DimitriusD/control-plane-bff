package com.example.controlplanebff.dto.ui;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeDictionaryItem {
    private String code;
    private String name;
    private List<String> supportedAssetTypes;
    private String defaultMarketType;
}


