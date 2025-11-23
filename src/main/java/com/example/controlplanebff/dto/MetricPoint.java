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
public class MetricPoint {
    private Instant ts;
    private Double messagesPerSec;
    private Integer avgLagMs;
    private Double errorRate;
}

