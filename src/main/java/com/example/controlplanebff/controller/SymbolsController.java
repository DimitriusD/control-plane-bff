package com.example.controlplanebff.controller;

import com.example.controlplanebff.dto.PageResponse;
import com.example.controlplanebff.dto.SymbolItem;
import com.example.controlplanebff.service.SymbolsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cp/v1")
@RequiredArgsConstructor
public class SymbolsController {

    private final SymbolsService symbolsService;

    @GetMapping("/exchanges/{exchangeCode}/markets/{marketType}/symbols")
    public ResponseEntity<PageResponse<SymbolItem>> searchSymbols(
            @PathVariable String exchangeCode,
            @PathVariable String marketType,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String base,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "20") Integer size) {
        
        PageResponse<SymbolItem> response = symbolsService.searchSymbols(
                exchangeCode, marketType, search, base, page, size);
        return ResponseEntity.ok(response);
    }
}

