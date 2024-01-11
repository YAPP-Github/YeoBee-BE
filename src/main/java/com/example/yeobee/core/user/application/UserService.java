package com.example.yeobee.core.user.application;

import com.example.yeobee.common.exception.BusinessException;
import com.example.yeobee.common.exception.ErrorCode;
import com.example.yeobee.core.auth.application.AppleAuthService;
import com.example.yeobee.core.auth.application.KakaoAuthService;
import com.example.yeobee.core.auth.domain.AuthProvider;
import com.example.yeobee.core.auth.domain.AuthProviderType;
import com.example.yeobee.core.user.domain.User;
import com.example.yeobee.core.user.domain.UserRepository;
import com.example.yeobee.core.user.dto.request.UserUpdateRequestDto;
import com.example.yeobee.core.user.dto.response.UserInfoResponseDto;
import com.example.yeobee.core.user.dto.response.UserUpdateResponseDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AppleAuthService appleAuthService;
    private final KakaoAuthService kakaoAuthService;

    public UserInfoResponseDto getUserInfo(User user) {
        return new UserInfoResponseDto(user);
    }

    @Transactional
    public UserUpdateResponseDto updateUserInfo(User user, UserUpdateRequestDto request) {
        user.updateInfo(request.nickname(), request.profileImageUrl());
        return new UserUpdateResponseDto(userRepository.save(user));
    }

    public void deleteUser(User user) {
        AuthProvider authProvider = user.getAuthProvider();
        switch (authProvider.getType()) {
            case APPLE -> appleAuthService.revoke(authProvider);
            case KAKAO -> kakaoAuthService.revoke(authProvider);
            default -> throw new BusinessException(ErrorCode.AUTH_PROVIDER_TYPE_INVALID);
        }
        userRepository.delete(user);
    }
}
