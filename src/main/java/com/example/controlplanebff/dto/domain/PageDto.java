package com.example.controlplanebff.dto.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageDto<T> {
    private List<T> content;
    private Integer page;
    private Integer size;
    private Long totalElements;
    private Integer totalPages;
}



