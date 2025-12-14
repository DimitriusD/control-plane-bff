package com.example.controlplanebff.client;

import com.example.controlplanebff.dto.*;
import com.example.controlplanebff.dto.upstream.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class ControlPlaneApiClient {

    private final WebClient controlPlaneApiWebClient;

    public ItemsResponse<String> getAssetTypes() {
        return callGet("/asset-types", new ParameterizedTypeReference<ItemsResponse<String>>() {});
    }

    public ItemsResponse<String> getMarketTypes() {
        return callGet("/market-types", new ParameterizedTypeReference<ItemsResponse<String>>() {});
    }

    public ItemsResponse<String> getStreamStatuses() {
        return callGet("/stream-statuses", new ParameterizedTypeReference<ItemsResponse<String>>() {});
    }

    public ItemsResponse<RegionDto> getRegions() {
        return callGet("/regions", new ParameterizedTypeReference<ItemsResponse<RegionDto>>() {});
    }

    public ItemsResponse<WindowDto> getWindows() {
        return callGet("/windows", new ParameterizedTypeReference<ItemsResponse<WindowDto>>() {});
    }

    public ItemsResponse<ForecastTargetDto> getForecastTargets() {
        return callGet("/forecast-targets", new ParameterizedTypeReference<ItemsResponse<ForecastTargetDto>>() {});
    }

    public ItemsResponse<String> getForecastHorizons() {
        return callGet("/forecast-horizons", new ParameterizedTypeReference<ItemsResponse<String>>() {});
    }

    public ItemsResponse<ModelTypeDto> getModelTypes() {
        return callGet("/model-types", new ParameterizedTypeReference<ItemsResponse<ModelTypeDto>>() {});
    }

    public PageResponse<ExchangeApiDto> getExchanges(Integer page, Integer size) {
        return callGetWithQuery("/exchanges", Map.of(
                "page", page != null ? page.toString() : "0",
                "size", size != null ? size.toString() : "1000"
        ), new ParameterizedTypeReference<PageResponse<ExchangeApiDto>>() {});
    }

    public PageResponse<MarketSymbolApiDto> getMarketSymbols(String exchange, String marketType, 
                                                               String assetType, Boolean enabled,
                                                               String search, Integer page, Integer size) {
        return callGetWithQuery("/market-symbols", buildQueryParams(
                exchange, marketType, assetType, enabled, search, page, size
        ), new ParameterizedTypeReference<PageResponse<MarketSymbolApiDto>>() {});
    }

    public ItemsResponse<String> getBaseAssets(String exchange, String marketType) {
        return callGetWithQuery("/market-symbols/base-assets", Map.of(
                "exchange", exchange,
                "marketType", marketType
        ), new ParameterizedTypeReference<ItemsResponse<String>>() {});
    }

    public ItemsResponse<String> getQuoteAssets(String exchange, String marketType, String baseAsset) {
        return callGetWithQuery("/market-symbols/quote-assets", Map.of(
                "exchange", exchange,
                "marketType", marketType,
                "baseAsset", baseAsset
        ), new ParameterizedTypeReference<ItemsResponse<String>>() {});
    }

    public PageResponse<SymbolItem> suggestSymbols(String exchange, String marketType, 
                                                     String search, String base, 
                                                     Integer page, Integer size) {
        return callGetWithQuery("/market-symbols/suggest", buildSuggestQueryParams(
                exchange, marketType, search, base, page, size
        ), new ParameterizedTypeReference<PageResponse<SymbolItem>>() {});
    }

    public PageResponse<StreamGroupDto> getStreams(String kind, String status, String exchange,
                                                    String assetType, String marketType, String region,
                                                    String window, String search,
                                                    Integer page, Integer size) {
        return callGetWithQuery("/streams", buildStreamsQueryParams(
                kind, status, exchange, assetType, marketType, region, window, search, page, size
        ), new ParameterizedTypeReference<PageResponse<StreamGroupDto>>() {});
    }

    public StreamStatsResponse getStreamStats(String kind, String status, String exchange,
                                              String assetType, String search) {
        return callGetWithQuery("/streams/stats", buildStreamsQueryParams(
                kind, status, exchange, assetType, null, null, null, search, null, null
        ), new ParameterizedTypeReference<StreamStatsResponse>() {});
    }

    public StreamGroupDto getStream(String streamId) {
        return callGet("/streams/" + streamId, new ParameterizedTypeReference<StreamGroupDto>() {});
    }

    public StreamGroupDto createStream(CreateStreamRequest request) {
        return callPost("/streams", request, new ParameterizedTypeReference<StreamGroupDto>() {});
    }

    public StreamGroupDto updateStreamState(String streamId, UpdateStreamStateRequest request) {
        return callPatch("/streams/" + streamId, request, new ParameterizedTypeReference<StreamGroupDto>() {});
    }

    public StreamMetricsResponse getStreamMetrics(String streamId, Instant from, Instant to) {
        return callGetWithQuery("/streams/" + streamId + "/metrics", buildMetricsQueryParams(from, to),
                new ParameterizedTypeReference<StreamMetricsResponse>() {});
    }

    public PageResponse<BackfillJobDto> getBackfillJobs(String exchange, String marketType, String assetType,
                                                        String symbol, String status, String search,
                                                        Integer page, Integer size) {
        return callGetWithQuery("/backfill-jobs", buildBackfillQueryParams(
                exchange, marketType, assetType, symbol, status, search, page, size
        ), new ParameterizedTypeReference<PageResponse<BackfillJobDto>>() {});
    }

    public BackfillStatsResponse getBackfillStats() {
        return callGet("/backfill-jobs/stats", new ParameterizedTypeReference<BackfillStatsResponse>() {});
    }

    public BackfillJobDto getBackfillJob(String jobId) {
        return callGet("/backfill-jobs/" + jobId, new ParameterizedTypeReference<BackfillJobDto>() {});
    }

    public BackfillJobDto createBackfillJob(CreateBackfillJobRequest request) {
        return callPost("/backfill-jobs", request, new ParameterizedTypeReference<BackfillJobDto>() {});
    }

    public BackfillJobDto updateBackfillJobState(String jobId, UpdateBackfillJobStateRequest request) {
        return callPatch("/backfill-jobs/" + jobId, request, new ParameterizedTypeReference<BackfillJobDto>() {});
    }

    public PageResponse<ForecastConfigDto> getForecastConfigs(String exchange, String marketType, String assetType,
                                                               String symbol, String horizon, Boolean enabled,
                                                               String search, Integer page, Integer size) {
        return callGetWithQuery("/forecast-configs", buildForecastQueryParams(
                exchange, marketType, assetType, symbol, horizon, enabled, search, page, size
        ), new ParameterizedTypeReference<PageResponse<ForecastConfigDto>>() {});
    }

    public ForecastConfigDto getForecastConfig(String configId) {
        return callGet("/forecast-configs/" + configId, new ParameterizedTypeReference<ForecastConfigDto>() {});
    }

    public ForecastConfigDto createForecastConfig(CreateForecastConfigRequest request) {
        return callPost("/forecast-configs", request, new ParameterizedTypeReference<ForecastConfigDto>() {});
    }

    public ForecastConfigDto updateForecastConfig(String configId, UpdateForecastConfigRequest request) {
        return callPut("/forecast-configs/" + configId, request, new ParameterizedTypeReference<ForecastConfigDto>() {});
    }

    public ForecastConfigDto patchForecastConfig(String configId, PartialUpdateForecastConfigRequest request) {
        return callPatch("/forecast-configs/" + configId, request, new ParameterizedTypeReference<ForecastConfigDto>() {});
    }

    private <T> T callGet(String path, ParameterizedTypeReference<T> typeRef) {
        long startTime = System.currentTimeMillis();
        try {
            T result = controlPlaneApiWebClient
                    .get()
                    .uri(path)
                    .retrieve()
                    .bodyToMono(typeRef)
                    .timeout(Duration.ofSeconds(10))
                    .block();

            long duration = System.currentTimeMillis() - startTime;
            log.info("GET {} - Status: 200 - Duration: {}ms", path, duration);
            return result;
        } catch (WebClientResponseException e) {
            long duration = System.currentTimeMillis() - startTime;
            String responseBody = e.getResponseBodyAsString();
            log.error("GET {} - Error {} after {}ms: {} | Response: {}", 
                    path, e.getStatusCode(), duration, e.getMessage(), 
                    responseBody != null && responseBody.length() > 500 
                            ? responseBody.substring(0, 500) + "..." 
                            : responseBody);
            throw e;
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            log.error("GET {} - Error after {}ms: {}", path, duration, e.getMessage(), e);
            throw new RuntimeException("Failed to call control-plane-service: " + path, e);
        }
    }

    private <T> T callGetWithQuery(String path, Map<String, String> queryParams, ParameterizedTypeReference<T> typeRef) {
        long startTime = System.currentTimeMillis();
        try {
            T result = controlPlaneApiWebClient
                    .get()
                    .uri(uriBuilder -> {
                        uriBuilder.path(path);
                        queryParams.forEach((key, value) -> {
                            if (value != null) {
                                uriBuilder.queryParam(key, value);
                            }
                        });
                        return uriBuilder.build();
                    })
                    .retrieve()
                    .bodyToMono(typeRef)
                    .timeout(Duration.ofSeconds(10))
                    .block();

            long duration = System.currentTimeMillis() - startTime;
            log.info("GET {} - Status: 200 - Duration: {}ms", path, duration);
            return result;
        } catch (WebClientResponseException e) {
            long duration = System.currentTimeMillis() - startTime;
            String responseBody = e.getResponseBodyAsString();
            log.error("GET {} - Error {} after {}ms: {} | Response: {}", 
                    path, e.getStatusCode(), duration, e.getMessage(), 
                    responseBody != null && responseBody.length() > 500 
                            ? responseBody.substring(0, 500) + "..." 
                            : responseBody);
            throw e;
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            log.error("GET {} - Error after {}ms: {}", path, duration, e.getMessage(), e);
            throw new RuntimeException("Failed to call control-plane-service: " + path, e);
        }
    }

    private <T> T callPost(String path, Object body, ParameterizedTypeReference<T> typeRef) {
        long startTime = System.currentTimeMillis();
        try {
            T result = controlPlaneApiWebClient
                    .post()
                    .uri(path)
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(typeRef)
                    .timeout(Duration.ofSeconds(30))
                    .block();

            long duration = System.currentTimeMillis() - startTime;
            log.info("POST {} - Status: 201 - Duration: {}ms", path, duration);
            return result;
        } catch (WebClientResponseException e) {
            long duration = System.currentTimeMillis() - startTime;
            String responseBody = e.getResponseBodyAsString();
            log.error("POST {} - Error {} after {}ms: {} | Response: {}", 
                    path, e.getStatusCode(), duration, e.getMessage(), 
                    responseBody != null && responseBody.length() > 500 
                            ? responseBody.substring(0, 500) + "..." 
                            : responseBody);
            throw e;
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            log.error("POST {} - Error after {}ms: {}", path, duration, e.getMessage(), e);
            throw new RuntimeException("Failed to call control-plane-service: " + path, e);
        }
    }

    private <T> T callPut(String path, Object body, ParameterizedTypeReference<T> typeRef) {
        long startTime = System.currentTimeMillis();
        try {
            T result = controlPlaneApiWebClient
                    .put()
                    .uri(path)
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(typeRef)
                    .timeout(Duration.ofSeconds(30))
                    .block();

            long duration = System.currentTimeMillis() - startTime;
            log.info("PUT {} - Status: 200 - Duration: {}ms", path, duration);
            return result;
        } catch (WebClientResponseException e) {
            long duration = System.currentTimeMillis() - startTime;
            String responseBody = e.getResponseBodyAsString();
            log.error("PUT {} - Error {} after {}ms: {} | Response: {}", 
                    path, e.getStatusCode(), duration, e.getMessage(), 
                    responseBody != null && responseBody.length() > 500 
                            ? responseBody.substring(0, 500) + "..." 
                            : responseBody);
            throw e;
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            log.error("PUT {} - Error after {}ms: {}", path, duration, e.getMessage(), e);
            throw new RuntimeException("Failed to call control-plane-service: " + path, e);
        }
    }

    private <T> T callPatch(String path, Object body, ParameterizedTypeReference<T> typeRef) {
        long startTime = System.currentTimeMillis();
        try {
            T result = controlPlaneApiWebClient
                    .patch()
                    .uri(path)
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(typeRef)
                    .timeout(Duration.ofSeconds(30))
                    .block();

            long duration = System.currentTimeMillis() - startTime;
            log.info("PATCH {} - Status: 200 - Duration: {}ms", path, duration);
            return result;
        } catch (WebClientResponseException e) {
            long duration = System.currentTimeMillis() - startTime;
            String responseBody = e.getResponseBodyAsString();
            log.error("PATCH {} - Error {} after {}ms: {} | Response: {}", 
                    path, e.getStatusCode(), duration, e.getMessage(), 
                    responseBody != null && responseBody.length() > 500 
                            ? responseBody.substring(0, 500) + "..." 
                            : responseBody);
            throw e;
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            log.error("PATCH {} - Error after {}ms: {}", path, duration, e.getMessage(), e);
            throw new RuntimeException("Failed to call control-plane-service: " + path, e);
        }
    }

    private Map<String, String> buildQueryParams(String exchange, String marketType, String assetType,
                                                  Boolean enabled, String search, Integer page, Integer size) {
        Map<String, String> params = new java.util.HashMap<>();
        if (exchange != null) params.put("exchange", exchange);
        if (marketType != null) params.put("marketType", marketType);
        if (assetType != null) params.put("assetType", assetType);
        if (enabled != null) params.put("enabled", enabled.toString());
        if (search != null) params.put("search", search);
        if (page != null) params.put("page", page.toString());
        if (size != null) params.put("size", size.toString());
        return params;
    }

    private Map<String, String> buildSuggestQueryParams(String exchange, String marketType, String search,
                                                         String base, Integer page, Integer size) {
        Map<String, String> params = new java.util.HashMap<>();
        params.put("exchange", exchange);
        params.put("marketType", marketType);
        if (search != null) params.put("search", search);
        if (base != null) params.put("base", base);
        if (page != null) params.put("page", page.toString());
        if (size != null) params.put("size", size.toString());
        return params;
    }

    private Map<String, String> buildStreamsQueryParams(String kind, String status, String exchange,
                                                         String assetType, String marketType, String region,
                                                         String window, String search, Integer page, Integer size) {
        Map<String, String> params = new java.util.HashMap<>();
        if (kind != null) params.put("kind", kind);
        if (status != null) params.put("status", status);
        if (exchange != null) params.put("exchange", exchange);
        if (assetType != null) params.put("assetType", assetType);
        if (marketType != null) params.put("marketType", marketType);
        if (region != null) params.put("region", region);
        if (window != null) params.put("window", window);
        if (search != null) params.put("search", search);
        if (page != null) params.put("page", page.toString());
        if (size != null) params.put("size", size.toString());
        return params;
    }

    private Map<String, String> buildMetricsQueryParams(Instant from, Instant to) {
        Map<String, String> params = new java.util.HashMap<>();
        if (from != null) params.put("from", from.toString());
        if (to != null) params.put("to", to.toString());
        return params;
    }

    private Map<String, String> buildBackfillQueryParams(String exchange, String marketType, String assetType,
                                                          String symbol, String status, String search,
                                                          Integer page, Integer size) {
        Map<String, String> params = new java.util.HashMap<>();
        if (exchange != null) params.put("exchange", exchange);
        if (marketType != null) params.put("marketType", marketType);
        if (assetType != null) params.put("assetType", assetType);
        if (symbol != null) params.put("symbol", symbol);
        if (status != null) params.put("status", status);
        if (search != null) params.put("search", search);
        if (page != null) params.put("page", page.toString());
        if (size != null) params.put("size", size.toString());
        return params;
    }

    private Map<String, String> buildForecastQueryParams(String exchange, String marketType, String assetType,
                                                          String symbol, String horizon, Boolean enabled,
                                                          String search, Integer page, Integer size) {
        Map<String, String> params = new java.util.HashMap<>();
        if (exchange != null) params.put("exchange", exchange);
        if (marketType != null) params.put("marketType", marketType);
        if (assetType != null) params.put("assetType", assetType);
        if (symbol != null) params.put("symbol", symbol);
        if (horizon != null) params.put("horizon", horizon);
        if (enabled != null) params.put("enabled", enabled.toString());
        if (search != null) params.put("search", search);
        if (page != null) params.put("page", page.toString());
        if (size != null) params.put("size", size.toString());
        return params;
    }
}

