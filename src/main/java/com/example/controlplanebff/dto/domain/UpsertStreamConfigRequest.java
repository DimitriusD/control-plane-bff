package com.example.controlplanebff.dto.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpsertStreamConfigRequest {
    private String exchange;
    private String marketType;
    private String symbol;
    private Boolean marketStreamEnabled;
    private Boolean newsStreamEnabled;
}



