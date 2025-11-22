package com.example.controlplanebff.mapper;

import com.example.controlplanebff.dto.domain.ForecastConfigDto;
import com.example.controlplanebff.dto.ui.ForecastingState;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ForecastConfigMapper {
    ForecastConfigMapper INSTANCE = Mappers.getMapper(ForecastConfigMapper.class);

    ForecastingState toForecastingState(ForecastConfigDto dto);
}



