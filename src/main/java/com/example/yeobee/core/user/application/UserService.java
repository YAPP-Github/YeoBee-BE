package com.example.yeobee.core.user.application;

import com.example.yeobee.common.exception.BusinessException;
import com.example.yeobee.common.exception.ErrorCode;
import com.example.yeobee.core.auth.domain.AuthProviderType;
import com.example.yeobee.core.user.domain.User;
import com.example.yeobee.core.user.domain.UserRepository;
import com.example.yeobee.core.user.dto.response.UserInfoResponseDto;
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
        return new UserInfoResponseDto(user.getNickname(),
                                       user.getProfileImage(),
                                       user.getAuthProviderList().get(0).getType());
    }
}
