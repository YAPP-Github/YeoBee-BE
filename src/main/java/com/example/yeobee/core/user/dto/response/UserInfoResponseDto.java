package com.example.yeobee.core.user.dto.response;

import com.example.yeobee.core.auth.domain.AuthProviderType;

public record UserInfoResponseDto(String nickname, String profileImage, AuthProviderType authProviderType) {
}
