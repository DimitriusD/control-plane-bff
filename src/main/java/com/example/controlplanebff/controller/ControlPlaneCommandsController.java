package com.example.controlplanebff.controller;

import com.example.controlplanebff.dto.domain.BackfillJobDto;
import com.example.controlplanebff.dto.ui.*;
import com.example.controlplanebff.service.ControlPlaneCommandsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ui/control")
@RequiredArgsConstructor
public class ControlPlaneCommandsController {

    private final ControlPlaneCommandsService commandsService;

    @PostMapping("/streams/market/toggle")
    public ResponseEntity<SymbolStreamsState> toggleMarketStream(
            @Valid @RequestBody ToggleMarketStreamRequest request) {
        
        SymbolStreamsState response = commandsService.toggleMarketStream(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/streams/news/toggle")
    public ResponseEntity<SymbolStreamsState> toggleNewsStream(
            @Valid @RequestBody ToggleNewsStreamRequest request) {
        
        SymbolStreamsState response = commandsService.toggleNewsStream(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/forecast/config")
    public ResponseEntity<ForecastingState> upsertForecastConfig(
            @Valid @RequestBody UpsertForecastConfigUiRequest request) {
        
        ForecastingState response = commandsService.upsertForecastConfig(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/backfill/jobs")
    public ResponseEntity<BackfillJobDto> createBackfillJob(
            @Valid @RequestBody CreateBackfillJobUiRequest request) {
        
        BackfillJobDto response = commandsService.createBackfillJob(request);
        return ResponseEntity.ok(response);
    }
}



