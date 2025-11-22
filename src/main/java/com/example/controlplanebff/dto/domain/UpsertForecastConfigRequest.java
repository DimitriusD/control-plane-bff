package com.example.controlplanebff.dto.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpsertForecastConfigRequest {
    private String exchange;
    private String marketType;
    private String symbol;
    private Boolean enabled;
    private List<String> enabledHorizons;
    private String lane;
    private String modelProfile;
}



