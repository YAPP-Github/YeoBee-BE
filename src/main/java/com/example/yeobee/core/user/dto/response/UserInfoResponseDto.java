package com.example.yeobee.core.user.dto.response;

import com.example.yeobee.core.auth.domain.AuthProviderType;
import com.example.yeobee.core.user.domain.User;
import com.example.yeobee.core.user.domain.UserState;
import io.swagger.v3.oas.annotations.media.Schema;

public record UserInfoResponseDto(Long id, @Schema(nullable = true) String nickname,
                                  @Schema(nullable = true) String profileImage, AuthProviderType authProviderType,
                                  UserState userState) {

    public UserInfoResponseDto(User user) {
        this(user.getId(),
             user.getNickname(),
             user.getProfileImageUrl(),
             user.getAuthProvider().getType(),
             user.getState());
    }
}
