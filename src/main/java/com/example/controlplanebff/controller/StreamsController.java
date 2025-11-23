package com.example.controlplanebff.controller;

import com.example.controlplanebff.dto.*;
import com.example.controlplanebff.service.StreamsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/api/cp/v1")
@RequiredArgsConstructor
public class StreamsController {

    private final StreamsService streamsService;

    @GetMapping("/streams")
    public ResponseEntity<PageResponse<StreamDto>> listStreams(
            @RequestParam(required = true) String kind,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String exchange,
            @RequestParam(required = false) String assetType,
            @RequestParam(required = false) String search,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "20") Integer size) {
        
        PageResponse<StreamDto> response = streamsService.listStreams(
                kind, status, exchange, assetType, search, page, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/streams/stats")
    public ResponseEntity<StreamStatsResponse> getStats(
            @RequestParam(required = false) String kind,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String exchange,
            @RequestParam(required = false) String assetType,
            @RequestParam(required = false) String search) {
        
        StreamStatsResponse response = streamsService.getStats(
                kind, status, exchange, assetType, search);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/streams/{streamId}")
    public ResponseEntity<StreamDto> getStream(@PathVariable String streamId) {
        StreamDto stream = streamsService.getStream(streamId);
        if (stream == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(stream);
    }

    @PostMapping("/streams")
    public ResponseEntity<StreamDto> createStream(@Valid @RequestBody CreateStreamRequest request) {
        StreamDto stream = streamsService.createStream(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(stream);
    }

    @PatchMapping("/streams/{streamId}")
    public ResponseEntity<StreamDto> updateStreamState(
            @PathVariable String streamId,
            @Valid @RequestBody UpdateStreamStateRequest request) {
        
        StreamDto stream = streamsService.updateStreamState(streamId, request);
        if (stream == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(stream);
    }

    @GetMapping("/streams/{streamId}/metrics")
    public ResponseEntity<StreamMetricsResponse> getStreamMetrics(
            @PathVariable String streamId,
            @RequestParam(required = false) Instant from,
            @RequestParam(required = false) Instant to) {
        
        StreamMetricsResponse response = streamsService.getStreamMetrics(streamId, from, to);
        return ResponseEntity.ok(response);
    }
}

