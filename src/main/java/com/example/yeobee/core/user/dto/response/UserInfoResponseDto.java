package com.example.yeobee.core.user.dto.response;

import com.example.yeobee.core.auth.domain.AuthProviderType;
import com.example.yeobee.core.user.domain.User;

public record UserInfoResponseDto(String nickname, String profileImage, AuthProviderType authProviderType) {

    public UserInfoResponseDto(User user) {
        this(user.getNickname(), user.getProfileImageUrl(), user.getAuthProviderList().get(0).getType());
    }
}
