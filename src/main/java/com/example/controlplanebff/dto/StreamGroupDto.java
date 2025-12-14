package com.example.controlplanebff.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class StreamGroupDto {
    private String id;
    private String kind; // MARKET | NEWS
    private String name;
    private String exchange;
    private String assetType;
    private String marketType;
    private String baseAsset;
    private String quoteAsset;
    private String region;
    private String window;
    private String status; // RUNNING | STARTING | STOPPED | DEGRADED | FAILED
    private List<StreamChannelDto> channels;
    
    // Group-level metrics
    private Double messagesPerSec;
    private Integer avgLagMs;
    private Instant lastHeartbeat;
    private Instant createdAt;
    private Instant updatedAt;
}

