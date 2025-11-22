package com.example.controlplanebff.controller;

import com.example.controlplanebff.dto.ui.ControlPlaneStreamsStateQuery;
import com.example.controlplanebff.dto.ui.ControlPlaneStreamsStateResponse;
import com.example.controlplanebff.dto.ui.DictionariesResponse;
import com.example.controlplanebff.dto.ui.MarketsQuery;
import com.example.controlplanebff.dto.ui.MarketsResponse;
import com.example.controlplanebff.service.ControlPlaneStreamsService;
import com.example.controlplanebff.service.DictionariesService;
import com.example.controlplanebff.service.MarketsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ui/control")
@RequiredArgsConstructor
public class ControlPlaneStreamsController {

    private final ControlPlaneStreamsService streamsService;
    private final DictionariesService dictionariesService;
    private final MarketsService marketsService;

    @GetMapping("/dictionaries")
    public ResponseEntity<DictionariesResponse> getDictionaries() {
        DictionariesResponse response = dictionariesService.getDictionaries();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/streams/state")
    public ResponseEntity<ControlPlaneStreamsStateResponse> getStreamsState(
            @Valid @ModelAttribute ControlPlaneStreamsStateQuery query) {
        
        ControlPlaneStreamsStateResponse response = streamsService.getStreamsState(query);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/markets")
    public ResponseEntity<MarketsResponse> getMarkets(
            @Valid @ModelAttribute MarketsQuery query) {
        
        MarketsResponse response = marketsService.getMarkets(
                query.getAssetType(), 
                query.getExchange(), 
                query.getMarketType());
        return ResponseEntity.ok(response);
    }
}


