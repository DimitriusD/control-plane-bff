package com.example.controlplanebff.service;

import com.example.controlplanebff.dto.PageResponse;
import com.example.controlplanebff.dto.SymbolItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MockSymbolsService {

    // Mock symbols database
    private static final List<SymbolItem> ALL_SYMBOLS = List.of(
            SymbolItem.builder().symbol("BTCUSDT").base("BTC").quote("USDT").build(),
            SymbolItem.builder().symbol("BTCEUR").base("BTC").quote("EUR").build(),
            SymbolItem.builder().symbol("BTCFDUSD").base("BTC").quote("FDUSD").build(),
            SymbolItem.builder().symbol("ETHUSDT").base("ETH").quote("USDT").build(),
            SymbolItem.builder().symbol("ETHEUR").base("ETH").quote("EUR").build(),
            SymbolItem.builder().symbol("ETHBTC").base("ETH").quote("BTC").build(),
            SymbolItem.builder().symbol("SOLUSDT").base("SOL").quote("USDT").build(),
            SymbolItem.builder().symbol("SOLBTC").base("SOL").quote("BTC").build(),
            SymbolItem.builder().symbol("XRPUSDT").base("XRP").quote("USDT").build(),
            SymbolItem.builder().symbol("BNBUSDT").base("BNB").quote("USDT").build()
    );

    public PageResponse<SymbolItem> searchSymbols(String exchangeCode, String marketType, 
                                                   String search, String base, 
                                                   Integer page, Integer size) {
        log.info("Searching symbols: exchange={}, marketType={}, search={}, base={}", 
                exchangeCode, marketType, search, base);

        List<SymbolItem> filtered = new ArrayList<>(ALL_SYMBOLS);

        // Filter by search (case-insensitive substring)
        if (search != null && !search.isBlank()) {
            String searchLower = search.toLowerCase();
            filtered = filtered.stream()
                    .filter(s -> s.getSymbol().toLowerCase().contains(searchLower) ||
                                 s.getBase().toLowerCase().contains(searchLower) ||
                                 s.getQuote().toLowerCase().contains(searchLower))
                    .collect(Collectors.toList());
        }

        // Filter by base
        if (base != null && !base.isBlank()) {
            filtered = filtered.stream()
                    .filter(s -> base.equalsIgnoreCase(s.getBase()))
                    .collect(Collectors.toList());
        }

        // Pagination
        int pageNum = page != null && page >= 0 ? page : 0;
        int sizeNum = size != null && size > 0 ? size : 20;
        int start = pageNum * sizeNum;
        int end = Math.min(start + sizeNum, filtered.size());

        List<SymbolItem> items = start < filtered.size() 
                ? filtered.subList(start, end) 
                : List.of();

        return PageResponse.<SymbolItem>builder()
                .items(items)
                .page(pageNum)
                .size(sizeNum)
                .total((long) filtered.size())
                .build();
    }
}

