package com.example.yeobee.core.user.dto.response;

import com.example.yeobee.core.user.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;

public record UserUpdateResponseDto(@Schema(nullable = true) String nickname,
                                    @Schema(nullable = true) String profileImageUrl) {

    public UserUpdateResponseDto(User user) {
        this(user.getNickname(), user.getProfileImageUrl());
    }
}
