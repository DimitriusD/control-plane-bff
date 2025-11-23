package com.example.controlplanebff.dto.upstream;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WindowDto {
    private String code;
    private String description;
    private Boolean enabled;
}

