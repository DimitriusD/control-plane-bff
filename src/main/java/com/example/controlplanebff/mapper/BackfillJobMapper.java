package com.example.controlplanebff.mapper;

import com.example.controlplanebff.dto.domain.BackfillJobDto;
import com.example.controlplanebff.dto.ui.BackfillState;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BackfillJobMapper {
    BackfillJobMapper INSTANCE = Mappers.getMapper(BackfillJobMapper.class);

    @Mapping(source = "status", target = "lastJobStatus")
    @Mapping(source = "fromTs", target = "lastJobFrom")
    @Mapping(source = "toTs", target = "lastJobTo")
    BackfillState toBackfillState(BackfillJobDto dto);
}



