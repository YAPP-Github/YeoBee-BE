package com.example.yeobee.common.dto.request;

import org.springframework.data.domain.PageRequest;

public record PageRequestDto(Integer pageIndex, Integer pageSize) {

    public PageRequest toPageRequest() {
        return PageRequest.of(pageIndex, pageSize);
    }
}
