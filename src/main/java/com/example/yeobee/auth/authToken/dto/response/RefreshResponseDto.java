package com.example.yeobee.auth.authToken.dto.response;

import lombok.Builder;

public record RefreshResponseDto(String appToken, String refreshToken) {

    @Builder
    public RefreshResponseDto {
    }
}
