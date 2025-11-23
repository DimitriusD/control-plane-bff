package com.example.controlplanebff.controller;

import com.example.controlplanebff.dto.DictionariesResponse;
import com.example.controlplanebff.service.DictionariesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cp/v1")
@RequiredArgsConstructor
public class DictionariesController {

    private final DictionariesService dictionariesService;

    @GetMapping("/dictionaries")
    public ResponseEntity<DictionariesResponse> getDictionaries() {
        DictionariesResponse response = dictionariesService.getDictionaries();
        return ResponseEntity.ok(response);
    }
}

