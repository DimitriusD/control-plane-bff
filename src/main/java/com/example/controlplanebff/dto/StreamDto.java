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
public class StreamDto {
    private String id;
    private String kind; // MARKET | NEWS
    private String name;
    private String exchange;
    private String assetType;
    private String marketType;
    private String symbol;
    private String region;
    private String status;
    private Double messagesPerSec;
    private Integer avgLagMs;
    private Instant lastHeartbeat;
    private Instant createdAt;
    private String window;
    
    // Extended fields for details
    private StreamAssignment assignment;
    private StreamMetricsSnapshot metricsSnapshot;
    private Long uptimeSec;
    private Instant updatedAt;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StreamAssignment {
        private String agentInstanceId;
        private String agentRegion;
        private Instant assignedAt;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StreamMetricsSnapshot {
        private Double messagesPerSec;
        private Integer avgLagMs;
        private Double errorRate;
    }
}

