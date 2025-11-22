package com.example.controlplanebff.dto.ui;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateBackfillJobUiRequest {
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
    
    @NotNull(message = "fromTs must not be null")
    private Instant fromTs;
    
    @NotNull(message = "toTs must not be null")
    private Instant toTs;
    
    @NotBlank(message = "resolution must not be blank")
    private String resolution;
}



