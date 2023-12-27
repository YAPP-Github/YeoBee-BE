package com.example.yeobee.auth.authToken.dto.response;

import lombok.Builder;

public record AuthResponseDto(String appToken, String refreshToken, Boolean isNew) {

    @Builder
    public AuthResponseDto {
    }
}
