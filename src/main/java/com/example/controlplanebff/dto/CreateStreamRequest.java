package com.example.controlplanebff.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateStreamRequest {
    private String kind; // MARKET | NEWS
    private String assetType;
    private String exchange;
    private String marketType;
    private String baseAsset;
    private String quoteAsset;
    private String region;
    private String window;
    private List<String> channels;
    private Boolean autoStart;
    
    // NEWS-specific fields
    private String sourceType;
    private String language;
    private List<String> symbolFilter;
    private Integer pollIntervalSec;
}

