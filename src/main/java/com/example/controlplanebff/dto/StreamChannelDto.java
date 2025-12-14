package com.example.controlplanebff.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class StreamChannelDto {
    private String channel; // e.g. TRADES, KLINES_1M, BOOK_TICKER, DEPTH
    private Boolean enabled;
    private String runtimeStatus; // RUNNING | STARTING | STOPPED | FAILED
    
    // Optional assignment
    private StreamChannelAssignment assignment;
    
    // Optional metrics snapshot
    private StreamChannelMetricsSnapshot metricsSnapshot;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class StreamChannelAssignment {
        private String agentInstanceId;
        private String agentRegion;
        private Instant assignedAt;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class StreamChannelMetricsSnapshot {
        private Double messagesPerSec;
        private Integer avgLagMs;
        private Double errorRate;
    }
}

