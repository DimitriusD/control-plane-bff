package com.example.controlplanebff.dto.ui;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ControlPlaneStreamsStateQuery {
    @NotBlank(message = "assetType is required")
    private String assetType;
    
    @NotBlank(message = "exchange is required")
    private String exchange;
    
    @NotBlank(message = "marketType is required")
    private String marketType;
}


