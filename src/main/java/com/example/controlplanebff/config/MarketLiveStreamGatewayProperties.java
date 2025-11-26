package com.example.controlplanebff.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "market-live-stream-gateway")
public record MarketLiveStreamGatewayProperties(String wsBaseUrl) {
}

