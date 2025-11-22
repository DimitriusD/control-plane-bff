package com.example.controlplanebff.mapper;

import com.example.controlplanebff.dto.domain.StreamConfigDto;
import com.example.controlplanebff.dto.ui.MarketStreamState;
import com.example.controlplanebff.dto.ui.NewsStreamState;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface StreamConfigMapper {
    StreamConfigMapper INSTANCE = Mappers.getMapper(StreamConfigMapper.class);

    @Mapping(source = "marketStreamEnabled", target = "enabled")
    @Mapping(source = "marketStreamStatus", target = "status")
    MarketStreamState toMarketStreamState(StreamConfigDto dto);

    @Mapping(source = "newsStreamEnabled", target = "enabled")
    @Mapping(source = "newsStreamStatus", target = "status")
    NewsStreamState toNewsStreamState(StreamConfigDto dto);
}



