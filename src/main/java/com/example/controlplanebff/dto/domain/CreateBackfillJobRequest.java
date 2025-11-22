package com.example.controlplanebff.dto.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateBackfillJobRequest {
    private String exchange;
    private String marketType;
    private String symbol;
    private Instant fromTs;
    private Instant toTs;
    private String resolution;
}



