package com.example.controlplanebff.service;

import com.example.controlplanebff.client.ControlPlaneApiClient;
import com.example.controlplanebff.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j
public class StreamsService {

    private final ControlPlaneApiClient apiClient;

    public PageResponse<StreamDto> listStreams(String kind, String status, String exchange,
                                               String assetType, String search,
                                               Integer page, Integer size) {
        log.info("Listing streams: kind={}, status={}, exchange={}, assetType={}, search={}", 
                kind, status, exchange, assetType, search);
        return apiClient.getStreams(kind, status, exchange, assetType, search, page, size);
    }

    public StreamStatsResponse getStats(String kind, String status, String exchange,
                                        String assetType, String search) {
        log.info("Getting stream stats: kind={}, status={}, exchange={}, assetType={}", 
                kind, status, exchange, assetType);
        return apiClient.getStreamStats(kind, status, exchange, assetType, search);
    }

    public StreamDto getStream(String streamId) {
        log.info("Getting stream: {}", streamId);
        return apiClient.getStream(streamId);
    }

    public StreamDto createStream(CreateStreamRequest request) {
        log.info("Creating stream: kind={}, exchange={}", request.getKind(), request.getExchange());
        return apiClient.createStream(request);
    }

    public StreamDto updateStreamState(String streamId, UpdateStreamStateRequest request) {
        log.info("Updating stream state: streamId={}, desiredState={}", streamId, request.getDesiredState());
        return apiClient.updateStreamState(streamId, request);
    }

    public StreamMetricsResponse getStreamMetrics(String streamId, Instant from, Instant to) {
        log.info("Getting stream metrics: streamId={}, from={}, to={}", streamId, from, to);
        return apiClient.getStreamMetrics(streamId, from, to);
    }
}

