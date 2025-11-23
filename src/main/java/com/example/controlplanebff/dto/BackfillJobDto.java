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
public class BackfillJobDto {
    private String id;
    private String exchange;
    private String assetType;
    private String marketType;
    private String symbol;
    private String baseAsset;
    private String quoteAsset;
    private String region;
    private Instant dateFrom;
    private Instant dateTo;
    private String status;
    private Double progressPercent;
    private Long processedBars;
    private Instant createdAt;
    private Instant startedAt;
    private Instant finishedAt;
}

