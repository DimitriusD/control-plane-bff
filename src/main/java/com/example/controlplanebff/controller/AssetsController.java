package com.example.controlplanebff.controller;

import com.example.controlplanebff.dto.AssetsResponse;
import com.example.controlplanebff.service.AssetsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cp/v1")
@RequiredArgsConstructor
public class AssetsController {

    private final AssetsService assetsService;

    @GetMapping("/exchanges/{exchangeCode}/markets/{marketType}/assets/base")
    public ResponseEntity<AssetsResponse> getBaseAssets(
            @PathVariable String exchangeCode,
            @PathVariable String marketType) {
        
        AssetsResponse response = assetsService.getBaseAssets(exchangeCode, marketType);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/exchanges/{exchangeCode}/markets/{marketType}/assets/base/{baseAssetCode}/quotes")
    public ResponseEntity<AssetsResponse> getQuoteAssets(
            @PathVariable String exchangeCode,
            @PathVariable String marketType,
            @PathVariable String baseAssetCode) {
        
        AssetsResponse response = assetsService.getQuoteAssets(exchangeCode, marketType, baseAssetCode);
        return ResponseEntity.ok(response);
    }
}

