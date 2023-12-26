package com.example.yeobee.auth.apple.dto.response;

import lombok.Getter;

@Getter
public class AppleAuthTokenResponseDto {

    private String access_token;
    private String refresh_token;
    private String id_token;
}
