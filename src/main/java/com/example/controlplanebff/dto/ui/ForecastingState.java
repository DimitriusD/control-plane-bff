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
public class ForecastingState {
    private Boolean enabled;
    private List<String> enabledHorizons;
    private String lane;
    private String modelProfile;
}



