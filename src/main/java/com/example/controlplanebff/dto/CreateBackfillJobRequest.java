package com.example.controlplanebff.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateBackfillJobRequest {
    private String exchange;
    private String assetType;
    private String marketType;
    private String baseAsset;
    private String quoteAsset;
    private String region;
    private Instant dateFrom;
    private Instant dateTo;
    private String window;
}

