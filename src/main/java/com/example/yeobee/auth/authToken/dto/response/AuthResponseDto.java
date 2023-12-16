package com.example.yeobee.auth.authToken.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponseDto {

    private String appToken;
    private String refreshToken;
}
