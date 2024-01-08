package com.example.yeobee.core.user.application;

import com.example.yeobee.common.exception.BusinessException;
import com.example.yeobee.common.exception.ErrorCode;
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

    public UserInfoResponseDto getUserInfo(User user) {
        if (user.getAuthProviderList().isEmpty()) {
            throw new BusinessException(ErrorCode.AUTH_PROVIDER_NOT_FOUND);
        }
        return new UserInfoResponseDto(user);
    }

    @Transactional
    public UserUpdateResponseDto updateUserInfo(User user, UserUpdateRequestDto request) {
        user.updateInfo(request.nickname(), request.profileImageUrl());
        return new UserUpdateResponseDto(user);
    }
}
