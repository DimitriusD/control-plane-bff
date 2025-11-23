package com.example.controlplanebff.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BackfillStatsResponse {
    private Integer activeJobs;
    private Integer failedJobs24h;
    private Double avgDurationSec;
    private Long processedBarsTotal;
}

