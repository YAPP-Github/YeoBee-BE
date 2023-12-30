package com.example.yeobee.common.dto;

import com.example.yeobee.common.exception.ErrorCode;

public record ErrorResponseDto(int code, String message) {

    public ErrorResponseDto(ErrorCode errorCode) {
        this(errorCode.getCode(), errorCode.getMessage());
    }
}
