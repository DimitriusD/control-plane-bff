package com.example.controlplanebff.controller;

import com.example.controlplanebff.dto.*;
import com.example.controlplanebff.service.BackfillJobsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cp/v1")
@RequiredArgsConstructor
public class BackfillJobsController {

    private final BackfillJobsService backfillJobsService;

    @GetMapping("/backfill-jobs")
    public ResponseEntity<PageResponse<BackfillJobDto>> listJobs(
            @RequestParam(required = false) String exchange,
            @RequestParam(required = false) String marketType,
            @RequestParam(required = false) String assetType,
            @RequestParam(required = false) String symbol,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String search,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "20") Integer size) {
        
        PageResponse<BackfillJobDto> response = backfillJobsService.listJobs(
                exchange, marketType, assetType, symbol, status, search, page, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/backfill-jobs/stats")
    public ResponseEntity<BackfillStatsResponse> getStats() {
        BackfillStatsResponse response = backfillJobsService.getStats();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/backfill-jobs/{jobId}")
    public ResponseEntity<BackfillJobDto> getJob(@PathVariable String jobId) {
        BackfillJobDto job = backfillJobsService.getJob(jobId);
        if (job == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(job);
    }

    @PostMapping("/backfill-jobs")
    public ResponseEntity<BackfillJobDto> createJob(@Valid @RequestBody CreateBackfillJobRequest request) {
        BackfillJobDto job = backfillJobsService.createJob(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(job);
    }

    @PatchMapping("/backfill-jobs/{jobId}")
    public ResponseEntity<BackfillJobDto> updateJobState(
            @PathVariable String jobId,
            @Valid @RequestBody UpdateBackfillJobStateRequest request) {
        
        BackfillJobDto job = backfillJobsService.updateJobState(jobId, request);
        if (job == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(job);
    }
}

