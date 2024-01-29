package com.example.yeobee.core.user.dto.response;

import com.example.yeobee.core.auth.domain.AuthProviderType;
import com.example.yeobee.core.user.domain.User;

public record UserInfoResponseDto(Long id, String nickname, String profileImage, AuthProviderType authProviderType) {

    public UserInfoResponseDto(User user) {
        this(user.getId(), user.getNickname(), user.getProfileImageUrl(), user.getAuthProvider().getType());
    }
}
