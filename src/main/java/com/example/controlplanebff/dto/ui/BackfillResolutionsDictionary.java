package com.example.controlplanebff.dto.ui;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BackfillResolutionsDictionary {
    private List<String> items;
    
    @JsonProperty("default")
    private String default_;
}

