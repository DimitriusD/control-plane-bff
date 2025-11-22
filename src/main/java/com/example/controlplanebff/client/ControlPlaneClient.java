package com.example.controlplanebff.client;

import com.example.controlplanebff.dto.domain.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ControlPlaneClient {

    private final WebClient controlPlaneWebClient;

    public List<ExchangeDto> getExchanges() {
        long startTime = System.currentTimeMillis();
        try {
            List<ExchangeDto> exchanges = controlPlaneWebClient
                    .get()
                    .uri("/api/exchanges")
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<ExchangeDto>>() {})
                    .timeout(Duration.ofSeconds(10))
                    .block();
            
            long duration = System.currentTimeMillis() - startTime;
            log.info("GET /api/exchanges - Status: 200 - Duration: {}ms", duration);
            return exchanges != null ? exchanges : List.of();
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            log.error("GET /api/exchanges - Error after {}ms: {}", duration, e.getMessage(), e);
            throw new RuntimeException("Failed to fetch exchanges from control-plane-service", e);
        }
    }

    public List<MarketSymbolDto> getMarketSymbols(String exchange, String marketType, String assetType, Boolean enabled) {
        long startTime = System.currentTimeMillis();
        try {
            List<MarketSymbolDto> symbols = controlPlaneWebClient
                    .get()
                    .uri(uriBuilder -> {
                        uriBuilder.path("/api/markets/symbols");
                        if (exchange != null) {
                            uriBuilder.queryParam("exchange", exchange);
                        }
                        if (marketType != null) {
                            uriBuilder.queryParam("marketType", marketType);
                        }
                        if (assetType != null) {
                            uriBuilder.queryParam("assetType", assetType);
                        }
                        if (enabled != null) {
                            uriBuilder.queryParam("enabled", enabled);
                        }
                        return uriBuilder.build();
                    })
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<MarketSymbolDto>>() {})
                    .timeout(Duration.ofSeconds(10))
                    .block();

            long duration = System.currentTimeMillis() - startTime;
            log.info("GET /api/markets/symbols - Status: 200 - Duration: {}ms", duration);
            return symbols != null ? symbols : List.of();
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            log.error("GET /api/markets/symbols - Error after {}ms: {}", duration, e.getMessage(), e);
            throw new RuntimeException("Failed to fetch market symbols from control-plane-service", e);
        }
    }

    public StreamConfigDto getStreamConfig(String exchange, String marketType, String symbol) {
        long startTime = System.currentTimeMillis();
        try {
            StreamConfigDto config = controlPlaneWebClient
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/api/streams/config")
                            .queryParam("exchange", exchange)
                            .queryParam("marketType", marketType)
                            .queryParam("symbol", symbol)
                            .build())
                    .retrieve()
                    .bodyToMono(StreamConfigDto.class)
                    .timeout(Duration.ofSeconds(10))
                    .block();

            long duration = System.currentTimeMillis() - startTime;
            log.info("GET /api/streams/config - Status: 200 - Duration: {}ms", duration);
            return config;
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            log.error("GET /api/streams/config - Error after {}ms: {}", duration, e.getMessage(), e);
            throw new RuntimeException("Failed to fetch stream config from control-plane-service", e);
        }
    }

    public StreamConfigDto upsertStreamConfig(UpsertStreamConfigRequest request) {
        long startTime = System.currentTimeMillis();
        try {
            StreamConfigDto config = controlPlaneWebClient
                    .put()
                    .uri("/api/streams/config")
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(StreamConfigDto.class)
                    .timeout(Duration.ofSeconds(10))
                    .block();

            long duration = System.currentTimeMillis() - startTime;
            log.info("PUT /api/streams/config - Status: 200 - Duration: {}ms", duration);
            return config;
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            log.error("PUT /api/streams/config - Error after {}ms: {}", duration, e.getMessage(), e);
            throw new RuntimeException("Failed to upsert stream config in control-plane-service", e);
        }
    }

    public ForecastConfigDto getForecastConfig(String exchange, String marketType, String symbol) {
        long startTime = System.currentTimeMillis();
        try {
            ForecastConfigDto config = controlPlaneWebClient
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/api/forecast/config")
                            .queryParam("exchange", exchange)
                            .queryParam("marketType", marketType)
                            .queryParam("symbol", symbol)
                            .build())
                    .retrieve()
                    .bodyToMono(ForecastConfigDto.class)
                    .timeout(Duration.ofSeconds(10))
                    .block();

            long duration = System.currentTimeMillis() - startTime;
            log.info("GET /api/forecast/config - Status: 200 - Duration: {}ms", duration);
            return config;
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            log.error("GET /api/forecast/config - Error after {}ms: {}", duration, e.getMessage(), e);
            throw new RuntimeException("Failed to fetch forecast config from control-plane-service", e);
        }
    }

    public ForecastConfigDto upsertForecastConfig(UpsertForecastConfigRequest request) {
        long startTime = System.currentTimeMillis();
        try {
            ForecastConfigDto config = controlPlaneWebClient
                    .put()
                    .uri("/api/forecast/config")
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(ForecastConfigDto.class)
                    .timeout(Duration.ofSeconds(10))
                    .block();

            long duration = System.currentTimeMillis() - startTime;
            log.info("PUT /api/forecast/config - Status: 200 - Duration: {}ms", duration);
            return config;
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            log.error("PUT /api/forecast/config - Error after {}ms: {}", duration, e.getMessage(), e);
            throw new RuntimeException("Failed to upsert forecast config in control-plane-service", e);
        }
    }

    public BackfillJobDto createBackfillJob(CreateBackfillJobRequest request) {
        long startTime = System.currentTimeMillis();
        try {
            BackfillJobDto job = controlPlaneWebClient
                    .post()
                    .uri("/api/backfill/jobs")
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(BackfillJobDto.class)
                    .timeout(Duration.ofSeconds(30))
                    .block();

            long duration = System.currentTimeMillis() - startTime;
            log.info("POST /api/backfill/jobs - Status: 200 - Duration: {}ms", duration);
            return job;
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            log.error("POST /api/backfill/jobs - Error after {}ms: {}", duration, e.getMessage(), e);
            throw new RuntimeException("Failed to create backfill job in control-plane-service", e);
        }
    }

    public PageDto<BackfillJobDto> getBackfillJobs(String exchange, String marketType, String symbol, Integer page, Integer size) {
        long startTime = System.currentTimeMillis();
        try {
            PageDto<BackfillJobDto> pageResult = controlPlaneWebClient
                    .get()
                    .uri(uriBuilder -> {
                        uriBuilder.path("/api/backfill/jobs");
                        if (exchange != null) {
                            uriBuilder.queryParam("exchange", exchange);
                        }
                        if (marketType != null) {
                            uriBuilder.queryParam("marketType", marketType);
                        }
                        if (symbol != null) {
                            uriBuilder.queryParam("symbol", symbol);
                        }
                        if (page != null) {
                            uriBuilder.queryParam("page", page);
                        }
                        if (size != null) {
                            uriBuilder.queryParam("size", size);
                        }
                        return uriBuilder.build();
                    })
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<PageDto<BackfillJobDto>>() {})
                    .timeout(Duration.ofSeconds(10))
                    .block();

            long duration = System.currentTimeMillis() - startTime;
            log.info("GET /api/backfill/jobs - Status: 200 - Duration: {}ms", duration);
            return pageResult != null ? pageResult : PageDto.<BackfillJobDto>builder().content(List.of()).build();
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            log.error("GET /api/backfill/jobs - Error after {}ms: {}", duration, e.getMessage(), e);
            throw new RuntimeException("Failed to fetch backfill jobs from control-plane-service", e);
        }
    }
}

