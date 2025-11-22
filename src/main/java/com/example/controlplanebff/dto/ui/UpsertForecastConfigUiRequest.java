package com.example.controlplanebff.dto.ui;

import com.example.controlplanebff.validation.ValidHorizons;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpsertForecastConfigUiRequest {
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
    
    @Size(min = 1, message = "enabledHorizons must not be empty")
    @ValidHorizons
    private List<@NotBlank String> enabledHorizons;
    
    private String lane;
    private String modelProfile;
}

