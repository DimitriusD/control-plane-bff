package com.example.controlplanebff.service;

import com.example.controlplanebff.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MockBackfillJobsService {

    private final Map<String, BackfillJobDto> jobs = new ConcurrentHashMap<>();

    public MockBackfillJobsService() {
        initializeMockData();
    }

    private void initializeMockData() {
        
        BackfillJobDto job1 = BackfillJobDto.builder()
                .id("bf-binance-btcusdt-2024-01")
                .exchange("BINANCE")
                .assetType("CRYPTO")
                .marketType("SPOT")
                .symbol("BTCUSDT")
                .baseAsset("BTC")
                .quoteAsset("USDT")
                .region("EU")
                .dateFrom(Instant.parse("2024-01-01T00:00:00Z"))
                .dateTo(Instant.parse("2024-02-01T00:00:00Z"))
                .status("RUNNING")
                .progressPercent(42.5)
                .processedBars(123456L)
                .createdAt(Instant.parse("2024-02-01T10:00:00Z"))
                .startedAt(Instant.parse("2024-02-01T10:05:00Z"))
                .finishedAt(null)
                .build();

        BackfillJobDto job2 = BackfillJobDto.builder()
                .id("bf-bybit-ethusdt-2024-02")
                .exchange("BYBIT")
                .assetType("CRYPTO")
                .marketType("SPOT")
                .symbol("ETHUSDT")
                .baseAsset("ETH")
                .quoteAsset("USDT")
                .region("US")
                .dateFrom(Instant.parse("2024-02-01T00:00:00Z"))
                .dateTo(Instant.parse("2024-03-01T00:00:00Z"))
                .status("COMPLETED")
                .progressPercent(100.0)
                .processedBars(234567L)
                .createdAt(Instant.parse("2024-03-01T10:00:00Z"))
                .startedAt(Instant.parse("2024-03-01T10:05:00Z"))
                .finishedAt(Instant.parse("2024-03-01T12:30:00Z"))
                .build();

        BackfillJobDto job3 = BackfillJobDto.builder()
                .id("bf-binance-solusdt-2024-03")
                .exchange("BINANCE")
                .assetType("CRYPTO")
                .marketType("SPOT")
                .symbol("SOLUSDT")
                .baseAsset("SOL")
                .quoteAsset("USDT")
                .region("EU")
                .dateFrom(Instant.parse("2024-03-01T00:00:00Z"))
                .dateTo(Instant.parse("2024-04-01T00:00:00Z"))
                .status("FAILED")
                .progressPercent(67.8)
                .processedBars(156789L)
                .createdAt(Instant.parse("2024-04-01T10:00:00Z"))
                .startedAt(Instant.parse("2024-04-01T10:05:00Z"))
                .finishedAt(Instant.parse("2024-04-01T11:20:00Z"))
                .build();

        jobs.put(job1.getId(), job1);
        jobs.put(job2.getId(), job2);
        jobs.put(job3.getId(), job3);
    }

    public PageResponse<BackfillJobDto> listJobs(String exchange, String marketType, String assetType,
                                                 String symbol, String status, String search,
                                                 Integer page, Integer size) {
        log.info("Listing backfill jobs: exchange={}, marketType={}, assetType={}, symbol={}, status={}", 
                exchange, marketType, assetType, symbol, status);

        List<BackfillJobDto> filtered = new ArrayList<>(jobs.values());

        if (exchange != null && !exchange.isBlank()) {
            filtered = filtered.stream()
                    .filter(j -> exchange.equals(j.getExchange()))
                    .collect(Collectors.toList());
        }

        if (marketType != null && !marketType.isBlank()) {
            filtered = filtered.stream()
                    .filter(j -> marketType.equals(j.getMarketType()))
                    .collect(Collectors.toList());
        }

        if (assetType != null && !assetType.isBlank()) {
            filtered = filtered.stream()
                    .filter(j -> assetType.equals(j.getAssetType()))
                    .collect(Collectors.toList());
        }

        if (symbol != null && !symbol.isBlank()) {
            filtered = filtered.stream()
                    .filter(j -> symbol.equals(j.getSymbol()))
                    .collect(Collectors.toList());
        }

        if (status != null && !status.isBlank()) {
            filtered = filtered.stream()
                    .filter(j -> status.equals(j.getStatus()))
                    .collect(Collectors.toList());
        }

        if (search != null && !search.isBlank()) {
            String searchLower = search.toLowerCase();
            filtered = filtered.stream()
                    .filter(j -> (j.getId() != null && j.getId().toLowerCase().contains(searchLower)) ||
                                 (j.getSymbol() != null && j.getSymbol().toLowerCase().contains(searchLower)))
                    .collect(Collectors.toList());
        }

        int pageNum = page != null && page >= 0 ? page : 0;
        int sizeNum = size != null && size > 0 ? size : 20;
        int start = pageNum * sizeNum;
        int end = Math.min(start + sizeNum, filtered.size());

        List<BackfillJobDto> items = start < filtered.size() 
                ? filtered.subList(start, end) 
                : List.of();

        return PageResponse.<BackfillJobDto>builder()
                .items(items)
                .page(pageNum)
                .size(sizeNum)
                .total((long) filtered.size())
                .build();
    }

    public BackfillStatsResponse getStats() {
        log.info("Getting backfill stats");

        List<BackfillJobDto> allJobs = new ArrayList<>(jobs.values());
        
        long activeJobs = allJobs.stream()
                .filter(j -> "RUNNING".equals(j.getStatus()) || "PENDING".equals(j.getStatus()))
                .count();
        
        long failedJobs24h = allJobs.stream()
                .filter(j -> "FAILED".equals(j.getStatus()))
                .count(); // Simplified
        
        double avgDurationSec = allJobs.stream()
                .filter(j -> j.getStartedAt() != null && j.getFinishedAt() != null)
                .mapToLong(j -> j.getFinishedAt().getEpochSecond() - j.getStartedAt().getEpochSecond())
                .average()
                .orElse(0.0);
        
        long processedBarsTotal = allJobs.stream()
                .filter(j -> j.getProcessedBars() != null)
                .mapToLong(BackfillJobDto::getProcessedBars)
                .sum();

        return BackfillStatsResponse.builder()
                .activeJobs((int) activeJobs)
                .failedJobs24h((int) failedJobs24h)
                .avgDurationSec(avgDurationSec)
                .processedBarsTotal(processedBarsTotal)
                .build();
    }

    public BackfillJobDto getJob(String jobId) {
        log.info("Getting backfill job: {}", jobId);
        return jobs.get(jobId);
    }

    public BackfillJobDto createJob(CreateBackfillJobRequest request) {
        log.info("Creating backfill job: exchange={}, symbol={}", request.getExchange(), 
                request.getBaseAsset() + request.getQuoteAsset());

        String symbol = request.getBaseAsset() + request.getQuoteAsset();
        String id = String.format("bf-%s-%s-%s", 
                request.getExchange().toLowerCase(),
                symbol.toLowerCase(),
                request.getDateFrom().toString().substring(0, 7).replace("-", ""));

        Instant now = Instant.now();
        String initialStatus = "PENDING";

        BackfillJobDto job = BackfillJobDto.builder()
                .id(id)
                .exchange(request.getExchange())
                .assetType(request.getAssetType())
                .marketType(request.getMarketType())
                .symbol(symbol)
                .baseAsset(request.getBaseAsset())
                .quoteAsset(request.getQuoteAsset())
                .region(request.getRegion())
                .dateFrom(request.getDateFrom())
                .dateTo(request.getDateTo())
                .status(initialStatus)
                .progressPercent(0.0)
                .processedBars(0L)
                .createdAt(now)
                .startedAt(null)
                .finishedAt(null)
                .build();

        jobs.put(id, job);
        return job;
    }

    public BackfillJobDto updateJobState(String jobId, UpdateBackfillJobStateRequest request) {
        log.info("Updating backfill job state: jobId={}, desiredState={}", jobId, request.getDesiredState());

        BackfillJobDto job = jobs.get(jobId);
        if (job == null) {
            return null;
        }

        BackfillJobDto updated;
        if ("RERUN".equals(request.getDesiredState())) {
            // Create a new job cloned from this one
            String newId = String.format("bf-%s-%s-%s-rerun", 
                    job.getExchange().toLowerCase(),
                    job.getSymbol().toLowerCase(),
                    Instant.now().toString().substring(0, 7).replace("-", ""));
            
            updated = BackfillJobDto.builder()
                    .id(newId)
                    .exchange(job.getExchange())
                    .assetType(job.getAssetType())
                    .marketType(job.getMarketType())
                    .symbol(job.getSymbol())
                    .baseAsset(job.getBaseAsset())
                    .quoteAsset(job.getQuoteAsset())
                    .region(job.getRegion())
                    .dateFrom(job.getDateFrom())
                    .dateTo(job.getDateTo())
                    .status("PENDING")
                    .progressPercent(0.0)
                    .processedBars(0L)
                    .createdAt(Instant.now())
                    .startedAt(null)
                    .finishedAt(null)
                    .build();
            
            jobs.put(newId, updated);
        } else {
            updated = BackfillJobDto.builder()
                    .id(job.getId())
                    .exchange(job.getExchange())
                    .assetType(job.getAssetType())
                    .marketType(job.getMarketType())
                    .symbol(job.getSymbol())
                    .baseAsset(job.getBaseAsset())
                    .quoteAsset(job.getQuoteAsset())
                    .region(job.getRegion())
                    .dateFrom(job.getDateFrom())
                    .dateTo(job.getDateTo())
                    .status(request.getDesiredState())
                    .progressPercent(job.getProgressPercent())
                    .processedBars(job.getProcessedBars())
                    .createdAt(job.getCreatedAt())
                    .startedAt(job.getStartedAt())
                    .finishedAt(job.getFinishedAt())
                    .build();
            
            jobs.put(jobId, updated);
        }

        return updated;
    }
}

