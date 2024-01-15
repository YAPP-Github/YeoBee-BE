package com.example.yeobee.core.auth.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AppleAuthTokenResponseDto(@JsonProperty("refresh_token") String refreshToken) {

}
