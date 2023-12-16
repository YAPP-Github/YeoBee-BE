package com.example.yeobee.auth.apple.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AppleLoginRequestDto {
    private String code;
    private String id_token;
}
