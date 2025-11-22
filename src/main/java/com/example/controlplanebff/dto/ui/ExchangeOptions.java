package com.example.controlplanebff.dto.ui;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeOptions {
    private String code;
    private String name;
    private List<String> marketTypes;
    private Set<String> baseAssets;
    private Set<String> quoteAssets;
}



