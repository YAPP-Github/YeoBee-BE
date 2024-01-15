package com.example.yeobee.core.user.dto.response;

import com.example.yeobee.core.user.domain.User;

public record UserUpdateResponseDto(String nickname, String profileImageUrl) {

    public UserUpdateResponseDto(User user) {
        this(user.getNickname(), user.getProfileImageUrl());
    }
}
