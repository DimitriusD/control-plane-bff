package com.example.controlplanebff.service;

import com.example.controlplanebff.client.ControlPlaneApiClient;
import com.example.controlplanebff.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BackfillJobsService {

    private final ControlPlaneApiClient apiClient;

    public PageResponse<BackfillJobDto> listJobs(String exchange, String marketType, String assetType,
                                                 String symbol, String status, String search,
                                                 Integer page, Integer size) {
        log.info("Listing backfill jobs: exchange={}, marketType={}, assetType={}, symbol={}, status={}", 
                exchange, marketType, assetType, symbol, status);
        return apiClient.getBackfillJobs(exchange, marketType, assetType, symbol, status, search, page, size);
    }

    public BackfillStatsResponse getStats() {
        log.info("Getting backfill stats");
        return apiClient.getBackfillStats();
    }

    public BackfillJobDto getJob(String jobId) {
        log.info("Getting backfill job: {}", jobId);
        return apiClient.getBackfillJob(jobId);
    }

    public BackfillJobDto createJob(CreateBackfillJobRequest request) {
        log.info("Creating backfill job: exchange={}, symbol={}", request.getExchange(), 
                request.getBaseAsset() + request.getQuoteAsset());
        return apiClient.createBackfillJob(request);
    }

    public BackfillJobDto updateJobState(String jobId, UpdateBackfillJobStateRequest request) {
        log.info("Updating backfill job state: jobId={}, desiredState={}", jobId, request.getDesiredState());
        return apiClient.updateBackfillJobState(jobId, request);
    }
}

