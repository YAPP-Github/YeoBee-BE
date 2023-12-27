package com.example.yeobee.auth.apple.dto.response;

public record AppleAuthTokenResponseDto(String access_token, String refresh_token, String id_token) {

}
