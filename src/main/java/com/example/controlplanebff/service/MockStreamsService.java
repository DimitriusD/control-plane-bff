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
public class MockStreamsService {

    private final Map<String, StreamDto> streams = new ConcurrentHashMap<>();

    public MockStreamsService() {
        // Initialize with some mock streams
        initializeMockData();
    }

    private void initializeMockData() {
        Instant now = Instant.now();
        
        StreamDto stream1 = StreamDto.builder()
                .id("md-binance-btcusdt-spot-eu")
                .kind("MARKET")
                .name("Binance BTCUSDT Spot EU")
                .exchange("BINANCE")
                .assetType("CRYPTO")
                .marketType("SPOT")
                .symbol("BTCUSDT")
                .region("EU")
                .status("RUNNING")
                .messagesPerSec(250.5)
                .avgLagMs(120)
                .lastHeartbeat(now)
                .createdAt(now.minusSeconds(8023))
                .window("1s")
                .assignment(StreamDto.StreamAssignment.builder()
                        .agentInstanceId("agent-03")
                        .agentRegion("EU")
                        .assignedAt(now.minusSeconds(8023))
                        .build())
                .metricsSnapshot(StreamDto.StreamMetricsSnapshot.builder()
                        .messagesPerSec(250.5)
                        .avgLagMs(120)
                        .errorRate(0.001)
                        .build())
                .uptimeSec(8023L)
                .updatedAt(now)
                .build();

        StreamDto stream2 = StreamDto.builder()
                .id("md-bybit-ethusdt-spot-us")
                .kind("MARKET")
                .name("Bybit ETHUSDT Spot US")
                .exchange("BYBIT")
                .assetType("CRYPTO")
                .marketType("SPOT")
                .symbol("ETHUSDT")
                .region("US")
                .status("RUNNING")
                .messagesPerSec(180.2)
                .avgLagMs(95)
                .lastHeartbeat(now)
                .createdAt(now.minusSeconds(5400))
                .window("1s")
                .assignment(StreamDto.StreamAssignment.builder()
                        .agentInstanceId("agent-01")
                        .agentRegion("US")
                        .assignedAt(now.minusSeconds(5400))
                        .build())
                .metricsSnapshot(StreamDto.StreamMetricsSnapshot.builder()
                        .messagesPerSec(180.2)
                        .avgLagMs(95)
                        .errorRate(0.0005)
                        .build())
                .uptimeSec(5400L)
                .updatedAt(now)
                .build();

        StreamDto stream3 = StreamDto.builder()
                .id("news-google-crypto-eu")
                .kind("NEWS")
                .name("Google News Crypto EU")
                .exchange(null)
                .assetType("CRYPTO")
                .marketType(null)
                .symbol(null)
                .region("EU")
                .status("RUNNING")
                .messagesPerSec(5.2)
                .avgLagMs(200)
                .lastHeartbeat(now)
                .createdAt(now.minusSeconds(12000))
                .window("30s")
                .assignment(StreamDto.StreamAssignment.builder()
                        .agentInstanceId("agent-05")
                        .agentRegion("EU")
                        .assignedAt(now.minusSeconds(12000))
                        .build())
                .metricsSnapshot(StreamDto.StreamMetricsSnapshot.builder()
                        .messagesPerSec(5.2)
                        .avgLagMs(200)
                        .errorRate(0.002)
                        .build())
                .uptimeSec(12000L)
                .updatedAt(now)
                .build();

        streams.put(stream1.getId(), stream1);
        streams.put(stream2.getId(), stream2);
        streams.put(stream3.getId(), stream3);
    }

    public PageResponse<StreamDto> listStreams(String kind, String status, String exchange,
                                               String assetType, String search,
                                               Integer page, Integer size) {
        log.info("Listing streams: kind={}, status={}, exchange={}, assetType={}, search={}", 
                kind, status, exchange, assetType, search);

        List<StreamDto> filtered = new ArrayList<>(streams.values());

        // Filter by kind (required)
        if (kind != null) {
            filtered = filtered.stream()
                    .filter(s -> kind.equals(s.getKind()))
                    .collect(Collectors.toList());
        }

        // Filter by status
        if (status != null && !status.isBlank()) {
            filtered = filtered.stream()
                    .filter(s -> status.equals(s.getStatus()))
                    .collect(Collectors.toList());
        }

        // Filter by exchange
        if (exchange != null && !exchange.isBlank()) {
            filtered = filtered.stream()
                    .filter(s -> exchange.equals(s.getExchange()))
                    .collect(Collectors.toList());
        }

        // Filter by assetType
        if (assetType != null && !assetType.isBlank()) {
            filtered = filtered.stream()
                    .filter(s -> assetType.equals(s.getAssetType()))
                    .collect(Collectors.toList());
        }

        // Filter by search
        if (search != null && !search.isBlank()) {
            String searchLower = search.toLowerCase();
            filtered = filtered.stream()
                    .filter(s -> (s.getId() != null && s.getId().toLowerCase().contains(searchLower)) ||
                                 (s.getName() != null && s.getName().toLowerCase().contains(searchLower)) ||
                                 (s.getSymbol() != null && s.getSymbol().toLowerCase().contains(searchLower)))
                    .collect(Collectors.toList());
        }

        // Pagination
        int pageNum = page != null && page >= 0 ? page : 0;
        int sizeNum = size != null && size > 0 ? size : 20;
        int start = pageNum * sizeNum;
        int end = Math.min(start + sizeNum, filtered.size());

        List<StreamDto> items = start < filtered.size() 
                ? filtered.subList(start, end) 
                : List.of();

        return PageResponse.<StreamDto>builder()
                .items(items)
                .page(pageNum)
                .size(sizeNum)
                .total((long) filtered.size())
                .build();
    }

    public StreamStatsResponse getStats(String kind, String status, String exchange,
                                       String assetType, String search) {
        log.info("Getting stream stats: kind={}, status={}, exchange={}, assetType={}", 
                kind, status, exchange, assetType);

        // Get filtered streams (reuse filtering logic)
        PageResponse<StreamDto> filtered = listStreams(kind, status, exchange, assetType, search, 0, Integer.MAX_VALUE);
        
        List<StreamDto> allStreams = filtered.getItems();
        
        long activeStreams = allStreams.stream()
                .filter(s -> "RUNNING".equals(s.getStatus()))
                .count();
        
        long failedStreams24h = allStreams.stream()
                .filter(s -> "FAILED".equals(s.getStatus()))
                .count(); // Simplified - in real app would check timestamp
        
        double avgLagMs = allStreams.stream()
                .filter(s -> s.getAvgLagMs() != null)
                .mapToInt(StreamDto::getAvgLagMs)
                .average()
                .orElse(0.0);
        
        double messagesPerSec = allStreams.stream()
                .filter(s -> s.getMessagesPerSec() != null)
                .mapToDouble(StreamDto::getMessagesPerSec)
                .sum();

        return StreamStatsResponse.builder()
                .activeStreams((int) activeStreams)
                .failedStreams24h((int) failedStreams24h)
                .avgLagMs(avgLagMs)
                .messagesPerSec(messagesPerSec)
                .build();
    }

    public StreamDto getStream(String streamId) {
        log.info("Getting stream: {}", streamId);
        return streams.get(streamId);
    }

    public StreamDto createStream(CreateStreamRequest request) {
        log.info("Creating stream: kind={}, exchange={}", 
                request.getKind(), request.getExchange());

        String id;
        String name;
        String symbol = null;

        if ("MARKET".equals(request.getKind())) {
            symbol = request.getBaseAsset() + request.getQuoteAsset();
            id = String.format("md-%s-%s-%s-%s", 
                    request.getExchange().toLowerCase(),
                    symbol.toLowerCase(),
                    request.getMarketType().toLowerCase(),
                    request.getRegion().toLowerCase());
            name = String.format("%s %s %s %s", 
                    request.getExchange(), symbol, request.getMarketType(), request.getRegion());
        } else {
            id = String.format("news-%s-%s-%s", 
                    request.getSourceType().toLowerCase().replace("_", "-"),
                    request.getAssetType().toLowerCase(),
                    request.getRegion().toLowerCase());
            name = String.format("%s %s %s", 
                    request.getSourceType(), request.getAssetType(), request.getRegion());
        }

        Instant now = Instant.now();
        String initialStatus = Boolean.TRUE.equals(request.getAutoStart()) ? "STARTING" : "PENDING";

        StreamDto stream = StreamDto.builder()
                .id(id)
                .kind(request.getKind())
                .name(name)
                .exchange(request.getExchange())
                .assetType(request.getAssetType())
                .marketType(request.getMarketType())
                .symbol(symbol)
                .region(request.getRegion())
                .status(initialStatus)
                .window(request.getWindow())
                .createdAt(now)
                .updatedAt(now)
                .build();

        streams.put(id, stream);
        return stream;
    }

    public StreamDto updateStreamState(String streamId, UpdateStreamStateRequest request) {
        log.info("Updating stream state: streamId={}, desiredState={}", streamId, request.getDesiredState());

        StreamDto stream = streams.get(streamId);
        if (stream == null) {
            return null;
        }

        StreamDto updated = StreamDto.builder()
                .id(stream.getId())
                .kind(stream.getKind())
                .name(stream.getName())
                .exchange(stream.getExchange())
                .assetType(stream.getAssetType())
                .marketType(stream.getMarketType())
                .symbol(stream.getSymbol())
                .region(stream.getRegion())
                .status(request.getDesiredState())
                .messagesPerSec(stream.getMessagesPerSec())
                .avgLagMs(stream.getAvgLagMs())
                .lastHeartbeat(stream.getLastHeartbeat())
                .createdAt(stream.getCreatedAt())
                .window(stream.getWindow())
                .assignment(stream.getAssignment())
                .metricsSnapshot(stream.getMetricsSnapshot())
                .uptimeSec(stream.getUptimeSec())
                .updatedAt(Instant.now())
                .build();

        streams.put(streamId, updated);
        return updated;
    }

    public StreamMetricsResponse getStreamMetrics(String streamId, Instant from, Instant to) {
        log.info("Getting stream metrics: streamId={}, from={}, to={}", streamId, from, to);

        StreamDto stream = streams.get(streamId);
        if (stream == null) {
            return StreamMetricsResponse.builder().points(List.of()).build();
        }

        // Generate mock time series
        List<MetricPoint> points = new ArrayList<>();
        Instant current = from != null ? from : Instant.now().minusSeconds(3600);
        Instant end = to != null ? to : Instant.now();
        
        double baseMessagesPerSec = stream.getMessagesPerSec() != null ? stream.getMessagesPerSec() : 200.0;
        int baseLagMs = stream.getAvgLagMs() != null ? stream.getAvgLagMs() : 120;

        while (current.isBefore(end) && points.size() < 100) {
            // Add some random jitter
            double messagesPerSec = baseMessagesPerSec + (Math.random() - 0.5) * 50;
            int avgLagMs = baseLagMs + (int) ((Math.random() - 0.5) * 40);
            double errorRate = Math.random() * 0.005;

            points.add(MetricPoint.builder()
                    .ts(current)
                    .messagesPerSec(messagesPerSec)
                    .avgLagMs(avgLagMs)
                    .errorRate(errorRate)
                    .build());

            current = current.plusSeconds(60); // 1-minute steps
        }

        return StreamMetricsResponse.builder()
                .points(points)
                .build();
    }
}

