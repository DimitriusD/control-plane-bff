package com.example.controlplanebff.controller;

import com.example.controlplanebff.dto.*;
import com.example.controlplanebff.service.ForecastConfigsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cp/v1")
@RequiredArgsConstructor
public class ForecastConfigsController {

    private final ForecastConfigsService forecastConfigsService;

    @GetMapping("/forecast-configs")
    public ResponseEntity<PageResponse<ForecastConfigDto>> listConfigs(
            @RequestParam(required = false) String exchange,
            @RequestParam(required = false) String marketType,
            @RequestParam(required = false) String assetType,
            @RequestParam(required = false) String symbol,
            @RequestParam(required = false) String horizon,
            @RequestParam(required = false) Boolean enabled,
            @RequestParam(required = false) String search,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "20") Integer size) {
        
        PageResponse<ForecastConfigDto> response = forecastConfigsService.listConfigs(
                exchange, marketType, assetType, symbol, horizon, enabled, search, page, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/forecast-configs/{configId}")
    public ResponseEntity<ForecastConfigDto> getConfig(@PathVariable String configId) {
        ForecastConfigDto config = forecastConfigsService.getConfig(configId);
        if (config == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(config);
    }

    @PostMapping("/forecast-configs")
    public ResponseEntity<ForecastConfigDto> createConfig(@Valid @RequestBody CreateForecastConfigRequest request) {
        ForecastConfigDto config = forecastConfigsService.createConfig(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(config);
    }

    @PutMapping("/forecast-configs/{configId}")
    public ResponseEntity<ForecastConfigDto> updateConfig(
            @PathVariable String configId,
            @Valid @RequestBody UpdateForecastConfigRequest request) {
        
        ForecastConfigDto config = forecastConfigsService.updateConfig(configId, request);
        if (config == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(config);
    }

    @PatchMapping("/forecast-configs/{configId}")
    public ResponseEntity<ForecastConfigDto> partialUpdateConfig(
            @PathVariable String configId,
            @Valid @RequestBody PartialUpdateForecastConfigRequest request) {
        
        ForecastConfigDto config = forecastConfigsService.partialUpdateConfig(configId, request);
        if (config == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(config);
    }
}

