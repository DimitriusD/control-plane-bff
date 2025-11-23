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
public class UpdateForecastConfigRequest {
    private String name;
    private String exchange;
    private String assetType;
    private String marketType;
    private String symbol;
    private String horizon;
    private List<String> targets;
    private List<String> modelTypes;
    private Boolean enabled;
}

