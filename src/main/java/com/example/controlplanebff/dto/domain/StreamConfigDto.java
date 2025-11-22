package com.example.controlplanebff.dto.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StreamConfigDto {
    private Long id;
    private String exchange;
    private String marketType;
    private String symbol;
    private Boolean marketStreamEnabled;
    private Boolean newsStreamEnabled;
    private String marketStreamStatus;
    private String newsStreamStatus;
}



