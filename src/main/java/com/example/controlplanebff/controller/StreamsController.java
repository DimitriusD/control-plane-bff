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
    public ResponseEntity<PageResponse<StreamGroupDto>> listStreams(
            @RequestParam(required = true) String kind,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String exchange,
            @RequestParam(required = false) String assetType,
            @RequestParam(required = false) String marketType,
            @RequestParam(required = false) String region,
            @RequestParam(required = false) String window,
            @RequestParam(required = false) String search,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "20") Integer size) {
        
        PageResponse<StreamGroupDto> response = streamsService.listStreams(
                kind, status, exchange, assetType, marketType, region, window, search, page, size);
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
    public ResponseEntity<StreamGroupDto> getStream(@PathVariable String streamId) {
        StreamGroupDto stream = streamsService.getStream(streamId);
        if (stream == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(stream);
    }

    @PostMapping("/streams")
    public ResponseEntity<StreamGroupDto> createStream(@Valid @RequestBody CreateStreamRequest request) {
        StreamGroupDto stream = streamsService.createStream(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(stream);
    }

    @PatchMapping("/streams/{streamId}")
    public ResponseEntity<StreamGroupDto> updateStreamState(
            @PathVariable String streamId,
            @Valid @RequestBody UpdateStreamStateRequest request) {
        
        StreamGroupDto stream = streamsService.updateStreamState(streamId, request);
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

