package com.example.controlplanebff.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StreamStatsResponse {
    private Integer activeStreams;
    private Integer failedStreams24h;
    private Double avgLagMs;
    private Double messagesPerSec;
}

