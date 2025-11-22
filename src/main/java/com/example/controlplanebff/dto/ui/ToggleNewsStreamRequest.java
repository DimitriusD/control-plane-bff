package com.example.controlplanebff.dto.ui;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ToggleNewsStreamRequest {
    @NotBlank(message = "exchange must not be blank")
    private String exchange;
    
    @NotBlank(message = "marketType must not be blank")
    private String marketType;
    
    @NotBlank(message = "assetType must not be blank")
    private String assetType;
    
    @NotBlank(message = "baseAsset must not be blank")
    private String baseAsset;
    
    @NotBlank(message = "quoteAsset must not be blank")
    private String quoteAsset;
    
    @NotNull(message = "enabled must not be null")
    private Boolean enabled;
}



